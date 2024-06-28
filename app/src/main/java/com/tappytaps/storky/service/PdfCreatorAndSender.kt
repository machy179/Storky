package com.tappytaps.storky.service

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.Toast
import androidx.core.content.FileProvider
import com.tappytaps.storky.model.Contraction
import com.tappytaps.storky.utils.convertCalendarToText
import com.tappytaps.storky.utils.convertSecondsToTimeString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

import com.itextpdf.text.*
import com.itextpdf.text.pdf.BaseFont
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import com.tappytaps.storky.R
import com.tappytaps.storky.utils.getDateInShare
import java.io.ByteArrayOutputStream

class PdfCreatorAndSender() {


    suspend fun convertToPdf(filteredContractionList: List<Contraction>, context: Context): File =
        withContext(
            Dispatchers.IO
        ) {

            val robotoFontPath = "res/font/roboto_regular.ttf"
            val baseFont = BaseFont.createFont(robotoFontPath, BaseFont.CP1250, BaseFont.EMBEDDED)
            val regularFont = Font(baseFont, 13f, Font.NORMAL, BaseColor(0, 0, 0))
            val titleFont = Font(baseFont, 20f, Font.BOLD, BaseColor(0, 0, 0))
            val descriptionFont = Font(baseFont, 13f, Font.NORMAL, BaseColor(138, 138, 143))
            val dateFont = Font(baseFont, 13f, Font.BOLD, BaseColor(0, 0, 0))
            val contractionTimeFont = Font(baseFont, 17f, Font.BOLD, BaseColor(0, 0, 0))
            val headerColor = BaseColor(138, 138, 143)

            val resources = context.resources
            val pdfFileName = resources.getString(R.string.contraction_report_pdf)
            val pdfFile = File(context.cacheDir, pdfFileName)

            //     val pdfFile = File.createTempFile("contraction_report", ".pdf", context.cacheDir)
            val document = Document()

            val titleText = resources.getString(R.string.overview_of_contractions_title)
            val descriptionText = resources.getString(R.string.overview_of_contractions_text_in_pdf)


            PdfWriter.getInstance(document, FileOutputStream(pdfFile))
            document.open()
            document.add(Paragraph("\n"))

            // Load the logo.png image from res/drawable
            val bitmap = BitmapFactory.decodeResource(resources, R.drawable.logo_pdf)
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val imageBytes = stream.toByteArray()
            val logo = Image.getInstance(imageBytes)
            logo.scaleToFit(100f, 100f) // Adjust the size as needed
            logo.alignment = Image.ALIGN_CENTER
            document.add(logo)

            // Add title
            val title = Paragraph(titleText, titleFont).apply {
                alignment = Element.ALIGN_CENTER
            }
            document.add(title)
            document.add(Paragraph("\n"))

            // Add description
            val descriptionTable = PdfPTable(1)
            descriptionTable.totalWidth = 375f
            descriptionTable.isLockedWidth = true

            val descriptionCell = PdfPCell(Paragraph(descriptionText, descriptionFont)).apply {
                border = Rectangle.NO_BORDER
                paddingBottom = 10f
                horizontalAlignment = Element.ALIGN_CENTER
            }
            descriptionTable.addCell(descriptionCell)

            document.add(descriptionTable)

            document.add(Paragraph("\n"))

            // Add date
            val dateText = getDateInShare(
                filteredContractionList.last().contractionTime,
                filteredContractionList.first().contractionTime
            )
            val date = Paragraph(dateText, dateFont).apply {
                alignment = Element.ALIGN_CENTER
            }
            document.add(date)


            val table = PdfPTable(3)
            table.totalWidth = 375f
            table.isLockedWidth = true

            table.horizontalAlignment = Element.ALIGN_CENTER

            filteredContractionList.reversed().forEachIndexed { index, contraction ->
                val contractionNumber = filteredContractionList.size - index

                val contractionCell = PdfPCell().apply {
                    addElement(
                        Paragraph(
                            "Contraction $contractionNumber",
                            regularFont
                        ).apply { font.color = headerColor })
                    addElement(
                        Paragraph(
                            convertSecondsToTimeString(contraction.lengthOfContraction),
                            contractionTimeFont
                        )
                    )
                    border = Rectangle.BOTTOM
                    borderColor = BaseColor(138, 138, 143)
                    //   paddingTop = 10f
                    paddingBottom = 10f
                }

                val timeCell = PdfPCell().apply {
                    // Set horizontal and vertical alignment
                    verticalAlignment = Element.ALIGN_MIDDLE
                    horizontalAlignment = Element.ALIGN_CENTER

                    addElement(
                        Paragraph(
                            convertCalendarToText(contraction.contractionTime),
                            regularFont
                        ).apply { alignment = Element.ALIGN_CENTER })
                    border = Rectangle.BOTTOM
                    borderColor = BaseColor(138, 138, 143)
                    // paddingTop = 10f
                    paddingBottom = 10f
                }

                val intervalCell = PdfPCell().apply {
                    horizontalAlignment = Element.ALIGN_RIGHT
                    addElement(Paragraph("Interval", regularFont).apply {
                        font.color = headerColor
                        alignment = Element.ALIGN_RIGHT
                    })
                    addElement(
                        Paragraph(
                            convertSecondsToTimeString(contraction.timeBetweenContractions),
                            contractionTimeFont
                        ).apply {
                            alignment = Element.ALIGN_RIGHT
                        })
                    border = Rectangle.BOTTOM
                    borderColor = BaseColor(138, 138, 143)
                    // paddingTop = 10f
                    paddingBottom = 10f
                }

                table.addCell(contractionCell)
                table.addCell(timeCell)
                table.addCell(intervalCell)
            }

            document.add(table)
            document.close()

            pdfFile
        }


    suspend fun sendEmailWithAttachment(file: File, context: Context) =
        withContext(Dispatchers.Main) {
            val uri: Uri =
                FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)

            val resources = context.resources
            val titleText = resources.getString(R.string.overview_of_contractions_title)
            val descriptionText = resources.getString(R.string.overview_of_contractions_text)

            val emailIntent = Intent(Intent.ACTION_SEND).apply {
                data = Uri.parse("mailto:") // Set data as empty email to open email composer
                putExtra(Intent.EXTRA_SUBJECT, titleText) // Set email subject
                putExtra(Intent.EXTRA_TEXT, descriptionText)
                putExtra(Intent.EXTRA_STREAM, uri)
            }
            emailIntent.type = "text/html"
            emailIntent.setType("message/rfc822") // Just email apps

            // Check if there are email apps available
            if (emailIntent.resolveActivity(context.packageManager) != null) {
                // Start email activity with chooser
                context.startActivity(Intent.createChooser(emailIntent, "Choose an email client"))
            } else {
                // No email apps available
                Toast.makeText(context, "No email apps found", Toast.LENGTH_SHORT).show()
            }
        }
}
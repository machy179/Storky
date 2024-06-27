package com.tappytaps.storky.repository

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.core.content.FileProvider
import com.itextpdf.html2pdf.HtmlConverter
import com.tappytaps.storky.model.Contraction
import com.tappytaps.storky.utils.convertCalendarToText
import com.tappytaps.storky.utils.convertSecondsToTimeString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class PdfRepository() {

    suspend fun generatePdfFromHtml(filteredContractionList: List<Contraction>, context: Context): File = withContext(
        Dispatchers.IO) {
        val htmlContent = """
    <html>
        <head>
            <style>
               .contraction-header {
                    font-family: 'Roboto', sans-serif; /* Specify fallback font */
                    color: #8A8A8F;
                    font-size: 13px;
                }
                .contraction-time {
                    font-family: 'Roboto', sans-serif; /* Specify fallback font */
                    color: #000000;
                    font-size: 17px;
                }
                .table-container {
                    display: flex;
                    justify-content: center;
                    align-items: center;
                    height: 100vh;
                }
                table {
                    width: 375px;
                    table-layout: fixed;
                    border-collapse: collapse;
                }
                tr { /* Add border to all rows, including the last child */
                    border-bottom: 1px solid #8A8A8F;
                }
                td {
                    padding: 10px;
                    width: 33%;
                }
                .right-align {
                    text-align: right;
                }
            </style>
        </head>
        <body>
            <div class="table-container">
                <table>
                    ${filteredContractionList.joinToString("") { contraction ->
            """
                        <tr>
                            <td style="vertical-align: top;">
                                <span class="contraction-header"><b>Contraction</b></span><br>
                                <span class="contraction-time">${convertSecondsToTimeString(contraction.lengthOfContraction)}</span>
                            </td>
                            <td style="text-align: center; vertical-align: middle;">
                                <span class="contraction-header">${convertCalendarToText(contraction.contractionTime)}</span>
                            </td>
                            <td class="right-align" style="vertical-align: top;">
                                <span class="contraction-header"><b>Interval</b></span><br>
                                <span class="contraction-time">${convertSecondsToTimeString(contraction.timeBetweenContractions)}</span>
                            </td>
                        </tr>
                        """
        }}
                </table>
            </div>
        </body>
    </html>
""".trimIndent()

        val pdfFile = File.createTempFile("contraction_report", ".pdf", context.cacheDir)
        FileOutputStream(pdfFile).use { outputStream ->
            HtmlConverter.convertToPdf(htmlContent, outputStream)
        }
        pdfFile
    }

    suspend fun sendEmailWithAttachment(file: File, context: Context) = withContext(Dispatchers.Main) {
        val uri: Uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)

        val emailIntent = Intent(Intent.ACTION_SEND).apply {
            data = Uri.parse("mailto:") // Set data as empty email to open email composer
            putExtra(Intent.EXTRA_SUBJECT, "Storky indicator contractions") // Set email subject
            putExtra(Intent.EXTRA_TEXT, "...performed by Storky app..")
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
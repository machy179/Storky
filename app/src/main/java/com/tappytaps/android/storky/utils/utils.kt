package com.tappytaps.android.storky.utils

import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tappytaps.android.storky.R
import com.tappytaps.android.storky.model.Contraction
import com.tappytaps.android.storky.service.PdfCreatorAndSender
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

fun isValidEmail(email: String): Boolean {
    val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()
    return emailRegex.matches(email)
}

@Composable
fun convertSecondsToStringResource(seconds: Int): String {
    val resourceString: String
    resourceString = when (seconds) {
        50 -> stringResource(R.string.seconds_50)
        60 -> stringResource(R.string.minute_1)
        70 -> stringResource(R.string.minute_110)
        80 -> stringResource(R.string.minute_120)

        240 -> stringResource(R.string.minutes_4)
        270 -> stringResource(R.string.minutes_430)
        300 -> stringResource(R.string.minutes_5)
        330 -> stringResource(R.string.minutes_530)
        360 -> stringResource(R.string.minutes_6)

        else -> stringResource(R.string.minute_1)

    }
    return resourceString
}

fun convertSecondsToTimeString(seconds: Int): String { // Convert seconds to a formatted time string
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return String.format("%01d:%02d", minutes, remainingSeconds)
}


fun convertSecondsToTimeString2(seconds: Int): String { // Convert seconds to a formatted time string in regarding minutes or second are 0
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60

    return when {
        minutes == 0 -> String.format("%d sec", remainingSeconds)
        remainingSeconds == 0 -> String.format("%d min", minutes)
        else -> String.format("%d min %d sec", minutes, remainingSeconds)
    }
}


fun convertCalendarToText(time: Calendar): String {
    val hours = time.get(Calendar.HOUR_OF_DAY)
    val minutes = time.get(Calendar.MINUTE)

    return String.format("%02d:%02d", hours, minutes)
}


@Composable
fun getFormattedDate(calendar: Calendar): String {
    val today = Calendar.getInstance() // Get today's calendar instance

    // Check if calendar represents today
    if (isSameDay(calendar, today)) {
        return stringResource(R.string.today) // Return "dnes" for today
    }

    // Check if calendar represents yesterday
    val yesterday = today.clone() as Calendar
    yesterday.add(Calendar.DAY_OF_MONTH, -1) // Subtract one day from today
    if (isSameDay(calendar, yesterday)) {
        return stringResource(R.string.yesterday) // Return "včera" for yesterday
    }

    // Otherwise, format date without year
    val dateFormatter = SimpleDateFormat("dd. MMMM", Locale.getDefault()) // Formatter without year
    return dateFormatter.format(calendar.time)
}


// Helper function to check if two calendars represent the same day
private fun isSameDay(calendar1: Calendar, calendar2: Calendar): Boolean {
    return calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) &&
            calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH) &&
            calendar1.get(Calendar.DAY_OF_MONTH) == calendar2.get(Calendar.DAY_OF_MONTH)
}

// Function for History screen
@Composable
fun getDateInHistory(calendarLastDay: Calendar, calendarFirstDay: Calendar): String {
    if (isSameDay(calendarLastDay, calendarFirstDay)) return getFormattedDate(calendarLastDay)
    else return (getFormattedDate(calendarLastDay) + " - " + getFormattedDate(calendarFirstDay))
}


fun getDateInShare(calendarLastDay: Calendar, calendarFirstDay: Calendar): String {
    val dateFormatter = SimpleDateFormat("dd. MMMM", Locale.getDefault()) // Formatter without year
    if (isSameDay(
            calendarLastDay,
            calendarFirstDay
        )
    ) return dateFormatter.format(calendarLastDay.time)
    else return (dateFormatter.format(calendarLastDay.time) + " - " + dateFormatter.format(
        calendarFirstDay.time
    ))
}

fun calculateAverageLengthOfContraction(
    listOfContractions: List<Contraction>,
    currentContractionLength: Int,
    includeCurrentContractionLength: Boolean = true,
): Int {
    val size =
        if (includeCurrentContractionLength) listOfContractions.size + 1 else listOfContractions.size
    //if one measurement, the average will be _currentContractionLength
    if (listOfContractions.size == 0) {
        return currentContractionLength

    } else if (listOfContractions.size > 0 && listOfContractions.size < 3) {
        var sumOfContractionLength = currentContractionLength
        listOfContractions.forEach { contraction ->
            sumOfContractionLength = sumOfContractionLength + contraction.lengthOfContraction
        }
        return sumOfContractionLength / size

    } else {
        var contractionLengths = listOfContractions.map { it.lengthOfContraction }
        contractionLengths =
            if (includeCurrentContractionLength) contractionLengths + currentContractionLength else contractionLengths

        // Sort contraction lengths in descending order:
        val sortedLengths = contractionLengths.sortedDescending()

        // Calculate the number of elements that will not be counted (20%):
        val excludedCount = (0.2 * sortedLengths.size).toInt()


        // To get the contraction lengths that will be counted:
        val includedLengths = sortedLengths.subList(0, sortedLengths.size - excludedCount)

        // Calculate the average of the included contraction lengths:
        val averageLength = includedLengths.sum() / includedLengths.size

        return averageLength

    }

}

fun calculateAverageLengthOfIntervalTime(
    listOfContractions: List<Contraction>,
): Int {
    val size = listOfContractions.size

    if (listOfContractions.size == 0) {
        return 0

    } else if (listOfContractions.size > 0 && listOfContractions.size < 3) {
        var sumOfLengthOfInterval = 0
        listOfContractions.forEach { contraction ->
            sumOfLengthOfInterval = sumOfLengthOfInterval + contraction.timeBetweenContractions
        }
        return sumOfLengthOfInterval / size

    } else {
        val intervalLengths = listOfContractions.map { it.timeBetweenContractions }

        // Arrange contraction lengths in descending order:
        val sortedLengths = intervalLengths.sortedDescending()

        // Count the number of elements that will not be counted (20%):
        val excludedCount = (0.2 * sortedLengths.size).toInt()


        // Get the contraction lengths to be counted:
        val includedLengths = sortedLengths.subList(excludedCount, sortedLengths.size)

        // Calculate the average of the included contraction lengths:
        val averageLength = includedLengths.sum() / includedLengths.size

        // Return the average:
        return averageLength

    }


}

fun checkStortkyNotificationAfter5Days(
    //checking, whether it si possible to set Storky notification "after 5 days"
    listOfContractions: List<Contraction>,
    currentContractionLength: Int,
): Boolean {

    Log.d("Checking Notification 5days", "1")
    if (currentContractionLength > 20) { //because current currentContractionLength is not in list
        val lastTwoContractions = listOfContractions
            .sortedByDescending { it.contractionTime }
            .take(2)
        Log.d(
            "Checking Notification 5days",
            "2 size lastThreeContractions= " + lastTwoContractions.size.toString()
        )

        val oneHourAgo = Calendar.getInstance().apply {
            add(Calendar.HOUR, -1)
        }

        if (lastTwoContractions.filter {
                it.contractionTime.after(oneHourAgo) //Contractions younger than 1 hour
                        && it.lengthOfContraction > 20
            } //and length of contraction more than 20
                .size > 1) {
            Log.d("Checking Notification 5days", "3 OK")
            return true
        } else return false
    } else return false


}

fun shareSetOfContractionsByEmail(
    //function for create pdf of list of contractions and send it by e-mail
    context: Context,
    contractionInSet: Int,
    listOfContractions: MutableStateFlow<List<Contraction>>,
    viewModel: ViewModel,
    pdfCreatorAndSender: PdfCreatorAndSender,
    currentContractionLength: Int? = null,
    currentTimeDateContraction: Calendar? = null,
) {

    val filteredContractionList: MutableList<Contraction> = listOfContractions
        .value
        .filter { it.in_set == contractionInSet }
        .toMutableList()

    // if there is stop watch running and it is saving of actual contractions, it is necessary to save current contraction
    if (currentTimeDateContraction != null && currentContractionLength != null && currentContractionLength != 0) {
        val newContraction = Contraction(
            contractionTime = currentTimeDateContraction,
            lengthOfContraction = currentContractionLength,
            timeBetweenContractions = 0
        )

        filteredContractionList.add(0, newContraction)
    }


    viewModel.viewModelScope.launch {
        val pdfFile = pdfCreatorAndSender.convertToPdf(
            filteredContractionList = filteredContractionList,
            context = context
        )
        pdfCreatorAndSender.sendEmailWithAttachment(
            file = pdfFile,
            context = context
        )
    }


}




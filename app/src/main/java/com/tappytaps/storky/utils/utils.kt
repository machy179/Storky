package com.tappytaps.storky.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.tappytaps.storky.R
import com.tappytaps.storky.model.Contraction
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

fun isValidEmail(email: String): Boolean {
    val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()
    return emailRegex.matches(email)
}

fun convertSecondsToTimeString(seconds: Int): String {
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return String.format("%01d:%02d", minutes, remainingSeconds)
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

fun convertSecondsToTimeString2(seconds: Int): String {
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return if (remainingSeconds == 0) {
        String.format("%d min", minutes)
    } else {
        String.format("%d min %d sec", minutes, remainingSeconds)
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


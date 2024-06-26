package com.tappytaps.storky.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.tappytaps.storky.R


// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily(Font(R.font.roboto)), // Replace with FontFamily.Roboto if you have it in your project
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
        textAlign = TextAlign.Center
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily(Font(R.font.roboto)), // Replace with FontFamily.Roboto if you have it in your project
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp,
        textAlign = TextAlign.Center
    ),
    bodySmall = TextStyle(
        fontFamily = FontFamily(Font(R.font.roboto)), // Replace with FontFamily.Roboto if you have it in your project
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp,
        textAlign = TextAlign.Center
    ),

    labelLarge = TextStyle(
        fontFamily = FontFamily(Font(R.font.roboto)), // Replace with FontFamily.Roboto if you have it in your project
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp,
        textAlign = TextAlign.Center
    ),
    labelMedium = TextStyle(
        fontFamily = FontFamily(Font(R.font.roboto)), // Replace with FontFamily.Roboto if you have it in your project
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp,
        textAlign = TextAlign.Center
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily(Font(R.font.roboto)), // Replace with FontFamily.Roboto if you have it in your project
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp,
        textAlign = TextAlign.Center
    ),



    titleLarge = TextStyle(
        fontFamily = FontFamily(Font(R.font.roboto)), // Replace with FontFamily.Roboto if you have it in your project
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        textAlign = TextAlign.Center
    ),
    titleMedium = TextStyle(
        fontFamily = FontFamily(Font(R.font.roboto)), // Replace with FontFamily.Roboto if you have it in your project
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp,
        textAlign = TextAlign.Center
    ),
    titleSmall = TextStyle(
        fontFamily = FontFamily(Font(R.font.roboto)), // Replace with FontFamily.Roboto if you have it in your project
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp,
        textAlign = TextAlign.Center
    ),

    headlineLarge =  TextStyle(
        fontFamily = FontFamily(Font(R.font.roboto)), // Replace with FontFamily.Roboto if you have it in your project
        fontWeight = FontWeight.Normal,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        textAlign = TextAlign.Center
    ),
    headlineMedium = TextStyle(
        fontFamily = FontFamily(Font(R.font.roboto)), // Replace with FontFamily.Roboto if you have it in your project
        fontWeight = FontWeight.Normal,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        textAlign = TextAlign.Center
    ),
    headlineSmall = TextStyle(
        fontFamily = FontFamily(Font(R.font.roboto)), // Replace with FontFamily.Roboto if you have it in your project
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        textAlign = TextAlign.Center
    ),


    displayLarge = TextStyle(
        fontFamily = FontFamily(Font(R.font.roboto)), // Replace with FontFamily.Roboto if you have it in your project
        fontWeight = FontWeight.Normal,
        fontSize = 57.sp,
        lineHeight = 64.sp,
        letterSpacing = -0.25.sp,
        textAlign = TextAlign.Center
    ),
    displayMedium = TextStyle(
        fontFamily = FontFamily(Font(R.font.roboto)), // Replace with FontFamily.Roboto if you have it in your project
        fontWeight = FontWeight.Normal,
        fontSize = 45.sp,
        lineHeight = 52.sp,
        textAlign = TextAlign.Center
    ),
    displaySmall = TextStyle(
        fontFamily = FontFamily(Font(R.font.roboto)), // Replace with FontFamily.Roboto if you have it in your project
        fontWeight = FontWeight.Normal,
        fontSize = 36.sp,
        lineHeight = 44.sp,
        textAlign = TextAlign.Center
    )



)
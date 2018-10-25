package com.matchfavorite.ai.sub3_matchfavorite.util

import android.view.View
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun parseDate(date: String?, time: String?): String? {
    val dateFormat = SimpleDateFormat("dd/MM/yy HH:mm:ssZ",
            Locale.getDefault())
    dateFormat.timeZone = TimeZone.getTimeZone("GMT")
    val parsedFormat = SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm", Locale.getDefault())
    try {
        return parsedFormat.format(dateFormat.parse("$date $time"))
    } catch (e: ParseException) {
        return null
    }
}

fun parseGoals(goalsResponse: String?): String?{
    return if (goalsResponse != null) goalsResponse.replace(":", ": ").replace(";", "\n") else null
}

fun parseSemicolons(response: String?): String?{
    return if (response != null) response.replace("; ", "\n") else null
}
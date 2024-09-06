package com.example.weatherapp.util

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

object DateUtil {
    fun String.toFormattedDate(): String {
        val inputDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputDateFormat = SimpleDateFormat("MMM d", Locale.getDefault())

        try {
            val date = inputDateFormat.parse(this)
            if (date != null) {
                return outputDateFormat.format(date)
            }
        } catch (e: Exception) {
            Log.e("error",e.toString())
        }
        return this
    }

    fun String.toFormattedDay(): String? {
        val dateComponents = this.split("-")
        return if (dateComponents.size == 3) {
            val year = dateComponents[0].toInt()
            val month = dateComponents[1].toInt() - 1
            val day = dateComponents[2].toInt()

            val calendar = Calendar.getInstance()
            calendar.set(year, month, day)
            val outputDateFormat = SimpleDateFormat("EE", Locale.getDefault())
            return outputDateFormat.format(calendar.time)
        } else null
    }
}
@RequiresApi(Build.VERSION_CODES.O)
fun getFormattedDate(): String {
    val date = LocalDate.now()
    val formatter = DateTimeFormatter.ofPattern("MMMM d") // Full month name and day of the month
    return date.format(formatter)
}

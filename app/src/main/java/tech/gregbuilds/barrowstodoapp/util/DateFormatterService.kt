package tech.gregbuilds.barrowstodoapp.util

import tech.gregbuilds.barrowstodoapp.common.Constants.DATE_PATTERN
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

interface DateFormatterService {
    fun getFormattedDate(date: Long?): String
    fun getDaysUntilDueDisplay(date: Long?): String
    fun getDaysUntilDue(date: Long?): Long
}

@Singleton
class DefaultDateFormatterService @Inject constructor() : DateFormatterService {

    override fun getFormattedDate(date: Long?): String {
        if (date == null) return ""
        val dateFormat = SimpleDateFormat(DATE_PATTERN, Locale.getDefault()) //extract this ofc to a dependency.
        return dateFormat.format(Date(date))
    }

    override fun getDaysUntilDueDisplay(date: Long?): String {
        if (date == null) return ""
        val today = Date()
        val dueDate = Date(date)
        val diff = dueDate.time - today.time
        val days = diff / (1000 * 60 * 60 * 24)
        return when {
            days < 0 -> "Overdue"
            days == 0L -> "Due Today"
            days == 1L -> "Due Tomorrow"
            else -> "Due in $days days"
        }
    }

    override fun getDaysUntilDue(date: Long?): Long {
        if (date == null) return Long.MAX_VALUE // Or another appropriate value for null dates
        val today = Date()
        val dueDate = Date(date)
        val diffInMillis = dueDate.time - today.time
        return TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS)
    }
}
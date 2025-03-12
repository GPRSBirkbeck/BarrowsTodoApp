package tech.gregbuilds.barrowstodoapp.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

interface DateFormatterService {
    fun formatDate(date: Long, pattern: String, locale: Locale = Locale.getDefault()): String
}

@Singleton
class DefaultDateFormatterService @Inject constructor() : DateFormatterService {
    override fun formatDate(date: Long, pattern: String, locale: Locale): String {
        val dateFormat = SimpleDateFormat(pattern, locale)
        return dateFormat.format(Date(date))
    }
}
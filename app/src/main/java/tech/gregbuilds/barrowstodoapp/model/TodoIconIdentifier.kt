package tech.gregbuilds.barrowstodoapp.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Work
import androidx.compose.ui.graphics.vector.ImageVector

enum class TodoIconIdentifier(val icon: ImageVector) {
    Calendar(Icons.Filled.CalendarMonth),
    Cart(Icons.Filled.ShoppingCart),
    Alarm(Icons.Filled.Alarm),
    Book(Icons.Filled.Book),
    Home(Icons.Filled.Home),
    Work(Icons.Filled.Work);

    companion object {
        fun fromIdentifier(identifier: String): TodoIconIdentifier {
            return when (identifier) {
                Calendar.name -> Calendar
                Alarm.name -> Alarm
                Book.name -> Book
                Cart.name -> Cart
                Home.name -> Home
                Work.name -> Work
                else -> Alarm // Default to Alarm if the identifier is not recognized
            }
        }
    }
}
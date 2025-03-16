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
    Alarm(Icons.Filled.Alarm),
    Book(Icons.Filled.Book),
    Calendar(Icons.Filled.CalendarMonth),
    Cart(Icons.Filled.ShoppingCart),
    Home(Icons.Filled.Home),
    Work(Icons.Filled.Work);

    companion object {
        fun fromImageVector(imageVector: ImageVector): TodoIconIdentifier {
            return when (imageVector) {
                Icons.Filled.Alarm -> Alarm
                Icons.Filled.Book -> Book
                Icons.Filled.CalendarMonth -> Calendar
                Icons.Filled.ShoppingCart -> Cart
                Icons.Filled.Home -> Home
                Icons.Filled.Work -> Work
                else -> Alarm // Default to Alarm if the icon is not recognized
            }
        }

        fun fromIdentifier(identifier: String): TodoIconIdentifier {
            return when (identifier) {
                Alarm.name -> Alarm
                Book.name -> Book
                Calendar.name -> Calendar
                Cart.name -> Cart
                Home.name -> Home
                Work.name -> Work
                else -> Alarm // Default to Alarm if the identifier is not recognized
            }
        }
    }
}
package tech.gregbuilds.barrowstodoapp.ui.details.state

sealed class NavigationEvent {
    data object NavigateBack : NavigationEvent()
}
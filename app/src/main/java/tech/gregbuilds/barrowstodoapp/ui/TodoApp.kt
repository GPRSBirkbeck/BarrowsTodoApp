package tech.gregbuilds.barrowstodoapp.ui

import androidx.compose.runtime.Composable
import tech.gregbuilds.barrowstodoapp.navigation.AppNavHost
import tech.gregbuilds.barrowstodoapp.theme.GregorySmithTemplateTheme

@Composable
fun TodoApp() {
    GregorySmithTemplateTheme {
        AppNavHost()
    }
}
package tech.gregbuilds.barrowstodoapp.ui.listScreen.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import tech.gregbuilds.barrowstodoapp.ui.listScreen.viewModel.TodoViewModel

@Composable
fun TodoListScreen(
    viewModel: TodoViewModel,
    modifier: Modifier = Modifier
) {
    // TODO: migrate to constraintLayouts
    val todoItems = viewModel.todoItems.collectAsState()
    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        todoItems.value.forEach {
            //TODO have a nice composable for Todo Items
            // pass each item events - think about which you want. In the next commit, think about these.
            item {
                Text("Title: ${it.title}")
            }

            //TODO think about adding pagination with the Paging library.
        }

        //TODO Add a button to add an item.
    }
}
package tech.gregbuilds.barrowstodoapp.ui.list.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import tech.gregbuilds.barrowstodoapp.ui.list.state.TodoListUiState
import tech.gregbuilds.barrowstodoapp.ui.list.viewModel.TodoListViewModel

@Composable
fun TodoListScreen(
    onAddClicked: () -> Unit,
    onItemClicked: (Int) -> Unit,
    viewModel: TodoListViewModel,
    modifier: Modifier = Modifier
) {
    Scaffold { innerPadding ->
        val uiState by viewModel.uiState.collectAsState()
        // TODO: migrate to constraintLayouts
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (uiState) {
                is TodoListUiState.Loading -> {
                    // Show a loading indicator
                    item {
                        CircularProgressIndicator()
                    }
                }

                is TodoListUiState.Success -> {
                    // Show the list of items
                    val listItems = (uiState as TodoListUiState.Success).listItems
                    listItems.forEach {
                        item {
                            Text(
                                text = "Title: ${it.title}",
                                modifier = Modifier.padding(16.dp)
                            )
                            Button(
                                onClick = { onItemClicked(it.id) },
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text("View")
                            }
                            //TODO have a nice composable for Todo Items
                            //TodoItemRow(item)
                        }

                        //TODO think about adding pagination with the Paging library.
                    }
                }

                is TodoListUiState.Failed -> {
                    // Show an error message
                    val errorMessage = (uiState as TodoListUiState.Failed).errorMessage
                    item {
                        Text(
                            text = "Error: $errorMessage",
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }

                is TodoListUiState.Empty -> {
                    // Show an empty message
                    item {
                        Text(
                            text = "No items found",
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
            item {
                Button(
                    onClick = { onAddClicked() },
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text("Create a To-do item")
                }
            }
        }
    }
}
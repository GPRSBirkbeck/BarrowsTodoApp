package tech.gregbuilds.barrowstodoapp.ui.list.screen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import tech.gregbuilds.barrowstodoapp.R
import tech.gregbuilds.barrowstodoapp.ui.composables.SwipeToDismissListItem
import tech.gregbuilds.barrowstodoapp.ui.list.state.TodoListUiState
import tech.gregbuilds.barrowstodoapp.ui.list.viewModel.TodoListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoListScreen(
    onAddClicked: () -> Unit,
    onItemClicked: (Int) -> Unit,
    viewModel: TodoListViewModel,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Your to-dos",
                        color = Color.White,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { innerPadding ->
        val uiState by viewModel.uiState.collectAsState()

        val lifecycleOwner = LocalLifecycleOwner.current
        LaunchedEffect(key1 = lifecycleOwner) {
            val observer = LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_RESUME) {
                    Log.d("TodoListScreen", "ON_RESUME event triggered")
                    viewModel.onScreenResumed()
                }
            }
            lifecycleOwner.lifecycle.addObserver(observer)

        }

        Box(modifier = Modifier.fillMaxSize()) {
            // Background Image
            Image(
                painter = painterResource(id = R.drawable.list_background),
                contentDescription = "Background Image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // TODO: migrate to constraintLayouts
            ConstraintLayout(
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                val (lazyColumn, button, loadingIndicator) = createRefs()

                when (uiState) {
                    is TodoListUiState.Loading -> {
                        // Show a loading indicator
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .constrainAs(loadingIndicator) {
                                    top.linkTo(parent.top)
                                    bottom.linkTo(parent.bottom)
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                },
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    is TodoListUiState.Success -> {
                        // Show the list of items
                        val listItems = (uiState as TodoListUiState.Success).listItems
                        LazyColumn(
                            modifier = Modifier
                                .padding(vertical = 12.dp)
                                .constrainAs(lazyColumn) {
                                    top.linkTo(parent.top)
                                    bottom.linkTo(button.top)
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                    height =
                                        Dimension.fillToConstraints
                                }
                                .fillMaxSize()
                        ) {
                            listItems.forEach {
                                item {
                                    SwipeToDismissListItem(
                                        onRemove = { id -> viewModel.swipeToDeleteItem(id) },
                                        item = it,
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                                        onClick = { onItemClicked(it.id) }
                                    )
                                }
                            }
                            //TODO think about adding pagination with the Paging library.
                        }
                    }

                    is TodoListUiState.Failed -> {
                        // Show an error message
                        val errorMessage = (uiState as TodoListUiState.Failed).errorMessage
                        Text(
                            text = "Error: $errorMessage",
                            modifier = Modifier.padding(16.dp)
                        )
                    }

                    is TodoListUiState.Empty -> {
                        // Show an empty message
                        Text(
                            text = "No items found",
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
                Button(
                    onClick = { onAddClicked() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 8.0.dp,
                        pressedElevation = 12.0.dp,
                        focusedElevation = 10.0.dp,
                        hoveredElevation = 1.0.dp,
                        disabledElevation = 0.0.dp
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .constrainAs(button) {
                            bottom.linkTo(parent.bottom, margin = 16.dp)
                            end.linkTo(parent.end, margin = 16.dp)
                        }
                ) {
                    Text("Create a To-do item")
                }
            }
        }
    }
}
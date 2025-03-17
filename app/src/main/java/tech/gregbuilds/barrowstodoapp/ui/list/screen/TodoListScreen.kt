package tech.gregbuilds.barrowstodoapp.ui.list.screen

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import kotlinx.coroutines.launch
import tech.gregbuilds.barrowstodoapp.R
import tech.gregbuilds.barrowstodoapp.ui.composables.SwipeToDismissListItem
import tech.gregbuilds.barrowstodoapp.ui.list.state.SortType
import tech.gregbuilds.barrowstodoapp.ui.list.state.TodoListUiState
import tech.gregbuilds.barrowstodoapp.ui.list.viewModel.TodoListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoListScreen(
    viewModel: TodoListViewModel,
    modifier: Modifier = Modifier,
    onAddClicked: () -> Unit,
    onItemClicked: (Int) -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val uiState by viewModel.uiState.collectAsState()
    val searchText by viewModel.searchQuery.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var hasNotificationPermission by remember { mutableStateOf(false) }

    // Check if the app has notification permission
    fun checkNotificationPermission() {
        hasNotificationPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    // Request notification permission
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        hasNotificationPermission = isGranted
        if (isGranted) {
            scope.launch {
                snackbarHostState.showSnackbar("Notification permission granted")
            }
        } else {
            scope.launch {
                snackbarHostState.showSnackbar("Notification permission denied")
            }
        }
    }

    // Check permission on launch
    LaunchedEffect(key1 = Unit) {
        checkNotificationPermission()
    }

    // Request permission if needed
    LaunchedEffect(key1 = hasNotificationPermission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !hasNotificationPermission) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Your to-dos",
                        textAlign = TextAlign.Start,
                        modifier = Modifier.fillMaxWidth(),
                        color = if (isSystemInDarkTheme()) Color.Black else Color.White
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
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

            ConstraintLayout(
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                val (searchBar, itemList, addButton, addTestButton, showLoading, noItemsCard) = createRefs()

                when (uiState) {
                    is TodoListUiState.Success,
                    TodoListUiState.None -> {
                        Card(
                            shape = RoundedCornerShape(8.dp),
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 8.dp
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                                .constrainAs(searchBar) {
                                    top.linkTo(parent.top)
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                },
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSystemInDarkTheme()) Color.Black else Color.White,
                            )
                        ) {
                            ConstraintLayout(
                                modifier = Modifier
                                    .wrapContentHeight()
                                    .fillMaxWidth()
                            ) {
                                val (searchIcon, searchTextField, reorderIcon, reorderText) = createRefs()

                                Icon(
                                    contentDescription = "search",
                                    imageVector = Icons.Default.Search,
                                    tint = MaterialTheme.colorScheme.tertiary,
                                    modifier = Modifier
                                        .padding(8.dp)
                                        .constrainAs(searchIcon) {
                                            start.linkTo(parent.start)
                                            end.linkTo(searchTextField.start)
                                            top.linkTo(searchTextField.top)
                                            bottom.linkTo(searchTextField.bottom)
                                        }
                                )
                                OutlinedTextField(
                                    value = searchText,
                                    onValueChange = { viewModel.updateSearchText(it) },
                                    placeholder = { Text("Search your to-dos") },
                                    modifier = Modifier
                                        .padding(8.dp)
                                        .constrainAs(searchTextField) {
                                            top.linkTo(parent.top)
                                            bottom.linkTo(reorderText.top)
                                            end.linkTo(parent.end, margin = 8.dp)
                                            start.linkTo(searchIcon.end, margin = 8.dp)
                                            width = Dimension.fillToConstraints
                                        }
                                )
                                val icon = when (viewModel.sortType.collectAsState().value) {
                                    SortType.NONE -> Icons.Default.Menu
                                    else -> Icons.Default.FilterList
                                }
                                Icon(
                                    tint = MaterialTheme.colorScheme.secondary,
                                    imageVector = icon,
                                    contentDescription = "menu",
                                    modifier = Modifier
                                        .padding(8.dp)
                                        .constrainAs(reorderIcon) {
                                            start.linkTo(searchIcon.start)
                                            top.linkTo(reorderText.top)
                                            bottom.linkTo(reorderText.bottom)
                                        }
                                        .clickable {
                                            viewModel.cycleSortType()
                                        }
                                        .rotate(if (viewModel.sortType.collectAsState().value == SortType.ASCENDING) 180f else 0f)
                                )
                                Text(
                                    text = when (viewModel.sortType.collectAsState().value) {
                                        SortType.ASCENDING -> "Ascending order of word frequency"
                                        SortType.DESCENDING -> "Descending order of word frequency"
                                        else -> "No sorting - just searching"
                                    },
                                    textAlign = TextAlign.Start,
                                    color = MaterialTheme.colorScheme.secondary,
                                    modifier = Modifier
                                        .padding(8.dp)
                                        .constrainAs(reorderText) {
                                            start.linkTo(searchTextField.start)
                                            top.linkTo(searchTextField.bottom)
                                            bottom.linkTo(parent.bottom)
                                        }
                                        .clickable {
                                            viewModel.cycleSortType()
                                        }
                                )
                            }
                        }
                    }

                    else -> {}
                }

                when (uiState) {
                    is TodoListUiState.Loading -> {
                        // Show a loading indicator
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .constrainAs(showLoading) {
                                    end.linkTo(parent.end)
                                    top.linkTo(parent.top)
                                    start.linkTo(parent.start)
                                    bottom.linkTo(parent.bottom)
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
                                .constrainAs(itemList) {
                                    end.linkTo(parent.end)
                                    start.linkTo(parent.start)
                                    bottom.linkTo(addButton.top)
                                    top.linkTo(searchBar.bottom)
                                    height = Dimension.fillToConstraints
                                }
                                .fillMaxSize()
                        ) {
                            items(listItems, key = { listItem -> listItem.id }) {
                                Row(Modifier.animateItem()) {
                                    SwipeToDismissListItem(
                                        item = it,
                                        modifier = Modifier.padding(
                                            horizontal = 12.dp,
                                            vertical = 4.dp
                                        ),
                                        onClick = { onItemClicked(it.id) },
                                        onRemove = { id -> viewModel.swipeToDeleteItem(id) }
                                    )
                                }
                            }
                            // In a production app I would handle pagination here.
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
                        Card(
                            modifier = Modifier
                                .wrapContentSize()
                                .padding(16.dp)
                                .constrainAs(noItemsCard) {
                                    top.linkTo(parent.top)
                                    bottom.linkTo(addButton.top)
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                },
                            shape = RoundedCornerShape(8.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSystemInDarkTheme()) Color.Black else Color.White,
                            ),
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 8.dp
                            )
                        ) {
                            Text(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth(),
                                textAlign = TextAlign.Center,
                                text = "No to-dos yet - create one below!",
                                color = if (isSystemInDarkTheme()) Color.White else Color.Black,
                            )
                        }
                    }

                    TodoListUiState.None -> {
                        Card(
                            modifier = Modifier
                                .wrapContentSize()
                                .padding(16.dp)
                                .constrainAs(noItemsCard) {
                                    top.linkTo(parent.top)
                                    bottom.linkTo(addButton.top)
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                },
                            shape = RoundedCornerShape(8.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSystemInDarkTheme()) Color.Black else Color.White,
                            ),
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 8.dp
                            )
                        ) {
                            Text(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth(),
                                textAlign = TextAlign.Center,
                                text = "No to-dos found with your filter",
                                color = if (isSystemInDarkTheme()) Color.White else Color.Black,
                            )
                        }
                    }
                }
                if (uiState == TodoListUiState.Empty) {
                    Button(
                        shape = RoundedCornerShape(8.dp),
                        onClick = { viewModel.addTestData() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isSystemInDarkTheme()) Color.Black else Color.White,
                            contentColor = MaterialTheme.colorScheme.secondary
                        ),
                        elevation = ButtonDefaults.buttonElevation(
                            hoveredElevation = 1.0.dp,
                            defaultElevation = 8.0.dp,
                            pressedElevation = 12.0.dp,
                            focusedElevation = 10.0.dp,
                            disabledElevation = 0.0.dp
                        ),
                        modifier = Modifier
                            .constrainAs(addTestButton) {
                                bottom.linkTo(parent.bottom, margin = 8.dp)
                                start.linkTo(parent.start, margin = 16.dp)
                            }
                    ) {
                        Text("Populate with test data")
                    }
                }
                Button(
                    onClick = { onAddClicked() },
                    shape = RoundedCornerShape(8.dp),
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
                    modifier = Modifier
                        .constrainAs(addButton) {
                            bottom.linkTo(parent.bottom, margin = 8.dp)
                            end.linkTo(parent.end, margin = 16.dp)
                        }
                ) {
                    Text("Add a to-do")
                }
            }
        }
    }
}
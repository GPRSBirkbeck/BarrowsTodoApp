package tech.gregbuilds.barrowstodoapp.ui.details.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import kotlinx.coroutines.flow.collectLatest
import tech.gregbuilds.barrowstodoapp.R
import tech.gregbuilds.barrowstodoapp.model.TodoIconIdentifier
import tech.gregbuilds.barrowstodoapp.ui.details.state.NavigationEvent
import tech.gregbuilds.barrowstodoapp.ui.details.state.TodoDetailsUiState
import tech.gregbuilds.barrowstodoapp.ui.details.viewModel.TodoDetailViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun TodoDetailScreen(
    viewModel: TodoDetailViewModel,
    todoId: Int?,
    goBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Load the TodoItem when the screen is launched
    LaunchedEffect(key1 = todoId) {
        viewModel.loadTodoItem(todoId)
    }

    // Collect the UI state from the ViewModel
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(key1 = Unit) {
        viewModel.navigationEvents.collectLatest { event ->
            when (event) {
                is NavigationEvent.NavigateBack -> {
                    goBack()
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.app_name),
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                navigationIcon = {
                    IconButton(onClick = goBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            val (lazyColumn, saveButton, cancelButton, loadingIndicator) = createRefs()

            // Handle different UI states
            when (uiState) {
                is TodoDetailsUiState.Loading -> {
                    // Show loading indicator
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

                is TodoDetailsUiState.Success -> {
                    val title = viewModel.title.collectAsState()
                    val body = viewModel.body.collectAsState()
                    val selectedIcon = viewModel.selectedIcon.collectAsState()
                    var showDatePicker by remember { mutableStateOf(false) }
                    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = viewModel.selectedDate.collectAsState().value)
                    LazyColumn(
                        modifier = Modifier
                            .constrainAs(lazyColumn) {
                                top.linkTo(parent.top)
                                bottom.linkTo(saveButton.top)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                                height = Dimension.fillToConstraints
                            }
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        item {
                            OutlinedTextField(
                                value = title.value,
                                onValueChange = {
                                    viewModel.updateTitle(it)
                                },
                                label = { Text("Title") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            )
                        }
                        item {
                            OutlinedTextField(
                                value = body.value,
                                onValueChange = {
                                    viewModel.updateBody(it)
                                },
                                label = { Text("Body") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            )
                        }
                        item {
                            Button(onClick = { showDatePicker = true }) {
                                Text(viewModel.dueDateString.collectAsState().value.ifEmpty { "Select Due Date" })
                            }
                            if (showDatePicker) {
                                DatePickerDialog(
                                    onDismissRequest = { showDatePicker = false },
                                    confirmButton = {
                                        TextButton(onClick = {
                                            showDatePicker = false
                                            viewModel.updateSelectedDate(datePickerState.selectedDateMillis)
                                        }) {
                                            Text("OK")
                                        }
                                    },
                                    dismissButton = {
                                        TextButton(onClick = { showDatePicker = false }) {
                                            Text("Cancel")
                                        }
                                    }
                                ) {
                                    DatePicker(state = datePickerState)
                                }
                            }
                        }
                        item {
                            val icons = listOf(
                                TodoIconIdentifier.Alarm,
                                TodoIconIdentifier.Book,
                                TodoIconIdentifier.Calendar,
                                TodoIconIdentifier.Cart,
                                TodoIconIdentifier.Home,
                                TodoIconIdentifier.Work
                            )
                            FlowRow(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                icons.forEach { icon ->
                                    Icon(
                                        imageVector = icon.icon,
                                        contentDescription = icon.name,
                                        tint = if (icon == selectedIcon.value) Color.Blue else Color.Gray,
                                        modifier = Modifier
                                            .padding(8.dp)
                                            .size(48.dp)
                                            .clickable {
                                                viewModel.updateSelectedIcon(icon)
                                            }
                                    )
                                }
                            }
                        }
                    }
                    Button(
                        onClick = {
                            viewModel.saveTodoItem()
                        },
                        enabled = viewModel.isSaveEnabled.collectAsState().value,
                        modifier = Modifier
                            .constrainAs(saveButton) {
                                bottom.linkTo(cancelButton.top, margin = 8.dp)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            }
                    ) {
                        Text("Save")
                    }
                    Button(
                        onClick = goBack,
                        modifier = Modifier
                            .constrainAs(cancelButton) {
                                bottom.linkTo(parent.bottom, margin = 16.dp)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            }
                    ) {
                        Text("Cancel")
                    }
                }

                is TodoDetailsUiState.Failed -> {
                    Text(text = (uiState as TodoDetailsUiState.Failed).errorMessage)
                }

                is TodoDetailsUiState.Empty -> {
                    Text(text = "No item found")
                }
            }
        }
    }
}

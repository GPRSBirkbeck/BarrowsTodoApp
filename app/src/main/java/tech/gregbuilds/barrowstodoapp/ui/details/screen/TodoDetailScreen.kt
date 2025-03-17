package tech.gregbuilds.barrowstodoapp.ui.details.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SelectableDates
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
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
    todoId: Int?,
    viewModel: TodoDetailViewModel,
    goBack: () -> Unit
) {
    // Load the TodoItem when the screen is launched
    LaunchedEffect(key1 = todoId) {
        viewModel.loadTodoItem(todoId)
    }
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
        modifier = Modifier.background(color = Color(0xFFECEFF1)),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Create a new to-do",
                        color = if (isSystemInDarkTheme()) Color.Black else Color.White,
                        textAlign = TextAlign.Start,
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
                            tint = if (isSystemInDarkTheme()) Color.Black else Color.White
                        )
                    }
                }
            )
        }
    ) { innerPadding ->

        Box(modifier = Modifier.fillMaxSize()) {
            // Background Image
            Image(
                painter = painterResource(id = R.drawable.list_background),
                contentDescription = "Background Image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                val (lazyColumn, saveButton, cancelButton, deleteButton, loadingIndicator) = createRefs()

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
                        val buttonElevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 8.0.dp,
                            pressedElevation = 12.0.dp,
                            focusedElevation = 10.0.dp,
                            hoveredElevation = 1.0.dp,
                            disabledElevation = 0.0.dp
                        )

                        val datePickerState =
                            rememberDatePickerState(
                                initialSelectedDateMillis = viewModel.selectedDate.collectAsState().value,
                                selectableDates = object :
                                    SelectableDates {
                                    override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                                        return utcTimeMillis >= System.currentTimeMillis()
                                    }
                                }
                            )
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
                                // Details Card
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (isSystemInDarkTheme()) Color.Black else Color.White
                                    ),
                                    elevation = CardDefaults.cardElevation(
                                        defaultElevation = 8.dp
                                    )
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp)
                                    ) {
                                        Text(
                                            text = "Let's start with some details",
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(bottom = 8.dp)
                                        )
                                        OutlinedTextField(
                                            value = title.value,
                                            onValueChange = { viewModel.updateTitle(it) },
                                            label = { Text("Title") },
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(bottom = 8.dp)
                                        )
                                        OutlinedTextField(
                                            value = body.value,
                                            onValueChange = { viewModel.updateBody(it) },
                                            label = { Text("Body") },
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                    }
                                }
                            }
                            item {
                                // Due Date Card
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (isSystemInDarkTheme()) Color.Black else Color.White
                                    ),
                                    elevation = CardDefaults.cardElevation(
                                        defaultElevation = 8.dp
                                    )
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp)
                                    ) {
                                        Text(
                                            text = "When's this due?",
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(bottom = 8.dp)
                                        )
                                        Button(
                                            onClick = { showDatePicker = !showDatePicker },
                                            elevation = buttonElevation,
                                            shape = RoundedCornerShape(8.dp),
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = MaterialTheme.colorScheme.secondary
                                            ),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Text(text = "Select Due Date")
                                        }
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
                            }
                            item {
                                // Icon Card
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (isSystemInDarkTheme()) Color.Black else  Color.White
                                    ),
                                    elevation = CardDefaults.cardElevation(
                                        defaultElevation = 8.dp
                                    )
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp)
                                    ) {
                                        Text(
                                            text = "Select Icon",
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(bottom = 8.dp)
                                        )
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
                                                    tint = if (icon == selectedIcon.value) MaterialTheme.colorScheme.secondary else Color.Gray,
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
                            }
                        }

                        Button(
                            onClick = {
                                viewModel.saveTodoItem()
                            },
                            colors = ButtonDefaults.buttonColors(
                                disabledContainerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f),
                                containerColor = MaterialTheme.colorScheme.secondary,
                                disabledContentColor = if (isSystemInDarkTheme()) Color.Black else Color.White
                            ),
                            shape = RoundedCornerShape(8.dp),
                            enabled = viewModel.isSaveEnabled.collectAsState().value,
                            modifier = Modifier
                                .constrainAs(saveButton) {
                                    if (viewModel.isExistingTodo) bottom.linkTo(deleteButton.top, margin = 8.dp)
                                    else bottom.linkTo(parent.bottom, margin = 16.dp)
                                    end.linkTo(parent.end, margin = 16.dp)
                                },
                            elevation = buttonElevation,
                        ) {
                            Text("Save")
                        }
                        Button(
                            onClick = goBack,
                            elevation = buttonElevation,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.secondary
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .constrainAs(cancelButton) {
                                    if (viewModel.isExistingTodo) bottom.linkTo(deleteButton.top, margin = 8.dp)
                                    else bottom.linkTo(parent.bottom, margin = 16.dp)
                                    start.linkTo(parent.start, margin = 16.dp)
                                }
                        ) {
                            Text("Cancel")
                        }

                        if (viewModel.isExistingTodo) {
                            Button(
                                onClick = {
                                    viewModel.deleteTodoItem()
                                },
                                elevation = buttonElevation,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.error
                                ),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .constrainAs(deleteButton) {
                                        start.linkTo(parent.start, margin = 16.dp)
                                        bottom.linkTo(parent.bottom, margin = 16.dp)
                                        end.linkTo(parent.end, margin = 16.dp)
                                    }
                            ) {
                                Text("Delete")
                            }
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
}

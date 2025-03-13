package tech.gregbuilds.barrowstodoapp.ui.details.viewModel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import tech.gregbuilds.barrowstodoapp.data.repositories.TodoRepositoryImpl
import javax.inject.Inject

@HiltViewModel
class TodoDetailViewModel @Inject constructor(
    private val todoRepository: TodoRepositoryImpl
) : ViewModel() {

}
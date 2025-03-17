package tech.gregbuilds.barrowstodoapp.ui.composables

import android.widget.Toast
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import tech.gregbuilds.barrowstodoapp.model.TodoItem
import tech.gregbuilds.barrowstodoapp.ui.details.TodoItemRow

@Composable
fun SwipeToDismissListItem(
    item: TodoItem,
    modifier: Modifier,
    onClick: () -> Unit = {},
    onRemove: (Int) -> Unit
) {
    val context = LocalContext.current
    val currentItem by rememberUpdatedState(item)
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = {
            when (it) {
                SwipeToDismissBoxValue.EndToStart -> {
                    onRemove(currentItem.id)
                    Toast.makeText(context, "Item deleted", Toast.LENGTH_SHORT).show()
                }
                SwipeToDismissBoxValue.Settled -> return@rememberSwipeToDismissBoxState false
                else -> return@rememberSwipeToDismissBoxState false
            }
            return@rememberSwipeToDismissBoxState false
        },
        // positional threshold of 25%
        positionalThreshold = { it * .25f }
    )
    SwipeToDismissBox(
        state = dismissState,
        backgroundContent = { DismissBackground(dismissState) },
        content = {
            TodoItemRow(
                item,
                modifier,
                onClick
            )
        }
    )
}
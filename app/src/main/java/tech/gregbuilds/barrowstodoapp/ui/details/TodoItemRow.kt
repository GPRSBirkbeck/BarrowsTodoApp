package tech.gregbuilds.barrowstodoapp.ui.details

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import tech.gregbuilds.barrowstodoapp.model.TodoIconIdentifier
import tech.gregbuilds.barrowstodoapp.model.TodoItem

@Composable
fun TodoItemRow(
    item: TodoItem,
    modifier: Modifier,
    onClick: () -> Unit = {}
) {
    ConstraintLayout(
        modifier = modifier.fillMaxWidth().clickable { onClick.invoke() }
    ) {
        val (icon, title, body, dueDate, daysUntilDue) = createRefs()
        createVerticalChain(title, body, dueDate, chainStyle = androidx.constraintlayout.compose.ChainStyle.Packed)

        val overdueColor = if (item.daysUntilDue < 0) Color.Red else Color.Unspecified

        Icon(
            tint = overdueColor,
            imageVector = TodoIconIdentifier.fromIdentifier(item.iconIdentifier).icon,
            contentDescription = "Todo Item Icon",
            modifier = Modifier
                .size(32.dp)
                .constrainAs(icon) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                }
        )

        Text(
            text = item.title,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .constrainAs(title) {
                    top.linkTo(parent.top)
                    start.linkTo(icon.end, margin = 8.dp)
                    end.linkTo(daysUntilDue.start)
                    width = Dimension.fillToConstraints
                }
        )

        Text(
            text = item.body,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .constrainAs(body) {
                    start.linkTo(title.start)
                    end.linkTo(daysUntilDue.start)
                    width = Dimension.fillToConstraints
                }
        )

        Text(
            color = overdueColor,
            text = item.dueDateString,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier
                .constrainAs(dueDate) {
                    start.linkTo(title.start)
                    end.linkTo(daysUntilDue.start)
                    width = Dimension.fillToConstraints
                }
        )

        Text(
            color = overdueColor,
            text = item.daysUntilDueDisplay,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .constrainAs(daysUntilDue) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end)
                }
        )
    }
}
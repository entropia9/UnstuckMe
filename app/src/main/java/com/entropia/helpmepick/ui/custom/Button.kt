import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.findRootCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CustomButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = ButtonDefaults.shape,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    elevation: Dp = 0.dp,
    border: BorderStroke? = null,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    text: @Composable BoxScope.() -> Unit,
    dropDownMenus: @Composable ColumnScope.(() -> Unit) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    var touchPoint: Offset by remember { mutableStateOf(Offset.Zero) }
    val density = LocalDensity.current
    val containerColor =
        if (enabled) colors.containerColor else colors.disabledContainerColor
    val contentColor =
        if (enabled) colors.contentColor else colors.disabledContentColor
    Surface(
        modifier = modifier
            .onGloballyPositioned { coordinates ->
                touchPoint = coordinates
                    .findRootCoordinates()
                    .positionInRoot()
            }
            .combinedClickable(
                onClick = onClick,
                onLongClick = {
                    expanded = true
                },
                enabled = enabled,
                role = Role.Button,
            ),
        shape = shape,
        color = containerColor,
        contentColor = contentColor,
        shadowElevation = elevation,
        border = border
    ) {
        val mergedStyle = LocalTextStyle.current.merge(MaterialTheme.typography.labelLarge)
        CompositionLocalProvider(
            LocalContentColor provides contentColor, LocalTextStyle provides mergedStyle
        ) {
            Row(
                Modifier
                    .defaultMinSize(
                        minWidth = ButtonDefaults.MinWidth, minHeight = ButtonDefaults.MinHeight
                    )
                    .padding(contentPadding),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box() {
                    text()
                    val (xDp, yDp) = with(density) {
                        (touchPoint.x.toDp()) to (touchPoint.y.toDp())
                    }
                    DropdownMenu(
                        expanded = expanded,
                        offset = DpOffset(xDp, yDp),
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.align(Alignment.Center)
                    ) {
                        dropDownMenus { expanded = false }
                    }
                }
            }
        }
    }
}
package ui.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

fun <T> LazyListScope.ListToGridItems(
    data: List<T>,
    columns: Int,
    itemContent: @Composable BoxScope.(index: Int, item: T) -> Unit
) {
    val rows = (data.size + columns - 1) / columns
    val groups = mutableListOf<MutableList<T>>()
    for (row in 0 until rows) {
        val group = mutableListOf<T>()
        for (column in 0 until columns) {
            val originIndex = row * columns + column
            if (originIndex < data.size) {
                group.add(data[originIndex])
            }
        }
        groups.add(group)
    }
    items(groups.size) {
        val group = groups[it]
        Row(verticalAlignment = Alignment.CenterVertically) {
            group.forEachIndexed { index, item ->
                val originIndex = index + it * columns
                Box(modifier = Modifier.weight(1f)) {
                    itemContent(originIndex, item)
                }
            }
            if (it == rows - 1 && group.size < columns) {
                Spacer(modifier = Modifier.weight(columns - group.size.toFloat()))
            }
        }
    }
}
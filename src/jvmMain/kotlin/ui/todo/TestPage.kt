package ui.todo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ui.common.ExpandableText
import ui.common.ListToGridItems

@Composable
fun TestPage() {

//    Box(Modifier.padding(top = 50.dp)) {
//        ExpandableText(modifier = Modifier.width(200.dp).background(Color.Red), text = "1失恋的人建议不要去听2失恋的人建议不要去听3失恋的人建议不要去听4失恋的人建议不要去听")
//    }
    val list = remember {
        val list = mutableListOf<String>()
//        for (i in 1..2) {
//            list.add("item$i")
//        }
        list
    }

    LazyColumn {
        ListToGridItems(list, 3) { index, item ->
           Box(modifier = Modifier.fillMaxWidth().height(50.dp), contentAlignment = Alignment.Center) {
               Text("$index------$item")
           }
        }
    }
}



package ui.todo

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun TestPage() {

    Box(Modifier.padding(top = 50.dp)) {
//        val text = DataStoreUtils.readStringData("test", "unknown")
//        val text = DataStoreUtils.getData("test",  "unknown").collectAsState("unknown")
//        ExpandableText(modifier = Modifier.width(200.dp).background(Color.Red), text = "1失恋的人建议不要去听2失恋的人建议不要去听3失恋的人建议不要去听4失恋的人建议不要去听")
    }
//    val list = remember {
//        val list = mutableListOf<String>()
////        for (i in 1..2) {
////            list.add("item$i")
////        }
//        list
//    }
//
//    LazyColumn {
//        ListToGridItems(list, 3) { index, item ->
//           Box(modifier = Modifier.fillMaxWidth().height(50.dp), contentAlignment = Alignment.Center) {
//               Text("$index------$item")
//           }
//        }
//    }

    LaunchedEffect(Unit) {
        delay(3000)

        //DataStoreUtils.putData("test", "testLocalData new")
    }
}



package br.edu.satc.todolistcompose.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.edu.satc.todolistcompose.ui.theme.ToDoListComposeTheme

@Composable
fun TaskCard(
    title: String = "Task title",
    description: String = "Task description",
    complete: Boolean = false
) {
    val taskTitle by remember {
        mutableStateOf(title)
    }
    val taskDescription by remember {
        mutableStateOf(description)
    }
    var taskComplete by remember {
        mutableStateOf(complete)
    }

    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ), modifier = Modifier.padding(top = 8.dp).fillMaxWidth().height(100.dp)

    ) {
        Column(
            modifier = Modifier.padding(8.dp).fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = taskTitle,
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        fontFamily = FontFamily.Serif
                    )
                )
                Checkbox(checked = taskComplete, onCheckedChange = { taskComplete = it })
            }
            Text(text = taskDescription, fontSize = 12.sp)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TaskCardPreview() {
    ToDoListComposeTheme {
        TaskCard()
    }
}
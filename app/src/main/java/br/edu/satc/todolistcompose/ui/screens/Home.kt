@file:OptIn(ExperimentalMaterial3Api::class)

package br.edu.satc.todolistcompose.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.room.Room
import br.edu.satc.todolistcompose.TaskData
import br.edu.satc.todolistcompose.repositories.AppDatabase
import br.edu.satc.todolistcompose.repositories.TaskDao
import br.edu.satc.todolistcompose.ui.components.TaskCard
import kotlinx.coroutines.launch


@Composable
fun HomeScreen(taskDao: TaskDao) {
    var showBottomSheet by remember { mutableStateOf(false) }
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val tasks = remember { mutableStateListOf<TaskData>() }

    LaunchedEffect(Unit) {
        tasks.clear()
        tasks.addAll(taskDao.getAll())
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = { Text(text = "ToDoList UniSATC") },
                actions = {
                    IconButton(onClick = { /* do something */ }) {
                        Icon(Icons.Rounded.Settings, contentDescription = "")
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text("Nova tarefa") },
                icon = { Icon(Icons.Filled.Add, contentDescription = "") },
                onClick = {
                    showBottomSheet = true
                }
            )
        }
    ) { innerPadding ->
        HomeContent(innerPadding, tasks)
        NewTask(
            showBottomSheet = showBottomSheet,
            taskDao = taskDao,
            onComplete = { showBottomSheet = false },
            onTaskSaved = { newTask ->
                tasks.add(newTask)
            }
        )
    }
}

@Composable
fun HomeContent(innerPadding: PaddingValues, tasks: List<TaskData>) {
    Column(
        modifier = Modifier
            .padding(horizontal = 4.dp)
            .padding(top = innerPadding.calculateTopPadding())
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top
    ) {
        for (task in tasks) {
            TaskCard(task.title, task.description, task.complete)
        }
    }
}

@Composable
fun NewTask(
    showBottomSheet: Boolean,
    taskDao: TaskDao,
    onComplete: () -> Unit,
    onTaskSaved: (TaskData) -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var taskTitle by remember { mutableStateOf("") }
    var taskDescription by remember { mutableStateOf("") }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                onComplete()
            },
            sheetState = sheetState
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    value = taskTitle,
                    onValueChange = { taskTitle = it },
                    label = { Text(text = "Título da tarefa") }
                )
                OutlinedTextField(
                    value = taskDescription,
                    onValueChange = { taskDescription = it },
                    label = { Text(text = "Descrição da tarefa") }
                )
                Button(
                    modifier = Modifier.padding(top = 4.dp),
                    onClick = {
                        val newTask = TaskData(
                            uid = 0,
                            title = taskTitle,
                            description = taskDescription,
                            complete = false
                        )
                        scope.launch {
                            taskDao.insertAll(newTask)
                            onTaskSaved(newTask)
                            sheetState.hide()
                        }.invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                onComplete()
                            }
                        }
                    }
                ) {
                    Text("Salvar")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    val fakeTasks = listOf(
        TaskData(1, "Estudar", "Estudar Compose", false),
        TaskData(2, "Ir ao mercado", "Comprar pão", true)
    )

    HomeContentPreview(fakeTasks)
}

@Composable
fun HomeContentPreview(tasks: List<TaskData>) {
    Column(
        modifier = Modifier
            .padding(horizontal = 4.dp)
            .padding(top = 16.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top
    ) {
        for (task in tasks) {
            TaskCard(task.title, task.description, task.complete)
        }
    }
}

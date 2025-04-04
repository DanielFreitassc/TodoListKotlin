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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.edu.satc.todolistcompose.TaskData
import br.edu.satc.todolistcompose.ui.components.TaskCard
import kotlinx.coroutines.launch


@Preview(showBackground = true)
@Composable
fun HomeScreen() {

    // states by remember
    // Guardam valores importantes de controle em nossa home
    var showBottomSheet by remember { mutableStateOf(false) }
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    /**
     * O componente Scaffold facilita a construção de telas seguindo as guidelines
     * do Google / Android / Material Design 3.
     * Com ele podemos facilmente incluir uma TopBar, BottomBar, FAB, etc.
     */
    Scaffold(

        /**
         * Aqui informamos como desejamos o comportamento da Tela quando
         * for realizado um "scroll" na lista
         * */
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),

        /**
         * Aqui declaramos nossa Top Bar e o conteúdo dela
         * */
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = { Text(text = "ToDoList UniSATC") },
                actions =
                {
                    /**
                     * Este é o botão de Settings que aparece no canto direito da TopBar
                     * Podemos usar ele para acessar alguma configuração do App.
                     * * */
                    IconButton(onClick = { /* do something */ }) {
                        Icon(
                            Icons.Rounded.Settings,
                            contentDescription = ""
                        )
                    }
                },
                /**
                 * Aplicamos um comportamento para o scroll da TopBar
                 * Neste caso, queremos que ela fique fixa.
                 * TopAppBarDefaults.pinnedScrollBehavior
                 */
                scrollBehavior = scrollBehavior
            )
        },

        /**
         * Aqui nosso FAB (Float Action Button).
         * Ele sempre fica ao pé da tela, a direita. Serve para disparar a ação principal da tela.
         * Neste caso, vamos usar para criar uma nova Task. Portanto ao clicar no button,
         * chamamos nosso "bottom sheet" que cria uma nova Task.
         */
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
        /**
         * Aqui exibimos o conteúdo da tela.
         * O que aparece no "meio".
         * Para ficar mais organizado, montei o conteúdo em functions separadas.
         * */
        HomeContent(innerPadding)
        NewTask(showBottomSheet = showBottomSheet) { showBottomSheet = false }

    }
}

@Composable
fun HomeContent(innerPadding: PaddingValues) {

    val tasks = mutableListOf<TaskData>()
    for (i in 0..5) {
        tasks.add(TaskData("Tarefa " + i, "Descricao " + i, i % 2 == 0))
    }

    /**
     * Aqui simplesmente temos uma Column com o nosso conteúdo.
     * A chamada verticalScroll(rememberScrollState()), passada para o Modifier,
     * avisa que o conteúdo será uma lista que pode precisar de scroll nessa tela.
     *
     * TaskCard exibe o conteúdo de uma tarefa. O conteúdo pode ser passado na chamada da function
     */

    Column(
        modifier = Modifier
            .padding(horizontal = 4.dp)
            .padding(top = innerPadding.calculateTopPadding())
            .fillMaxSize()
            .verticalScroll(
                rememberScrollState()
            ),
        verticalArrangement = Arrangement.Top
    ) {
        for (task in tasks) {
            TaskCard(task.title, task.description, task.complete)
        }
    }
}

/**
 * NewTask abre uma janela estilo "modal". No Android conhecida por BottomSheet.
 * Aqui podemos "cadastrar uma nova Task".
 */
@Composable
fun NewTask(showBottomSheet: Boolean, onComplete: () -> Unit) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var taskTitle by remember {
        mutableStateOf("")
    }
    var taskDescription by remember {
        mutableStateOf("")
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                onComplete()
            },
            sheetState = sheetState,

            ) {
            // Sheet content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                OutlinedTextField(
                    value = taskTitle,
                    onValueChange = {taskTitle = it},
                    label = { Text(text = "Título da tarefa") })
                OutlinedTextField(
                    value = taskDescription,
                    onValueChange = {taskDescription = it},
                    label = { Text(text = "Descrição da tarefa") })
                Button(modifier = Modifier.padding(top = 4.dp), onClick = {
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            onComplete()
                        }
                    }
                }) {
                    Text("Salvar")
                }
            }
        }
    }
}
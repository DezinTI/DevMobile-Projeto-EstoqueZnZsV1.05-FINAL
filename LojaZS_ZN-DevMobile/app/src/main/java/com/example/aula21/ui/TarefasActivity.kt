package com.example.aula21.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.aula21.ui.common.SimpleFactory
import com.example.aula21.ui.ui.theme.Aula21Theme
import com.example.aula21.viewmodel.TodosViewModel

class TarefasActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val userId = intent.getIntExtra("userId", -1)
        val username = intent.getStringExtra("username") ?: "Unknow User"

        setContent {
            TelaTarefas(userId, username)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TelaTarefas(userId: Int, username: String) {

    val context = LocalContext.current
    val tarefasVM : TodosViewModel = viewModel(
        factory = SimpleFactory { TodosViewModel(context) }
    )
    var status by remember {  mutableStateOf<String?>(null) }

    Column(
        Modifier
            .fillMaxSize()
            .padding(15.dp)) {

        if (userId <= 0) {
            Text("Erro ao carregar dados")
        } else {

            tarefasVM.loadTodos(userId)

            Text("Lista de Tarefas - $username",
                textAlign = TextAlign.Center,
                fontSize = 25.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp))

            LazyColumn{

                items(tarefasVM.listTodos.value){

                    tarefaAtual ->

                    status = if (tarefaAtual.completed) "Concluída" else "Pendente"

                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(vertical = 5.dp)
                            .combinedClickable (
                                onClick = {
                                          tarefasVM.changeStatus(tarefaAtual)
                                },
                                onLongClick = {
                                    tarefasVM.deleteTodo(tarefaAtual)
                            })
                    ) {

                        Text(text = tarefaAtual.title,
                            fontSize = 22.sp)
                        Text(text = status!!,
                            fontSize = 18.sp)

                        Divider(Modifier.padding(vertical = 3.dp))

                    }

                }
            }

        }

    }

}
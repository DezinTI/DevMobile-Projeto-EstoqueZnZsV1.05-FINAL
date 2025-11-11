package com.example.aula21.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.aula21.model.repository.UsuarioRepository
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClienteSearchScreen(nav: NavHostController) {
    val email = remember { mutableStateOf("") }
    val cpf = remember { mutableStateOf("") }
    val resultado = remember { mutableStateOf("") }
    val repo = UsuarioRepository(nav.context)
    val scope = rememberCoroutineScope()

    Column(Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = email.value,
            onValueChange = { email.value = it },
            label = { Text("Buscar por e-mail") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = cpf.value,
            onValueChange = { cpf.value = it },
            label = { Text("Buscar por CPF") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        )
        Button(
            onClick = {
                scope.launch {
                    resultado.value = ""
                    if (email.value.isNotBlank()) {
                        try {
                            val u = repo.buscarPorEmail(email.value)
                            resultado.value = u?.let { "${it.nome} • ${it.email} • ${it.cpf}" } ?: "Não encontrado"
                        } catch (e: Exception) {
                            resultado.value = "Erro: ${e.message}"
                        }
                    } else if (cpf.value.isNotBlank()) {
                        try {
                            val u = repo.buscarPorCpf(cpf.value)
                            resultado.value = u?.let { "${it.nome} • ${it.email} • ${it.cpf}" } ?: "Não encontrado"
                        } catch (e: Exception) {
                            resultado.value = "Erro: ${e.message}"
                        }
                    } else {
                        resultado.value = "Informe e-mail ou CPF"
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
        ) { Text("Buscar") }

        if (resultado.value.isNotEmpty()) {
            LazyColumn(modifier = Modifier.padding(top = 12.dp)) {
                item {
                    Text(resultado.value)
                }
            }
        }

        Button(onClick = { nav.popBackStack() }, modifier = Modifier.padding(top = 8.dp)) {
            Text("Voltar")
        }
    }
}


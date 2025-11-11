package com.example.aula21.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.aula21.ui.navigation.NavRoutes
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.aula21.ui.common.SimpleFactory
import com.example.aula21.viewmodel.CamisetasViewModel
import com.example.aula21.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CamisetaDetailScreen(nav: NavHostController, id: Int) {
    val vm: CamisetasViewModel = viewModel(factory = SimpleFactory { CamisetasViewModel(nav.context) })
    val auth: AuthViewModel = viewModel(factory = SimpleFactory { AuthViewModel(nav.context) })
    val item = vm.selecionada.collectAsState()
    val isAdmin = auth.isAdmin.collectAsState()
    val entrada = remember { mutableStateOf("") }

    LaunchedEffect(id) { vm.carregarPorId(id) }

    Column(Modifier.padding(16.dp)) {
        val c = item.value
        if (c != null) {
            Text(text = c.nome)
            Text(text = "Time: ${c.time}")
            Text(text = "Tamanho: ${c.tamanho}")
            Text(text = "Quantidade: ${c.quantidade}")
            Text(text = "Preço: R$ ${c.preco}")

            if (isAdmin.value) {
                Button(onClick = { nav.navigate(NavRoutes.CamisetaForm.edit(c.id)) }, modifier = Modifier.padding(top = 8.dp)) {
                    Text("Editar")
                }
                Button(onClick = { vm.excluir(c); nav.popBackStack() }, modifier = Modifier.padding(top = 8.dp)) {
                    Text("Excluir")
                }
            } else {
                OutlinedTextField(
                    value = entrada.value,
                    onValueChange = { entrada.value = it },
                    label = { Text("Entrada de estoque (quantidade)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                )
                Button(onClick = {
                    val q = entrada.value.toIntOrNull() ?: 0
                    if (q > 0) vm.entradaEstoque(c.id, q)
                }, modifier = Modifier.padding(top = 8.dp)) { Text("Dar Entrada") }
            }
        } else {
            Text("Carregando...")
        }
        Button(onClick = { nav.popBackStack() }, modifier = Modifier.padding(top = 8.dp)) {
            Text("Voltar")
        }
    }
}

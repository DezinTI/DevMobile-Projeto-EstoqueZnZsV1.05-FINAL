package com.example.aula21.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
fun CamisetaListScreen(nav: NavHostController) {
    val vm: CamisetasViewModel = viewModel(factory = SimpleFactory { CamisetasViewModel(nav.context) })
    val auth: AuthViewModel = viewModel(factory = SimpleFactory { AuthViewModel(nav.context) })
    val lista = vm.camisetas.collectAsState()
    val query = vm.termoBusca.collectAsState()
    val isAdmin = auth.isAdmin.collectAsState()

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        OutlinedTextField(
            value = query.value,
            onValueChange = { vm.atualizarBusca(it) },
            label = { Text("Buscar por ID/nome/time/tamanho") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        if (isAdmin.value) {
            Button(
                onClick = { nav.navigate(NavRoutes.CamisetaForm.default.replace("{editId}", "-1")) },
                modifier = Modifier.padding(top = 8.dp)
            ) { Text("Adicionar") }
        }
        LazyColumn(modifier = Modifier.padding(top = 12.dp)) {
            items(lista.value, key = { it.id }) { item ->
                val codigo = String.format("%02d", item.id)
                Text(
                    text = "$codigo - ${item.nome} • ${item.time} • ${item.tamanho} • x${item.quantidade}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp)
                        .clickable { nav.navigate(NavRoutes.CamisetaDetail.build(item.id)) }
                )
            }
        }
    }
}

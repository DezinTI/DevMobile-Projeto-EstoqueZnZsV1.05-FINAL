package com.example.aula21.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.aula21.ui.common.SimpleFactory
import com.example.aula21.ui.navigation.NavRoutes
import com.example.aula21.viewmodel.PedidosViewModel
import com.example.aula21.viewmodel.AuthViewModel

@Composable
fun PedidoListScreen(nav: NavHostController) {
    val vm: PedidosViewModel = viewModel(factory = SimpleFactory { PedidosViewModel(nav.context) })
    val auth: AuthViewModel = viewModel(factory = SimpleFactory { AuthViewModel(nav.context) })
    val pedidos = vm.pedidos.collectAsState()

    Column(Modifier.padding(16.dp)) {
        Button(onClick = {
            val user = auth.userId.value ?: "local"
            vm.novoRascunho(user) { id -> nav.navigate(NavRoutes.PedidoForm.edit(id)) }
        }, modifier = Modifier.fillMaxWidth()) { Text("Novo Pedido") }

        val abertos = pedidos.value.filter { it.status == "rascunho" }
        val baixados = pedidos.value.filter { it.status == "baixado" }

        LazyColumn(modifier = Modifier.padding(top = 12.dp)) {
            if (abertos.isNotEmpty()) {
                item { Text("Em aberto", modifier = Modifier.padding(vertical = 6.dp)) }
                items(abertos, key = { it.id }) { p ->
                    val titulo = "PEDIDO ${p.id}"
                    Text(
                        text = "$titulo • Total: R$ ${"%.2f".format(p.total)} • ${p.status}",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .clickable { nav.navigate(NavRoutes.PedidoDetail.build(p.id)) }
                    )
                }
            }
            if (baixados.isNotEmpty()) {
                item { Text("Baixados", modifier = Modifier.padding(vertical = 6.dp)) }
                items(baixados, key = { it.id }) { p ->
                    val titulo = "PEDIDO ${p.id}"
                    Text(
                        text = "$titulo • Total: R$ ${"%.2f".format(p.total)} • ${p.status}",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .clickable { nav.navigate(NavRoutes.PedidoDetail.build(p.id)) }
                    )
                }
            }
        }
    }
}

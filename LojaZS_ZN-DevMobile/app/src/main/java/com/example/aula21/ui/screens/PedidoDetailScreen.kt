package com.example.aula21.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
fun PedidoDetailScreen(nav: NavHostController, id: Int) {
    val vm: PedidosViewModel = viewModel(factory = SimpleFactory { PedidosViewModel(nav.context) })
    val auth: AuthViewModel = viewModel(factory = SimpleFactory { AuthViewModel(nav.context) })
    val pedido = vm.selecionado.collectAsState()
    val itens = vm.itensTemp.collectAsState()
    val erro = vm.erro.collectAsState()
    val msg = vm.mensagem.collectAsState()
    val userId = auth.userId.collectAsState()
    val isTeste = (userId.value == "teste")

    LaunchedEffect(id) { vm.carregarPedido(id) }

    Column(Modifier.padding(16.dp)) {
        val p = pedido.value
        if (p != null) {
            Text("PEDIDO ${p.id}")
            Text("Status: ${p.status}")
            Text("Total: R$ ${"%.2f".format(p.total)}")

            itens.value.forEach { i ->
                Text("- ${i.nomeCamiseta} x${i.quantidade} = R$ ${"%.2f".format(i.subtotal)}")
            }

            Button(onClick = { nav.navigate(NavRoutes.PedidoForm.edit(p.id)) }, modifier = Modifier.padding(top = 8.dp)) { Text("Editar") }
            if (p.status != "baixado") {
                Button(onClick = { vm.baixarPedido() }, enabled = !isTeste, modifier = Modifier.padding(top = 8.dp)) { Text("Baixar") }
            }
            if (p.status == "baixado") {
                Button(onClick = { vm.faturarPedido() }, modifier = Modifier.padding(top = 8.dp)) { Text("Faturar") }
            }
            if (p.status == "faturado") {
                Button(onClick = { vm.fecharPedido() }, modifier = Modifier.padding(top = 8.dp)) { Text("Fechar") }
            }
            Button(onClick = { vm.excluirSelecionado(); nav.popBackStack() }, enabled = !isTeste, modifier = Modifier.padding(top = 8.dp)) { Text("Excluir") }
            if (erro.value != null) Text(erro.value!!, modifier = Modifier.padding(top = 8.dp))
            if (msg.value != null) Text(msg.value!!, modifier = Modifier.padding(top = 8.dp))
        } else {
            Text("Carregando...")
        }
        Button(onClick = { nav.popBackStack() }, modifier = Modifier.padding(top = 8.dp)) { Text("Voltar") }
    }
}


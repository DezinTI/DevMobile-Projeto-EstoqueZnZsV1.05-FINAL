package com.example.aula21.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.aula21.ui.common.SimpleFactory
import com.example.aula21.viewmodel.PedidosViewModel
import com.example.aula21.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PedidoFormScreen(nav: NavHostController, editId: Int) {
    val vm: PedidosViewModel = viewModel(factory = SimpleFactory { PedidosViewModel(nav.context) })
    val auth: AuthViewModel = viewModel(factory = SimpleFactory { AuthViewModel(nav.context) })

    val pedido = vm.selecionado.collectAsState()
    val itens = vm.itensTemp.collectAsState()
    val erro = vm.erro.collectAsState()
    val msg = vm.mensagem.collectAsState()
    val userId = auth.userId.collectAsState()
    val isTeste = (userId.value == "teste")

    val entrada = remember { mutableStateOf("") }
    val quantidade = remember { mutableStateOf("") }

    LaunchedEffect(editId) {
        if (editId >= 0) {
            vm.carregarPedido(editId)
        } else {
            val user = auth.userId.value ?: "local"
            vm.novoRascunho(user) { id -> vm.carregarPedido(id) }
        }
    }

    Column(Modifier.padding(16.dp)) {
        Text("${pedido.value?.let { "PEDIDO ${it.id}" } ?: "Carregando..."}")
        if (erro.value != null) Text(erro.value!!, modifier = Modifier.padding(top = 8.dp))
        if (msg.value != null) Text(msg.value!!, modifier = Modifier.padding(top = 8.dp))

        OutlinedTextField(
            value = entrada.value,
            onValueChange = { entrada.value = it },
            label = { Text("ID ou Nome da Camiseta") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        )
        OutlinedTextField(
            value = quantidade.value,
            onValueChange = { quantidade.value = it },
            label = { Text("Quantidade") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        )
        Row(Modifier.fillMaxWidth().padding(top = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = {
                val q = quantidade.value.toIntOrNull() ?: 0
                vm.adicionarItemPorIdOuNome(entrada.value.trim(), q)
            }, modifier = Modifier.weight(1f)) { Text("Adicionar Item") }
            Button(onClick = { vm.salvarItens() }, modifier = Modifier.weight(1f)) { Text("Salvar Itens") }
        }

        LazyColumn(modifier = Modifier.padding(top = 12.dp)) {
            itemsIndexed(itens.value) { index, item ->
                val qState = remember(item.id, item.quantidade) { mutableStateOf(item.quantidade.toString()) }
                Column(Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
                    Text("${item.nomeCamiseta} x${item.quantidade} = R$ ${"%.2f".format(item.subtotal)}")
                    Row(Modifier.fillMaxWidth().padding(top = 6.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = qState.value,
                            onValueChange = { qState.value = it },
                            label = { Text("Alterar Qtde") },
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )
                        Button(onClick = {
                            val q = qState.value.toIntOrNull() ?: 0
                            vm.atualizarItemTemp(index, q)
                        }) { Text("Atualizar") }
                        Button(onClick = { vm.removerItemTemp(index) }) { Text("Remover") }
                    }
                }
            }
        }

        Row(Modifier.fillMaxWidth().padding(top = 12.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { vm.baixarPedido() }, enabled = !isTeste, modifier = Modifier.weight(1f)) { Text("Baixar Pedido") }
            Button(onClick = { nav.popBackStack() }, modifier = Modifier.weight(1f)) { Text("Voltar") }
        }
    }
}

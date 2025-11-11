package com.example.aula21.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.aula21.ui.common.SimpleFactory
import com.example.aula21.viewmodel.CamisetasViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CamisetaFormScreen(nav: NavHostController, editId: Int) {
    val vm: CamisetasViewModel = viewModel(factory = SimpleFactory { CamisetasViewModel(nav.context) })
    val nome = remember { mutableStateOf("") }
    val time = remember { mutableStateOf("") }
    val tamanho = remember { mutableStateOf("") }
    val quantidade = remember { mutableStateOf("") }
    val preco = remember { mutableStateOf("") }
    val imagemUrl = remember { mutableStateOf("") }
    var submitted by remember { mutableStateOf(false) }

    LaunchedEffect(editId) {
        if (editId >= 0) {
            vm.carregarPorId(editId)
        }
    }
    LaunchedEffect(vm.selecionada.value) {
        vm.selecionada.value?.let { c ->
            nome.value = c.nome
            time.value = c.time
            tamanho.value = c.tamanho
            quantidade.value = c.quantidade.toString()
            preco.value = c.preco.toString()
            imagemUrl.value = c.imagemUrl.orEmpty()
        }
    }

    Column(Modifier.padding(16.dp)) {
        OutlinedTextField(nome.value, { nome.value = it }, label = { Text("Nome") }, singleLine = true, modifier = Modifier.fillMaxWidth())
        if (submitted && nome.value.isBlank()) Text("Falta especificar nome", modifier = Modifier.padding(top = 4.dp))
        OutlinedTextField(time.value, { time.value = it }, label = { Text("Time") }, singleLine = true, modifier = Modifier.fillMaxWidth().padding(top = 8.dp))
        if (submitted && time.value.isBlank()) Text("Falta especificar time", modifier = Modifier.padding(top = 4.dp))
        OutlinedTextField(tamanho.value, { tamanho.value = it }, label = { Text("Tamanho") }, singleLine = true, modifier = Modifier.fillMaxWidth().padding(top = 8.dp))
        if (submitted && tamanho.value.isBlank()) Text("Falta especificar tamanho", modifier = Modifier.padding(top = 4.dp))
        OutlinedTextField(quantidade.value, { quantidade.value = it }, label = { Text("Quantidade") }, singleLine = true, modifier = Modifier.fillMaxWidth().padding(top = 8.dp))
        if (submitted && quantidade.value.toIntOrNull() == null) Text("Quantidade inválida", modifier = Modifier.padding(top = 4.dp))
        OutlinedTextField(preco.value, { preco.value = it }, label = { Text("Preço") }, singleLine = true, modifier = Modifier.fillMaxWidth().padding(top = 8.dp))
        if (submitted && preco.value.toDoubleOrNull() == null) Text("Preço inválido", modifier = Modifier.padding(top = 4.dp))
        OutlinedTextField(imagemUrl.value, { imagemUrl.value = it }, label = { Text("Imagem URL (opcional)") }, singleLine = true, modifier = Modifier.fillMaxWidth().padding(top = 8.dp))

        val canSave = nome.value.isNotBlank() && time.value.isNotBlank() && tamanho.value.isNotBlank() && quantidade.value.toIntOrNull() != null && preco.value.toDoubleOrNull() != null
        Button(
            onClick = {
                submitted = true
                vm.salvar(
                    id = if (editId >= 0) editId else null,
                    nome = nome.value,
                    time = time.value,
                    tamanho = tamanho.value,
                    quantidade = quantidade.value.toIntOrNull() ?: 0,
                    preco = preco.value.toDoubleOrNull() ?: 0.0,
                    imagemUrl = imagemUrl.value.ifBlank { null }
                )
                nav.popBackStack()
            },
            modifier = Modifier.padding(top = 12.dp),
            enabled = canSave
        ) { Text(if (editId >= 0) "Salvar" else "Cadastrar") }
    }
}

package com.example.aula21.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.aula21.ui.common.SimpleFactory
import com.example.aula21.viewmodel.AuthViewModel
import com.example.aula21.viewmodel.SyncViewModel

@Composable
fun SyncScreen(nav: NavHostController) {
    val authVM: AuthViewModel = viewModel(factory = SimpleFactory { AuthViewModel(nav.context) })
    val syncVM: SyncViewModel = viewModel(factory = SimpleFactory { SyncViewModel(nav.context) })
    val uid = authVM.userId.collectAsState()
    val status = syncVM.status.collectAsState()
    val loading = syncVM.loading.collectAsState()

    Column(Modifier.padding(16.dp)) {
        Text("Sincronização com API (Retrofit)")
        Text("Usuário: ${uid.value ?: "não autenticado"}", modifier = Modifier.padding(top = 8.dp))
        if (status.value != null) Text(status.value!!, modifier = Modifier.padding(top = 8.dp))
        Button(
            onClick = { uid.value?.let { syncVM.pull(it) } },
            modifier = Modifier.padding(top = 8.dp),
            enabled = uid.value != null && !loading.value
        ) { Text("Pull (Baixar da API)") }
        Button(
            onClick = { uid.value?.let { syncVM.push(it) } },
            modifier = Modifier.padding(top = 8.dp),
            enabled = uid.value != null && !loading.value
        ) { Text("Push (Não suportado pela API)") }
        Button(onClick = { nav.popBackStack() }, modifier = Modifier.padding(top = 8.dp)) {
            Text("Voltar")
        }
    }
}

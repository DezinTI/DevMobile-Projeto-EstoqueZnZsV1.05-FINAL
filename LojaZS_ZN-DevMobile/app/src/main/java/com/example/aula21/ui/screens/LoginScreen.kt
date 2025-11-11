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
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.aula21.ui.navigation.NavRoutes
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.aula21.ui.common.SimpleFactory
import com.example.aula21.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(nav: NavHostController) {
    val vm: AuthViewModel = viewModel(factory = SimpleFactory { AuthViewModel(nav.context) })
    val email = remember { mutableStateOf("") }
    val senha = remember { mutableStateOf("") }
    val erro = vm.error.collectAsState()
    val userId = vm.userId.collectAsState()

    // Observa o userId como State; ao autenticar com sucesso, navega para Home
    LaunchedEffect(userId.value) {
        val uid = userId.value
        if (uid != null) {
            nav.navigate(NavRoutes.Home.route) {
                popUpTo(NavRoutes.Login.route) { inclusive = true }
            }
        }
    }

    Column(Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = email.value,
            onValueChange = { email.value = it },
            label = { Text("User: nome") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = senha.value,
            onValueChange = { senha.value = it },
            label = { Text("Senha") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        )
        if (erro.value != null) {
            Text(text = erro.value!!, modifier = Modifier.padding(top = 8.dp))
        }
        Button(
            onClick = { vm.login(email.value, senha.value) },
            enabled = email.value.isNotBlank() && senha.value.isNotBlank(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
        ) {
            Text("Entrar")
        }
    }
}

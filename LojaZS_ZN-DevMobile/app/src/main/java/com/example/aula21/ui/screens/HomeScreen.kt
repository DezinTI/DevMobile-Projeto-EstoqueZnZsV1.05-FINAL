package com.example.aula21.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.aula21.ui.navigation.NavRoutes
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.aula21.ui.common.SimpleFactory
import com.example.aula21.viewmodel.AuthViewModel

@Composable
fun HomeScreen(nav: NavHostController) {
    val authVM: AuthViewModel = viewModel(factory = SimpleFactory { AuthViewModel(nav.context) })
    Column(Modifier.padding(16.dp)) {
        Button(
            onClick = { nav.navigate(NavRoutes.Camisetas.route) },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Ver Estoque") }

        Button(
            onClick = { nav.navigate(NavRoutes.CamisetaForm.default.replace("{editId}", "-1")) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) { Text("Adicionar Camiseta") }

        Button(
            onClick = { nav.navigate(NavRoutes.Pedidos.route) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) { Text("Pedidos") }

        Button(
            onClick = { nav.navigate(NavRoutes.PedidoForm.default.replace("{editId}", "-1")) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) { Text("Novo Pedido") }

        Button(
            onClick = {
                authVM.logout()
                nav.navigate(NavRoutes.Login.route) {
                    popUpTo(NavRoutes.Home.route) { inclusive = true }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) { Text("Logout") }
    }
}

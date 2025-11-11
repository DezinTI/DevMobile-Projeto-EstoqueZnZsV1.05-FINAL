package com.example.aula21.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.example.aula21.ui.screens.*

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = NavRoutes.Splash.route) {
        composable(NavRoutes.Splash.route) { SplashScreen(navController) }
        composable(NavRoutes.Login.route) { LoginScreen(navController) }
        composable(NavRoutes.Home.route) { HomeScreen(navController) }
        composable(NavRoutes.Camisetas.route) { CamisetaListScreen(navController) }
        composable(
            NavRoutes.CamisetaDetail.route,
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: 0
            CamisetaDetailScreen(navController, id)
        }
        composable(
            NavRoutes.CamisetaForm.default,
            arguments = listOf(navArgument("editId") {
                type = NavType.IntType
                defaultValue = -1
                nullable = false
            })
        ) { backStackEntry ->
            val editId = backStackEntry.arguments?.getInt("editId") ?: -1
            CamisetaFormScreen(navController, editId)
        }
        composable(NavRoutes.Pedidos.route) { PedidoListScreen(navController) }
        composable(
            NavRoutes.PedidoDetail.route,
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: 0
            PedidoDetailScreen(navController, id)
        }
        composable(
            NavRoutes.PedidoForm.default,
            arguments = listOf(navArgument("editId") {
                type = NavType.IntType
                defaultValue = -1
                nullable = false
            })
        ) { backStackEntry ->
            val editId = backStackEntry.arguments?.getInt("editId") ?: -1
            PedidoFormScreen(navController, editId)
        }
    }
}


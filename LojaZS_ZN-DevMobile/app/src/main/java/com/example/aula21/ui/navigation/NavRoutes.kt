package com.example.aula21.ui.navigation

sealed class NavRoutes(val route: String) {
    data object Splash: NavRoutes("splash")
    data object Login: NavRoutes("login")
    data object Home: NavRoutes("home")
    data object Camisetas: NavRoutes("camisetas")
    data object CamisetaDetail: NavRoutes("camiseta/{id}") { fun build(id: Int) = "camiseta/$id" }
    data object CamisetaForm: NavRoutes("camisetaForm?editId={editId}") { fun edit(id: Int) = "camisetaForm?editId=$id"; const val default = "camisetaForm?editId={editId}" }
    data object Pedidos: NavRoutes("pedidos")
    data object PedidoDetail: NavRoutes("pedido/{id}") { fun build(id: Int) = "pedido/$id" }
    data object PedidoForm: NavRoutes("pedidoForm?editId={editId}") { fun edit(id: Int) = "pedidoForm?editId=$id"; const val default = "pedidoForm?editId={editId}" }
}

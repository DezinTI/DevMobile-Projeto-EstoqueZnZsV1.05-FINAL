package com.example.aula21.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.aula21.ui.navigation.NavRoutes
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(nav: NavHostController) {
    LaunchedEffect(Unit) {
        delay(800)
        nav.navigate(NavRoutes.Login.route) {
            popUpTo(NavRoutes.Splash.route) { inclusive = true }
        }
    }
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

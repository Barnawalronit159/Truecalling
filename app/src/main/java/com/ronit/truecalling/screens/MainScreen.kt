package com.ronit.truecalling.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.*
import com.ronit.truecalling.navigation.Screen

@Composable
fun MainScreen() {

    val navController = rememberNavController()
    val currentRoute = navController
        .currentBackStackEntryAsState().value?.destination?.route

    Scaffold(
        bottomBar = {
            NavigationBar {

                NavigationBarItem(
                    selected = currentRoute == Screen.Dialer.route,
                    onClick = { navController.navigate(Screen.Dialer.route) {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    } },
                    icon = { Text("📞") },
                    label = { Text("Dialer") }
                )

                NavigationBarItem(
                    selected = currentRoute == Screen.Contacts.route,
                    onClick = { navController.navigate(Screen.Contacts.route) },
                    icon = { Text("👥") },
                    label = { Text("Contacts") }
                )

                NavigationBarItem(
                    selected = currentRoute == Screen.Logs.route,
                    onClick = { navController.navigate(Screen.Logs.route) },
                    icon = { Text("📋") },
                    label = { Text("Logs") }
                )

                NavigationBarItem(
                    selected = currentRoute == Screen.Timer.route,
                    onClick = { navController.navigate(Screen.Timer.route) },
                    icon = { Text("⏱") },
                    label = { Text("Timer") }
                )
            }
        }
    ) { padding ->

        NavHost(
            navController = navController,
            startDestination = Screen.Dialer.route,
            modifier = Modifier.padding(padding)
        ) {

            composable(Screen.Dialer.route) {
                DialerScreen()
            }

            composable(Screen.Contacts.route) {
                ContactsScreen()
            }

            composable(Screen.Logs.route) {
                CallLogScreen()
            }

            composable(Screen.Timer.route) {
                CallTimerScreen()
            }
        }
    }
}
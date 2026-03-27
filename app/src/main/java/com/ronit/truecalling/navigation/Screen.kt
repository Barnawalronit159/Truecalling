package com.ronit.truecalling.navigation

sealed class Screen(val route: String) {
    object Dialer : Screen("dialer")
    object Contacts : Screen("contacts")
    object Logs : Screen("logs")
    object Timer : Screen("timer")
}
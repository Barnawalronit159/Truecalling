package com.ronit.truecalling

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.ronit.truecalling.screens.MainScreen
import com.ronit.truecalling.ui.theme.TrueCallingTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TrueCallingTheme {
                MainScreen()
            }
        }
    }
}




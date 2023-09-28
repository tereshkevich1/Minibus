package com.example.minibus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.minibus.screens.MainScreen
import com.example.minibus.ui.theme.MinibusTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MinibusTheme {
                MainScreen()
            }
        }
    }
}



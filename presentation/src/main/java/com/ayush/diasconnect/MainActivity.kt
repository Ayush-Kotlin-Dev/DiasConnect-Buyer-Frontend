package com.ayush.diasconnect

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.ayush.diasconnect.home.ProductScreen
import com.ayush.diasconnect.ui.theme.DiasConnectTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContent {
            DiasConnectTheme {
                ProductScreen()
            }
        }
    }
}

package com.ayush.diasconnect

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.FadeTransition
import cafe.adriel.voyager.transitions.SlideTransition
import com.ayush.diasconnect.auth.AuthScreen
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
                val viewModel: MainActivityViewModel = hiltViewModel()
                val uiState by viewModel.uiState.collectAsState()
                when (uiState) {
                    is MainActivityViewModel.UiState.Loading -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }
                    is MainActivityViewModel.UiState.LoggedIn -> {
                        Navigator(screen = ContainerApp()) { navigator ->
                            SlideTransition(navigator = navigator)
                        }
                    }
                    is MainActivityViewModel.UiState.LoggedOut -> {
                        Navigator(screen = AuthScreen()) { navigator ->
                            SlideTransition(navigator = navigator)
                        }
                    }
                }
            }
        }
    }
}


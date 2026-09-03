package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.di.AppContainer
import com.example.di.AppViewModelFactory
import com.example.presentation.main.MainNewsScreen
import com.example.presentation.news_afrique.AfricaNewsViewModel
import com.example.presentation.news_cameroun.CameroonNewsViewModel
import com.example.presentation.news_monde.WorldNewsViewModel
import com.example.ui.theme.MyApplicationTheme

/**
 * ============================================================================
 * POINT D'ENTRÉE ANDROID : MAIN ACTIVITY
 * ============================================================================
 * Initialise le conteneur d'injection de dépendances [AppContainer],
 * instancie les ViewModels via [AppViewModelFactory] et charge l'écran
 * principal [MainNewsScreen] avec Edge-to-Edge activé.
 */
class MainActivity : ComponentActivity() {

    private val appContainer by lazy { AppContainer(applicationContext) }
    private val viewModelFactory by lazy { AppViewModelFactory(appContainer) }

    private val cameroonViewModel: CameroonNewsViewModel by viewModels { viewModelFactory }
    private val africaViewModel: AfricaNewsViewModel by viewModels { viewModelFactory }
    private val worldViewModel: WorldNewsViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainNewsScreen(
                    cameroonViewModel = cameroonViewModel,
                    africaViewModel = africaViewModel,
                    worldViewModel = worldViewModel
                )
            }
        }
    }
}

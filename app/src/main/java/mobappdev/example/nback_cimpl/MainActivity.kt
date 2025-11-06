package mobappdev.example.nback_cimpl

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import mobappdev.example.nback_cimpl.ui.screens.GameScreen
import mobappdev.example.nback_cimpl.ui.screens.HomeScreen
import mobappdev.example.nback_cimpl.ui.theme.NBack_CImplTheme
import mobappdev.example.nback_cimpl.ui.viewmodels.GameVM

/**
 * This is the MainActivity of the application
 *
 * Your navigation between the two (or more) screens should be handled here
 * For this application you need at least a homescreen (a start is already made for you)
 * and a gamescreen (you will have to make yourself, but you can use the same viewmodel)
 *
 * Date: 25-08-2023
 * Version: Version 1.0
 * Author: Yeetivity
 *
 */


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NBack_CImplTheme {

                val gameViewModel: GameVM = viewModel(
                    factory = GameVM.Factory
                )

                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = "homescreen"
                ) {
                    composable("homescreen") {
                        HomeScreen(
                            vm = gameViewModel,
                            onStartGame = {
                                navController.navigate("gamescreen")
                            }
                        )
                    }
                    composable("gamescreen") {
                        GameScreen(
                            vm = gameViewModel,
                            onEndGame = {
                                navController.navigate("homescreen")
                            }
                        )
                    }
                }
            }
        }
    }
}
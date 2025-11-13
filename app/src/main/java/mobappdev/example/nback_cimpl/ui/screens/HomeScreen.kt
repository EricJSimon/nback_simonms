package mobappdev.example.nback_cimpl.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import mobappdev.example.nback_cimpl.R
import mobappdev.example.nback_cimpl.ui.viewmodels.FakeVM
import mobappdev.example.nback_cimpl.ui.viewmodels.GameType
import mobappdev.example.nback_cimpl.ui.viewmodels.GameViewModel
import kotlin.math.roundToInt

/**
 * This is the Home screen composable
 *
 * Currently this screen shows the saved highscore
 * It also contains a button which can be used to show that the C-integration works
 * Furthermore it contains two buttons that you can use to start a game
 *
 * Date: 25-08-2023
 * Version: Version 1.0
 * Author: Yeetivity & Simonms
 *
 */

@Composable
fun HomeScreen(
    vm: GameViewModel,
    onStartGame: () -> Unit
) {
    val highscore by vm.highscore.collectAsState()  // Highscore is its own StateFlow
    val snackBarHostState = remember { SnackbarHostState() }
    val nBackValue by vm.nBack.collectAsState()
    val gameState by vm.gameState.collectAsState()

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.padding(32.dp),
                text = "High-Score = $highscore",
                style = MaterialTheme.typography.headlineLarge
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Time: ${gameState.eventInterval/1000}s",
                    style = MaterialTheme.typography.headlineMedium
                )
                Text(
                    text = "Events: ${gameState.numberOfEvents}",
                    style = MaterialTheme.typography.headlineMedium
                )
            }
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    Text(
                        text = "N-Back: $nBackValue",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Slider(
                        modifier = Modifier.padding(horizontal = 64.dp),
                        value = nBackValue.toFloat(),
                        onValueChange = { newValue ->
                            vm.setNback(newValue.roundToInt())
                        },
                        valueRange = 1f..4f,
                        steps = 2
                    )
                }
            }

            Button(onClick = {
                onStartGame()
            }) {
                Text(
                    modifier = Modifier.padding(16.dp),
                    text = "Start Game".uppercase(),
                    style = MaterialTheme.typography.displaySmall
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(onClick = { vm.setGameType(GameType.Audio) }) {
                    Icon(
                        painter = painterResource(id = R.drawable.sound_on),
                        contentDescription = "Sound",
                        modifier = Modifier
                            .height(48.dp)
                            .aspectRatio(3f / 2f)
                    )
                }
                Button(
                    onClick = { vm.setGameType(GameType.Visual) }) {
                    Icon(
                        painter = painterResource(id = R.drawable.visual),
                        contentDescription = "Visual",
                        modifier = Modifier
                            .height(48.dp)
                            .aspectRatio(3f / 2f)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    Surface {
        HomeScreen(
            FakeVM(),
            onStartGame = {}
        )
    }
}
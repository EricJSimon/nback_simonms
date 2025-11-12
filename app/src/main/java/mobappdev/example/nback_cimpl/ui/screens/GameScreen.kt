package mobappdev.example.nback_cimpl.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import mobappdev.example.nback_cimpl.ui.components.GameBoard
import mobappdev.example.nback_cimpl.ui.viewmodels.FakeVM
import mobappdev.example.nback_cimpl.ui.viewmodels.GameType
import mobappdev.example.nback_cimpl.ui.viewmodels.GameViewModel


/**
 *
 * This is the gamescreen composable
 * The screen shows two buttons to
 * match either position or audio.
 *
 * The screen also takes a GameBoard
 * and sets the grid to the eventValues
 *
 * Date: 2025-11-06
 * @author Simonms
 *
 */
@Composable
fun GameScreen(
    vm: GameViewModel, onEndGame: () -> Unit
) {
    val gameState by vm.gameState.collectAsState()
    val currentScore by vm.score.collectAsState()

    var showDialog by remember { mutableStateOf(false) }

    val isVisualMatchEnabled =
        gameState.gameType == GameType.Visual || gameState.gameType == GameType.AudioVisual
    val isAudioMatchEnabled =
        gameState.gameType == GameType.Audio || gameState.gameType == GameType.AudioVisual

    BackHandler(enabled = true) {
        showDialog = true
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                showDialog = false
            },
            title = { Text("End Game?") },
            text = { Text("Are you sure you want to quit? Your current score will be lost.") },
            confirmButton = {
                Button(
                    onClick = {
                        showDialog = false
                        vm.stopGame()
                        onEndGame()
                    }
                ) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        showDialog = false
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround

    ) {
        Text(
            text = "Score: $currentScore",
            style = MaterialTheme.typography.headlineMedium
        )

        GameBoard(
            gameState = gameState, modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .padding(16.dp)
        )

//        Button(onClick = vm::startGame) {
//            Text(text = "Generate eventValues")
//        }

        Row {
            Button(
                onClick = { vm.checkMatch() },
                enabled = isVisualMatchEnabled
            ) {
                Text("Position Match")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(
                onClick = { vm.checkMatch() },
                enabled = isAudioMatchEnabled
            ) {
                Text("Audio Match")
            }
        }
    }
}

@Preview
@Composable
fun GameScreenPreview() {
    Surface {
        GameScreen(
            FakeVM(),
            onEndGame = {}
        )
    }
}
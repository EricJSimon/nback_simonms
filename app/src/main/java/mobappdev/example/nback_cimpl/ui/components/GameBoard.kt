package mobappdev.example.nback_cimpl.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import mobappdev.example.nback_cimpl.ui.viewmodels.GameState

/**
 *
 * This is the gameboard layout.
 * It is implemented to any screen
 * that needs to display it.
 *
 * The cells also have an active state
 * and changes color depending if it is active or not.
 *
 * (Yello = isActive,
 *  Gray = isNotActive)
 *
 * @param modifier: Modifier
 * @param size: Int
 * @param gameState: GameState
 *
 * date: 2025-11-06
 * @author Simonms
 *
 */
@Composable
fun GameBoard(
    modifier: Modifier = Modifier,
    size: Int = 3,
    gameState: GameState
) {
    var isCellVisible by remember { mutableStateOf(true) }

    LaunchedEffect(key1 = gameState.tick) {
        if (gameState.eventValue != -1) {
            isCellVisible = true
            delay(500L)
            isCellVisible = false
        }
    }

    Box(modifier = modifier) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            repeat(size) { rowIndex ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    repeat(size) { colIndex ->
                        val cellIndex = rowIndex * size + colIndex
                        val isCellActive = (cellIndex == gameState.eventValue - 1)

                        GridCell(
                            isActive = isCellActive,
                            visible = isCellVisible
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun GridCell(
    isActive: Boolean,
    visible: Boolean,
    modifier: Modifier = Modifier
) {
    val cellColor = if (isActive && visible) Color.Yellow else Color.LightGray

    Box(
        modifier = modifier
            .size(80.dp)
            .background(cellColor)
            .border(2.dp, Color.Gray)
    )
}

@Preview
@Composable
fun GameBoardPreview() {
    GameBoard(gameState = GameState(eventValue = 4))
}
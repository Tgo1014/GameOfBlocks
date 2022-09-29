package tgo1014.gameofblocks.game

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import tgo1014.gameofblocks.ui.theme.GameOfBlocksTheme

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun GameScreen(viewModel: GameViewModel = viewModel()) {
    val gameState by viewModel.gameStateFlow.collectAsStateWithLifecycle()
    GameScreen(
        grid = gameState.grid,
        score = gameState.finalScore,
        onItemClicked = viewModel::onGridTouched
    )
}

@Composable
private fun GameScreen(
    grid: Array<Array<Game.GridItem>>,
    score: Int,
    onItemClicked: (x: Int, y: Int) -> Unit = { _, _ -> },
) = Column(
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier
        .background(MaterialTheme.colorScheme.primaryContainer)
        .fillMaxSize(),
) {
    Row {
        grid.forEachIndexed { x, gridItems ->
            Column {
                gridItems.forEachIndexed { y, item ->
                    val boxColor =
                        if (!item.painted) Color.Transparent else MaterialTheme.colorScheme.primary
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(50.dp)
                            .border(1.dp, MaterialTheme.colorScheme.onSurface)
                            .background(boxColor)
                            .clickable { onItemClicked(x, y) }
                    ) {
                        if (item.points > 0) {
                            Text(
                                text = "${item.points}",
                                color = contentColorFor(backgroundColor = boxColor)
                            )
                        }
                    }
                }
            }
        }
    }
    AnimatedVisibility(score > 0) {
        Text(text = "Score: $score")
    }
}

private val previewGrid = Array(5) { Array(5) { Game.GridItem() } }

@Preview(showBackground = true)
@Composable
private fun GameScreenPreview() = GameOfBlocksTheme {
    GameScreen(previewGrid, 0)
}

@Preview(showBackground = true)
@Composable
private fun GameScreenPreviewWithScore() = GameOfBlocksTheme {
    GameScreen(previewGrid, 10)
}
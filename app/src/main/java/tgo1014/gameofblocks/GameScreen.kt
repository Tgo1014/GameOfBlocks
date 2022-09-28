package tgo1014.gameofblocks

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
fun GameScreen() {
    val gameViewModel = viewModel<GameViewModel>()
    val game = gameViewModel.game
    val grid by game.gameGrid.collectAsStateWithLifecycle()
    GameScreen(grid = grid, onItemClicked = gameViewModel::onGridTouched)
}

@Composable
private fun Grid(
    grid: Array<Array<Game.GridItem>>,
    modifier: Modifier = Modifier,
    onItemClicked: (x: Int, y: Int) -> Unit = { _, _ -> },
) {
    Row(modifier = modifier) {
        grid.forEachIndexed { x, gridItems ->
            Column {
                gridItems.forEachIndexed { y, item ->
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .border(1.dp, MaterialTheme.colorScheme.onSurface)
                            .background(if (!item.painted) Color.Transparent else MaterialTheme.colorScheme.primary)
                            .clickable { onItemClicked(x, y) }
                    ) {
                        Text("${item.points}")
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun GameScreenPreview() = GameOfBlocksTheme {
    GameScreen()
}
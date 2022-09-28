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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tgo1014.gameofblocks.ui.theme.GameOfBlocksTheme

@Composable
fun GameScreen() {
    val coroutineScope = rememberCoroutineScope()
    val game = remember { Game(gameScope = coroutineScope) } // todo state on color change
    val grid by game.gameGrid.collectAsState() // todo replace with collectAsStateWithLifecycle()
    Box(modifier = Modifier.background(MaterialTheme.colorScheme.primaryContainer)) {
        Grid(
            grid = grid,
            onItemClicked = game::onGridTouched,
            modifier = Modifier.align(Alignment.Center)
        )
    }
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
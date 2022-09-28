package tgo1014.gameofblocks

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class Game(
    private val width: Int = 5,
    private val height: Int = 5,
    private val maxMoves: Int = 10,
    private val gameScope: CoroutineScope,
) {

    private val _gameGrid = MutableStateFlow(emptyGrid())
    val gameGrid = _gameGrid.asStateFlow()

    private var isPlayable = true
    private var currentMoves = 0
    private val isGameOver get() = currentMoves >= maxMoves

    fun onGridTouched(x: Int, y: Int) {
        if (isGameOver) {
            reset()
            return
        }
        if (!isPlayable || _gameGrid.value[x][y].painted) {
            return
        }
        gameScope.launch {
            isPlayable = false
            currentMoves += 1
            _gameGrid.update { grid ->
                grid[x][y] = grid[x][y].copy(painted = true)
                grid.copyOf()
            }
            applyGravity(x, y)
            isPlayable = true
            checkGameOver()
        }
    }

    private suspend fun applyGravity(x: Int, y: Int) {
        gameScope.async {
            // out of bounds
            if (y == height - 1) {
                return@async
            }
            val yBellow = y + 1
            val gameGrid = _gameGrid.value
            // top of another block
            if (gameGrid[x][yBellow].painted) {
                return@async
            }
            // bridge
            if (gameGrid.getOrNull(x - 1)?.get(y)?.painted == true
                && gameGrid.getOrNull(x + 1)?.get(y)?.painted == true
            ) {
                return@async
            }
            _gameGrid.update { grid ->
                grid[x][y] = grid[x][y].copy(painted = false)
                grid[x][yBellow] = grid[x][y].copy(painted = true)
                grid.copyOf()
            }
            delay(100)
            applyGravity(x, yBellow)
        }.await()
    }

    private fun checkGameOver() {
        if (!isGameOver) return
        calculateScore()
    }

    private fun calculateScore(
        x: Int = height - 1,
        y: Int = width - 1,
        movesCounted: Int = 0,
    ) {
        if (movesCounted == maxMoves) return
        if (x < 0 || y < 0) return
        if (_gameGrid.value[x][y].points > 0) return
        var countedMoves = movesCounted
        if (_gameGrid.value[x][y].painted) {
            val yUnder = y + 1
            _gameGrid.update { grid ->
                grid[x][y] = grid[x][y].copy(
                    points = if (grid[x].getOrNull(yUnder)?.painted == false) {
                        5
                    } else {
                        (grid[x].getOrNull(yUnder)?.points ?: 0) + 5
                    }
                )
                grid.copyOf()
            }
            countedMoves += 1
        } else {
            // Check if there's a block on top of the space
            for (spaceY in y - 1 downTo 0) {
                if (_gameGrid.value[x][spaceY].painted) {
                    _gameGrid.update { grid ->
                        grid[x][y] = grid[x][y].copy(points = 10)
                        grid.copyOf()
                    }
                    break
                }
            }
        }
        calculateScore(x - 1, y, countedMoves)
        calculateScore(x, y - 1, countedMoves)
    }

    private fun reset() {
        currentMoves = 0
        _gameGrid.update { emptyGrid() }
    }

    private fun emptyGrid() = Array(height) { Array(width) { GridItem() } }

    data class GameState(
        val movesLeft: Int
    )

    enum class State {
        PLAYING, GAME_ENDED
    }

    data class GridItem(val painted: Boolean = false, val points: Int = 0)

}
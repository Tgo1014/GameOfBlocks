package tgo1014.gameofblocks

import app.cash.turbine.testIn
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test
import tgo1014.gameofblocks.game.Game


@OptIn(ExperimentalCoroutinesApi::class)
class GameTest {

    @Test
    fun `GIVEN game create WHEN getting grid THEN size same as creation`() = runTest {
        for (height in 1..5) {
            for (width in 1..3) {
                val game = Game(height = height, width = width, gameScope = this)
                val gridState = game.gameGrid.testIn(this)
                val grid = gridState.awaitItem()
                assert(grid.size == height)
                assert(grid.first().size == width)
                gridState.cancel()
            }
        }
    }

    @Test
    fun `GIVEN invalid constructor values WHEN creating game object THEN crashes`() = runTest {
        val heightResult = runCatching { Game(height = 0, gameScope = this) }
        assert(heightResult.isFailure)
        val widthResult = runCatching { Game(width = 0, gameScope = this) }
        assert(widthResult.isFailure)
        val maxMovesResult = runCatching { Game(maxMoves = 0, gameScope = this) }
        assert(maxMovesResult.isFailure)
    }

    @Test
    fun `GIVEN game started WHEN bottom of the grid clicked THEN item is set as painted`() {
        runTest {
            val game = Game(height = 2, width = 2, gameScope = this)
            val gameState = game.gameGrid.testIn(this)
            var grid = gameState.awaitItem()
            assert(!grid[1][1].painted)
            game.onGridTouched(1, 1)
            grid = gameState.awaitItem()
            assert(grid[1][1].painted)
            gameState.expectNoEvents()
            gameState.cancel()
        }
    }

    @Test
    fun `GIVEN game started WHEN all moves used THEN clicking resets grid`() {
        runTest {
            val game = Game(height = 2, width = 2, maxMoves = 2, gameScope = this)
            val gameState = game.gameGrid.testIn(this)
            game.onGridTouched(0, 1)
            game.onGridTouched(1, 1)
            var grid = gameState.awaitItem()
            assert(grid[0][1].painted)
            assert(grid[1][1].painted)
            game.onGridTouched(0, 1)
            grid = gameState.expectMostRecentItem()
            assert(!grid[0][1].painted)
            assert(!grid[1][1].painted)
            gameState.cancel()
        }
    }

}
package tgo1014.gameofblocks.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

class GameViewModel : ViewModel() {
    private val game = Game(gameScope = viewModelScope)
    val gameState = game.gameState
    fun onGridTouched(x: Int, y: Int) = game.onGridTouched(x, y)
}
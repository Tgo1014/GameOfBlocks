package tgo1014.gameofblocks.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

class GameViewModel : ViewModel() {
    val game = Game(gameScope = viewModelScope)
    fun onGridTouched(x: Int, y: Int) = game.onGridTouched(x, y)
}
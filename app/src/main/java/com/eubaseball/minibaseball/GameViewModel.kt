package com.eubaseball.minibaseball

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GameViewModel(private val repository: GameRepository) : ViewModel() {
    private val _guessResult = MutableLiveData<Pair<Int, Int>>()
    val guessResult: LiveData<Pair<Int, Int>> = _guessResult

    fun startGame() {
        val target = repository.generateTarget()
        // 뷰 업데이트...
    }

    fun makeGuess(guess: String, target: String) {
        val result = repository.checkGuess(guess, target)
        _guessResult.value = result
    }
}
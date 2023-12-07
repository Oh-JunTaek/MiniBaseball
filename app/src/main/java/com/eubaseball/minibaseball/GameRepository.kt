package com.eubaseball.minibaseball

interface GameRepository {
    fun generateTarget(): String
    fun checkGuess(guess: String, target: String): Pair<Int, Int>

}
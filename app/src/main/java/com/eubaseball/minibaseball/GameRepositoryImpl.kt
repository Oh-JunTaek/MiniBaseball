package com.eubaseball.minibaseball

class GameRepositoryImpl : GameRepository {

    override fun generateTarget(): String {
        val numbers = (1..9).shuffled().take(3)
        return numbers.joinToString("")
    }

    override fun checkGuess(guess: String, target: String): Pair<Int, Int> {
        var strikes = 0
        var balls = 0

        for (i in guess.indices) {
            if (guess[i] == target[i]) {
                strikes++
            } else if (target.contains(guess[i])) {
                balls++
            }
        }

        return Pair(strikes, balls)
    }
}
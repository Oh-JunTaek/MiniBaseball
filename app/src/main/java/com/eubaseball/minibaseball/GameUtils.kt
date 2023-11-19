package com.eubaseball.minibaseball

object GameUtils {
    fun generateTarget(): String {
        val numbers = (1..9).shuffled().take(3)
        return numbers.joinToString("")
    }

    fun checkGuess(guess: String, target: String): Pair<Int, Int> {
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

    fun getImageResource(number: Int): Int {
        return when (number) {
            1 -> R.drawable.btn1
            2 -> R.drawable.btn2
            3 -> R.drawable.btn3
            4 -> R.drawable.btn4
            5 -> R.drawable.btn5
            6 -> R.drawable.btn6
            7 -> R.drawable.btn7
            8 -> R.drawable.btn8
            9 -> R.drawable.btn9
            else -> R.drawable.btnquestion
        }
    }
}
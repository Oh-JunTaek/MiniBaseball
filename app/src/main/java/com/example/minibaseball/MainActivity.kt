package com.example.minibaseball

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.minibaseball.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var inputNumber = mutableListOf<Int?>(null, null, null)
    private val target = generateTarget()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 각 버튼 클릭 이벤트 설정
        listOf(
            binding.btn1, binding.btn2, binding.btn3,
            binding.btn4, binding.btn5, binding.btn6,
            binding.btn7, binding.btn8, binding.btn9
        ).forEachIndexed { index, button ->
            button.setOnClickListener {
                addNumberToInput(index + 1)
            }
        }

        // '확인' 버튼 클릭 이벤트 설정
        binding.btnok.setOnClickListener {
            val userInput = inputNumber.joinToString("")

            if (userInput.length == 3) { // 입력된 숫자가 3개인 경우에만 처리합니다.
                val result = checkGuess(userInput, target)

                // answer TextView 업데이트
                binding.answer.text =
                    "너가 말한 숫자\n${userInput}은\n${result.second}볼 ${result.first}스트라이크"

                // board TextView 업데이트
                val prevText = if (binding.board.text.isEmpty()) "" else "${binding.board.text}\n"
                binding.board.text = "$prevText${userInput}: ${result.second}볼 ${result.first}스트라이크"

                // 입력 초기화
                inputNumber.fill(null)
                listOf(
                    binding.a1,
                    binding.a2,
                    binding.a3
                ).forEach { it.setImageResource(R.drawable.baseline_question_mark_24) }
            }
        }
    }

            private fun addNumberToInput(number: Int) {
                // 첫 번째 빈 위치 찾기
                val position = inputNumber.indexOf(null)
                if (position != -1) {
                    inputNumber[position] = number

                    // 적절한 이미지로 업데이트하기 (여기서는 단순히 숫자로 표현함)
                    when (position) {
                        0 -> {
                            binding.a1.setImageResource(getImageResource(number)) }

                        1 -> {
                            binding.a2.setImageResource(getImageResource(number)) }

                        2 -> {
                            binding.a3.setImageResource(getImageResource(number)) }
                    }
                }
            }

            private fun getImageResource(number: Int): Int {
                return when (number) {
                    //...
                    else -> R.drawable.baseline_question_mark_24
                }
            }
        private fun generateTarget(): String {
            return (100..999).random().toString()
        }

        private fun checkGuess(guess: String, target: String): Pair<Int, Int> {
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
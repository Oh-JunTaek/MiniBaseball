package com.example.minibaseball

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
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
                val userInputText = SpannableString("너가 말한 숫자\n${userInput}\n의 결과는")
                userInputText.setSpan(ForegroundColorSpan(Color.BLACK), 0, userInputText.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

                val ballInfo = SpannableString("\n${result.second}볼")
                ballInfo.setSpan(ForegroundColorSpan(Color.GREEN), 0, ballInfo.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

                val strikeInfo = SpannableString(" ${result.first}스트라이크")
                strikeInfo.setSpan(ForegroundColorSpan(Color.YELLOW), 0, strikeInfo.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

                binding.answer.text = TextUtils.concat(userInputText, ballInfo, strikeInfo)

                // board TextView 업데이트
                val userInputTextBoard = SpannableString("${userInput}: ")
                userInputTextBoard.setSpan(ForegroundColorSpan(Color.WHITE), 0, userInputTextBoard.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

                val ballInfoBoard = SpannableString("${result.second}볼")
                ballInfoBoard.setSpan(ForegroundColorSpan(Color.GREEN), 0, ballInfoBoard.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

                val strikeInfoBoard = SpannableString(" ${result.first}스트라이크")
                strikeInfoBoard.setSpan(ForegroundColorSpan(Color.YELLOW), 0,
                    strikeInfoBoard.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )

                val prevText =
                    if (binding.boardText.text.isEmpty()) "" else "${binding.boardText.text}\n"
                binding.boardText.text =
                    TextUtils.concat(prevText,userInputTextBoard ,ballInfoBoard ,strikeInfoBoard)

                // 입력 초기화
                inputNumber.fill(null)
                listOf(
                    binding.a1,
                    binding.a2,
                    binding.a3
                ).forEach { it.setImageResource(R.drawable.baseline_question_mark_24) }
            }
        }
//delete 버튼에 대한 기능 설정
        binding.btndelete.setOnClickListener {
            // 입력값 초기화
            inputNumber.fill(null)

            // 이미지 리소스 초기화
            listOf(
                binding.a1,
                binding.a2,
                binding.a3
            ).forEach { it.setImageResource(R.drawable.baseline_question_mark_24) }
        }
    }
//입력된 값 표기
            private fun addNumberToInput(number: Int) {
                // 첫 번째 빈 위치 찾기
                val position = inputNumber.indexOf(null)
                if (position != -1) {
                    inputNumber[position] = number

                    // 이미지 불러오기(하단 getImageResource참조)
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

            private fun getImageResource(number: Int): Int {//이미지 불러오기
                val resourceName = "baseline_filter_${number}_24"
                return resources.getIdentifier(resourceName, "drawable", packageName)

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


package com.eubaseball.minibaseball

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.eubaseball.minibaseball.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var inputNumber = mutableListOf<Int?>(null, null, null)
    private var target = generateTarget()
    private var tries = 0
    private var recordCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupButtons()
    }

    private fun setupButtons() {
        setupNumberButtons()
        setupOkButton()
        setupDeleteButton()
    }

    private fun setupNumberButtons() {
        listOf(
            binding.btn1, binding.btn2, binding.btn3,
            binding.btn4, binding.btn5, binding.btn6,
            binding.btn7, binding.btn8, binding.btn9
        ).forEachIndexed { index, button ->
            button.setOnClickListener {
                addNumberToInput(index + 1)
            }
        }
    }

    private fun setupOkButton() {
        binding.btnok.setOnClickListener {
            handleGuess()
        }
    }

    private fun setupDeleteButton() {
        binding.btndelete.setOnClickListener {
            resetInput()
        }
    }

    private fun handleGuess() {
        val userInput = inputNumber.joinToString("")
        if (userInput.length == 3) {
            tries++
            recordCount++
            val result = checkGuess(userInput, target)
            updateAnswerText(userInput, result)
            updateBoardText(userInput, result)
            resetInput()
            if (result.first == 3) {
                showGameEndDialog()
            }
        }
    }

    private fun updateAnswerText(userInput: String, result: Pair<Int, Int>) {
        val userInputText = SpannableString("너가 말한 숫자\n${userInput}\n의 결과는")
        userInputText.setSpan(
            ForegroundColorSpan(Color.BLACK),
            0,
            userInputText.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        val ballInfo = SpannableString("\n${result.second}볼")
        ballInfo.setSpan(
            ForegroundColorSpan(Color.GREEN),
            0,
            ballInfo.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        val strikeInfo = SpannableString(" ${result.first}스트라이크")
        strikeInfo.setSpan(
            ForegroundColorSpan(Color.YELLOW),
            0,
            strikeInfo.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        binding.answer.text = TextUtils.concat(userInputText, ballInfo, strikeInfo)
    }

    private fun updateBoardText(userInput: String, result: Pair<Int, Int>) {
        val boardTextBuilder = if (binding.boardText.text is SpannableStringBuilder) {
            binding.boardText.text as SpannableStringBuilder
        } else {
            SpannableStringBuilder(binding.boardText.text)
        }

        val userInputTextBoard = SpannableString("${userInput}: ")
        userInputTextBoard.setSpan(
            ForegroundColorSpan(Color.WHITE),
            0,
            userInputTextBoard.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        boardTextBuilder.append(userInputTextBoard)

        val ballInfoBoard = SpannableString("${result.second}볼 ")
        ballInfoBoard.setSpan(
            ForegroundColorSpan(Color.GREEN),
            0,
            ballInfoBoard.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        boardTextBuilder.append(ballInfoBoard)

        val strikeInfoBoard = SpannableString(" ${result.first}스트라이크")
        strikeInfoBoard.setSpan(
            ForegroundColorSpan(Color.YELLOW),
            0,
            strikeInfoBoard.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        boardTextBuilder.append(strikeInfoBoard)

        if (recordCount % 1 == 0) {
            boardTextBuilder.append("\n")
        }

        binding.boardText.text = boardTextBuilder
        scrollToBottom()
    }

    private fun resetInput() {
        inputNumber.fill(null)
        listOf(
            binding.a1,
            binding.a2,
            binding.a3
        ).forEach { it.setImageResource(R.drawable.btnquestion) }
    }

    private fun scrollToBottom() {
        binding.board.fullScroll(View.FOCUS_DOWN)
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
                    binding.a1.setImageResource(getImageResource(number))
                }

                1 -> {
                    binding.a2.setImageResource(getImageResource(number))
                }

                2 -> {
                    binding.a3.setImageResource(getImageResource(number))
                }
            }
        }
    }

    private fun getImageResource(number: Int): Int {
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

    private fun showGameEndDialog() {
        AlertDialog.Builder(this)
            .setTitle("축하합니다!")
            .setMessage("축하합니다. ${tries}회 만에 맞추셨어요.")
            .setPositiveButton("새 게임") { _, _ ->
                resetGame()
            }
            .setNegativeButton("메인 화면") { _, _ ->
                goToSplashActivity()
            }.show()
    }

    private fun resetGame() {
        target = generateTarget()
        tries = 0
        inputNumber.fill(null)

        listOf(
            binding.a1,
            binding.a2,
            binding.a3
        ).forEach { it.setImageResource(R.drawable.baseline_question_mark_24) }
    }

    private fun goToSplashActivity() {
        val intent = Intent(this, SplashActivity::class.java)
        startActivity(intent)
    }

    private fun generateTarget(): String {
        val numbers = (1..9).shuffled().take(3)
        return numbers.joinToString("")
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
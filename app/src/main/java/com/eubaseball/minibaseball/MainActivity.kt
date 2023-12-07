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
import androidx.lifecycle.lifecycleScope
import com.eubaseball.minibaseball.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var gameSessionManager: GameSessionManager
    private var inputNumber = mutableListOf<Int?>(null, null, null)
    private var target = GameUtils.generateTarget()
    private var recordCount = 0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        gameSessionManager = GameSessionManager(this)

        setupButtons()
    }

    private fun setupButtons() {
        setupNumberButtons()
        setupOkButton()
        setupDeleteButton()
    }//숫자,확인,삭제 버튼 설정 함수 호출

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
    }//숫자 버튼

    private fun setupOkButton() {
        binding.btnok.setOnClickListener {
            handleGuess()
        }
    }//확인 버튼

    private fun setupDeleteButton() {
        binding.btndelete.setOnClickListener {
            resetInput()
        }
    }//삭제 버튼

    private fun handleGuess() {
        val userInput = inputNumber.joinToString("")
        if (userInput.length == 3) {
            gameSessionManager.tries++
            recordCount++
            val result = GameUtils.checkGuess(userInput, target)
            updateAnswerText(userInput, result)
            updateBoardText(userInput, result)
            resetInput()
            if (result.first == 3) {
                showGameEndDialog()
            }
        }
    }//사용자 추측 처리, 결과 화면 표출

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
    }//추측 결과 표출

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

        boardTextBuilder.append("\n")

        binding.boardText.text = boardTextBuilder
        scrollToBottom()
    }//결과판

    private fun resetInput() {
        inputNumber.fill(null)
        listOf(
            binding.a1,
            binding.a2,
            binding.a3
        ).forEach { it.setImageResource(R.drawable.btnquestion) }
    }//입력 초기화

    private fun scrollToBottom() {
        binding.board.fullScroll(View.FOCUS_DOWN)
    }//스크롤바

    private fun addNumberToInput(number: Int) {
        val position = inputNumber.indexOf(null)
        if (position != -1) {
            inputNumber[position] = number

            when (position) {
                0 -> {
                    binding.a1.setImageResource(GameUtils.getImageResource(number))
                }

                1 -> {
                    binding.a2.setImageResource(GameUtils.getImageResource(number))
                }

                2 -> {
                    binding.a3.setImageResource(GameUtils.getImageResource(number))
                }
            }
        }
    }//입력된 숫자 추가

    private fun showGameEndDialog() {
        gameSessionManager.playCount++

        AlertDialog.Builder(this)
            .setTitle("축하합니다!")
            .setMessage("축하합니다. ${gameSessionManager.tries}회 만에 맞추셨어요.")
            .setPositiveButton("새 게임") { _, _ ->
                lifecycleScope.launch {
                    Achievements.checkAchievements(gameSessionManager.tries, gameSessionManager.playCount)
                }
                resetGame()
            }
            .setNegativeButton("메인 화면") { _, _ ->
                goToSplashActivity()
            }.show()
    }

    private fun resetGame() {
        target = GameUtils.generateTarget()
        gameSessionManager.tries = 0
        inputNumber.fill(null)

        binding.boardText.text = ""

        listOf(
            binding.a1,
            binding.a2,
            binding.a3
        ).forEach { it.setImageResource(R.drawable.btnquestion) }
    }//게임 초기화

    private fun goToSplashActivity() {
        val intent = Intent(this, SplashActivity::class.java)
        startActivity(intent)
    }
}
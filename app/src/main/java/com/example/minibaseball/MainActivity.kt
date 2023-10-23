package com.example.minibaseball

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.minibaseball.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val userInput = "123" // 사용자 입력 값을 가져옵니다.
        val result = checkGuess(userInput, target) // 'target'은 생성된 목표 숫자입니다.

        // answer TextView 업데이트
        binding.answer.text = "너가 말한 숫자\n${userInput}은\n${result.second}볼 ${result.first}스트라이크"

        // board TextView 업데이트
        binding.board.text = "${userInput}: ${result.second}볼 ${result.first}스트라이크"
    }
}

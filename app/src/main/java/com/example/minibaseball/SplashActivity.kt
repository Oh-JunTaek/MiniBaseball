package com.example.minibaseball

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.minibaseball.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.startButton.setOnClickListener {
            // 게임 시작 로직 구현
        }

        binding.howToPlayButton.setOnClickListener {
            // 플레이 방법 로직 구현
        }

        binding.infoButton.setOnClickListener {
            // 정보 보기 로직 구현
        }
    }
}
package com.eubaseball.minibaseball

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.eubaseball.minibaseball.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.startButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        binding.howToPlayButton.setOnClickListener {
            val howToPlayMessage = """
        숫자 야구 게임은 플레이어가 상대방의 숫자를 맞추는 게임입니다. 
        이 게임은 다음과 같이 진행됩니다.

        1. 컴퓨터는 중복되지 않는 3자리의 무작위 숫자를 생성합니다.
        2. 플레이어는 자신이 생각하는 3자리의 숫자를 입력합니다.
        3. 컴퓨터는 플레이어가 제시한 숫자에 대해 스트라이크와 볼 판정을 합니다.
           - 스트라이크: 자릿수와 값 모두 정확하게 맞춘 경우
           - 볼: 값은 맞지만 자릿수가 틀린 경우
        4. 컴퓨터의 숫자를 완전히 맞출 때까지 (즉, 3스트라이크일 때까지) 위 과정을 반복합니다.
        예를 들어, 컴퓨터가 생각한 숫자가 "123"일 때,
        플레이어가 "123"을 제시하면 결과는 3스트라이크입니다(완전히 맞춘 경우).
        플레이어가 "132"을 제시하면 결과는 1스트라이크 2볼입니다(값은 모두 맞았지만, 일부 자릿수가 틀린 경우).
        플레이어가 "456"을 제시하면 결과는 아웃입니다(아무것도 맞추지 못한 경우).
        

        몇 라운드 연습 후에 기본적인 전략과 규칙을 이해하는 데 도움될 것입니다.
    """.trimIndent()

            AlertDialog.Builder(this)
                .setTitle("How to Play")
                .setMessage(howToPlayMessage)
                .setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }

        binding.infoButton.setOnClickListener {
            val packageInfo = packageManager.getPackageInfo(packageName, 0)
            val versionInfo = packageInfo.versionName
            val message = """
        개발자 이메일 : wns5388@naver.com
        버전 정보 : $versionInfo
        후원 계좌 : 카카오뱅크 3333-11-4621655
    """.trimIndent()

            AlertDialog.Builder(this)
                .setTitle("정보")
                .setMessage(message)
                .setPositiveButton("광고보기") { dialog, which ->
                    Toast.makeText(this, "미구현 기능입니다.", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                    // 광고 보기 버튼 클릭 시 동작 구현
                    // 예: 광고 보여주는 함수 호출 등
                    dialog.dismiss()
                }
                .setNegativeButton("닫기") { dialog, which ->
                    // 닫기 버튼 클릭 시 동작 (여기서는 창 닫기만 함)
                    dialog.dismiss()
                }
                .show()
        }
    }
}
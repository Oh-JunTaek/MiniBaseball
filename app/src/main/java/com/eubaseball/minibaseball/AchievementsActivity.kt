package com.eubaseball.minibaseball

import android.content.Context
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Date
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.eubaseball.minibaseball.databinding.ActivityAchievementsBinding
import com.eubaseball.minibaseball.databinding.AchievementItemBinding
import kotlinx.coroutines.launch


class AchievementsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAchievementsBinding
    private lateinit var achievementsRepository: AchievementsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAchievementsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Achievements.initialize(this)
        achievementsRepository = AchievementsRepository(this)

        lifecycleScope.launch {
            val achievementsAdapter = AchievementsAdapter(achievementsRepository.getAll())
            binding.recyclerView.adapter = achievementsAdapter
        }
    }
}

class AchievementsRepository(context: Context) {
    private val achievementDao = AppDatabase.getDatabase(context).achievementDao()
    suspend fun getAll(): List<Achievement> {
        return withContext(Dispatchers.IO) {
            achievementDao.getAll()
        }
    }
    suspend fun unlock(achievement: Achievement) {
        withContext(Dispatchers.IO) {
            achievementDao.insert(achievement)
        }
    }
}

class AchievementsAdapter(private val achievements: List<Achievement>) :
      RecyclerView.Adapter<AchievementsAdapter.ViewHolder>() {

            class ViewHolder(private val binding: AchievementItemBinding) :
                RecyclerView.ViewHolder(binding.root) {
                fun bind(achievement: Achievement) {
                    binding.tvTitle.text = achievement.id
                    binding.tvDescription.text = achievement.description
                }
            }

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
                val binding =
                    AchievementItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                return ViewHolder(binding)
            }

            override fun onBindViewHolder(holder: ViewHolder, position: Int) {
                holder.bind(achievements[position])
            }

            override fun getItemCount() = achievements.size
        }

        @Entity
        data class Achievement(
            @PrimaryKey val id: String,
            val description: String,
            val unlockedAt: Date
        )

object Achievements {
    private val achievements = mutableSetOf<Achievement>()
    private lateinit var achievementsRepository: AchievementsRepository

    fun initialize(context: Context) {
        achievementsRepository = AchievementsRepository(context)
    }

    suspend fun unlock(achievement: Achievement) {
        withContext(Dispatchers.IO) {
            achievements.add(achievement)
            achievementsRepository.unlock(achievement)
        }
    }
    fun isUnlocked(id: String): Boolean {
        return achievements.any { it.id == id }
    }

    suspend fun checkAchievements(tries: Int, playCount: Int) {
        if (tries == 1 && !isUnlocked("Lucky Player")) {
            unlock(Achievement("Lucky Player", "1회만에 게임을 클리어했습니다.", Date()))
        }
        if (playCount == 1 && !isUnlocked("Wellcome")) {
            unlock(Achievement("Wellcome", "처음으로 게임을 플레이했습니다.", Date()))
        }
        // 여기에 다른 업적을 체크하는 코드를 추가합니다.
    }
}
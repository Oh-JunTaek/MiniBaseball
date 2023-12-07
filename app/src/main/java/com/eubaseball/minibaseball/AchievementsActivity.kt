package com.eubaseball.minibaseball

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Date

@Entity
data class Achievement(
    @PrimaryKey val id: String,
    val description: String,
    val unlockedAt: Date
)

object Achievements {
    private val achievements = mutableSetOf<Achievement>()

    suspend fun unlock(achievement: Achievement) {
        withContext(Dispatchers.IO) {
            achievements.add(achievement)
            // TODO: AppDatabase의 인스턴스를 통해 AchievementDao를 얻고,
            //       이를 사용하여 achievement를 DB에 저장
        }
    }
    fun isUnlocked(id: String): Boolean {
        return achievements.any { it.id == id }
    }
}
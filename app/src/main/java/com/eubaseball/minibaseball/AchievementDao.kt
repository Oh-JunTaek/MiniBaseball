package com.eubaseball.minibaseball

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface AchievementDao {
    @Query("SELECT * FROM achievement")
    fun getAll(): List<Achievement>

    @Insert
    fun insert(achievement: Achievement)
}
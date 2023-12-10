package com.eubaseball.minibaseball

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.OnConflictStrategy

@Dao
interface AchievementDao {
    @Query("SELECT * FROM achievement")
    suspend fun getAll(): List<Achievement>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(achievement: Achievement)
}
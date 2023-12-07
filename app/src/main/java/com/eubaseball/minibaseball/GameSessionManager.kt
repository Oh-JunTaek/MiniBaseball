package com.eubaseball.minibaseball

import android.content.Context

class GameSessionManager(context: Context) {
    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    var playCount: Int
        get() = prefs.getInt(PLAY_COUNT_KEY, 0)
        set(value) = prefs.edit().putInt(PLAY_COUNT_KEY, value).apply()

    var tries: Int
        get() = prefs.getInt(TRIES_KEY, 0)
        set(value) = prefs.edit().putInt(TRIES_KEY, value).apply()

    companion object {
        private const val PREFS_NAME = "com.eubaseball.minibaseball.prefs"
        private const val PLAY_COUNT_KEY = "playCount"
        private const val TRIES_KEY = "tries"
    }
}
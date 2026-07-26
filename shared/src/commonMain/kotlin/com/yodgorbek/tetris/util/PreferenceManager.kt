package com.yodgorbek.tetris.util

import com.russhwolf.settings.Settings
import com.russhwolf.settings.set
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class HighScore(val score: Int, val date: Long)

class PreferenceManager(private val settings: Settings = Settings()) {

    private val json = Json { ignoreUnknownKeys = true }

    var isSoundEnabled: Boolean
        get() = settings.getBoolean("sound_enabled", true)
        set(value) { settings["sound_enabled"] = value }

    fun saveHighScore(score: Int) {
        val currentScores = getHighScores().toMutableList()
        currentScores.add(HighScore(score, 0L)) // Simplification: 0 for date
        val sortedScores = currentScores.sortedByDescending { it.score }.take(5)
        settings["high_scores"] = json.encodeToString(sortedScores)
    }

    fun getHighScores(): List<HighScore> {
        val jsonString = settings.getStringOrNull("high_scores") ?: return emptyList()
        return try {
            json.decodeFromString(jsonString)
        } catch (e: Exception) {
            emptyList()
        }
    }
}

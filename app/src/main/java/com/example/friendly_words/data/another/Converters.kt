package com.example.friendly_words.data.another

import androidx.room.TypeConverter
import com.example.friendly_words.therapist.ui.configuration.reinforcement.defaultPraiseMap

class Converters {

    @TypeConverter
    fun fromStringList(list: List<String>): String {
        return list.joinToString(separator = "||")
    }

    @TypeConverter
    fun toStringList(data: String): List<String> {
        return if (data.isBlank()) emptyList() else data.split("||")
    }

    @TypeConverter
    fun fromPraiseMap(map: Map<String, Boolean>): String =
        map.entries.joinToString(";;") { "${it.key}::${it.value}" }

    @TypeConverter
    fun toPraiseMap(data: String): Map<String, Boolean> {
        if (data.isBlank()) return defaultPraiseMap().mapValues { false }

        val parsed: Map<String, Boolean> = data.split(";;")
            .mapNotNull { entry ->
                val parts = entry.split("::")
                if (parts.size == 2) parts[0] to parts[1].toBooleanStrict() else null
            }.toMap()

        // scal z domyślną listą pochwał, upewniając się że mamy każdy klucz
        return defaultPraiseMap().mapValues { (key, _) -> parsed[key] ?: false }
    }
}
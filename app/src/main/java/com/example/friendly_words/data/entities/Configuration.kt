package com.example.friendly_words.data.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "configurations")
data class Configuration(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,

    val isActive: Boolean = false,
    val activeMode: String? = null,
    val isExample:Boolean=false,

//    @Embedded(prefix = "learning_")
//    val learningSettings: LearningSettings = LearningSettings(),
//
//    @Embedded(prefix = "test_")
//    val testSettings: TestSettings = TestSettings()
)

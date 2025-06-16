package com.example.friendly_words.data.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "configurations")
data class Configuration(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,

//    @Embedded(prefix = "learning_")
//    val learningSettings: LearningSettings = LearningSettings(),
//
//    @Embedded(prefix = "test_")
//    val testSettings: TestSettings = TestSettings()
)

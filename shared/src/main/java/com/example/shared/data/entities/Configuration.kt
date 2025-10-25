package com.example.shared.data.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.shared.data.another.ConfigurationMaterialState
import com.example.shared.data.another.ConfigurationReinforcementState

@Entity(tableName = "configurations")
data class Configuration(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val isActive: Boolean = false,
    val activeMode: String? = null,
    val isExample:Boolean=false,

    @Embedded(prefix = "learning_")
    val learningSettings: LearningSettings = LearningSettings(),

    @Embedded(prefix = "test_")
    val testSettings: TestSettings = TestSettings()
)

fun Configuration.toLearningSettings(
    materialState: ConfigurationMaterialState = ConfigurationMaterialState(emptyList()),
    reinforcementState: ConfigurationReinforcementState = ConfigurationReinforcementState()
): LearningSettings {
    val ls = this.learningSettings
    return LearningSettings(
        numberOfWords = materialState.vocabItems.size,
        displayedImagesCount = ls.displayedImagesCount,
        repetitionPerWord = ls.repetitionPerWord,
        commandType = ls.commandType,
        showLabelsUnderImages = ls.showLabelsUnderImages,
        readCommand = ls.readCommand,
        hintAfterSeconds = ls.hintAfterSeconds,
        typesOfHints = ls.typesOfHints,
        typesOfPraises = ls.typesOfPraises,
        animationsEnabled = ls.animationsEnabled
    )
}


package com.example.friendly_words.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "configurations")
data class Configuration(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
//    val repetitionsTest: Int,  // ilosc powtorzen dla kazdego slowa tescie
//    val repetitionsLearning: Int, // ilosc powtorzen dla kazdego slowa tescie
//    val numberOfImagesOnScreen: Int, //liczba zdjec wysweitlanych na ekranie dziecka (nie moze bycs wieksza od liczby dodanych slow)
//    val commandType: String, // rodzaj polecenia (moze byc jeszcze enum tutaj)
//    val showCaptions: Boolean, // czy podpisy pod obrazkami
//    val readCommand: Boolean, // czy czytanie polecenia
//    val hintAfterSeconds: Int, // pokazanie podpowiedzi X sekundach
//    val readPraiseAloud: Boolean, // czy czytanie glosowe pochwal
//    val totalNumberOfAttempts: Int, // laczna liczba prob (tryb testu)
//    val answerTimeSeconds: Int // czas na odpowiedz (tryb testu)

)

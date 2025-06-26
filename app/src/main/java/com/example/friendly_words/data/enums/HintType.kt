package com.example.friendly_words.data.enums

enum class HintType(val displayName: String){
    OMBRAMUJ_POPRAWNA("Obramuj poprawną"),
    PORUSZ_POPRAWNA("Porusz poprawną"),
    POWIEKSZ_POPRAWNA("Powiększ poprawną"),
    WYSZARZ_NIEPOPRAWNE("Wyszarz niepoprawne");

    override fun toString(): String = displayName
}
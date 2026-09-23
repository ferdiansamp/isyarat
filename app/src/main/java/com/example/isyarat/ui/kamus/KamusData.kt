package com.example.isyarat.ui.kamus

import com.example.isyarat.R

data class SignEntry(
    val kata: String,
    val imageRes: Int
)

fun getSignListFromDrawables(): List<SignEntry> {
    val fields = R.drawable::class.java.fields
    return fields
        .filter { it.name.startsWith("sign_") }
        .mapNotNull { field ->
            val resId = field.getInt(null)
            val kata = field.name
                .removePrefix("sign_")
                .replace("_", " ")
                .split(" ")
                .joinToString(" ") { word -> word.replaceFirstChar { it.uppercase() } }
            SignEntry(kata = kata, imageRes = resId)
        }
        .sortedBy { it.kata }
}
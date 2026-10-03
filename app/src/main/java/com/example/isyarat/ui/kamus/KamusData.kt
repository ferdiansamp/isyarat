package com.example.isyarat.ui.kamus

import com.example.isyarat.R

data class SignEntry(
    val kata: String,
    val imageRes: Int
)

enum class KamusCategory(val prefix: String, val label: String) {
    HURUF("huruf_", "Huruf"),
    KATA("kata_", "Kata Sehari-hari")
}

fun getSignListByCategory(category: KamusCategory): List<SignEntry> {
    val fields = R.drawable::class.java.fields
    return fields
        .filter { it.name.startsWith(category.prefix) }
        .mapNotNull { field ->
            val resId = field.getInt(null)
            val rawName = field.name.removePrefix(category.prefix).replace("_", " ")
            val kata = rawName.replaceFirstChar { it.uppercase() }
            SignEntry(kata = kata, imageRes = resId)
        }
        .sortedBy { it.kata }
}
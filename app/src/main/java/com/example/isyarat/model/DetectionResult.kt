package com.example.isyarat.model

data class DetectionResult(
    val label: String,
    val confidence: Float,
    val x1: Float,
    val y1: Float,
    val x2: Float,
    val y2: Float
)
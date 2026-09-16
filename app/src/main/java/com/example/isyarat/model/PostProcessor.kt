package com.example.isyarat.model

import com.example.isyarat.utils.Constants
import kotlin.math.max
import kotlin.math.min

object PostProcessor {

    fun process(
        output: Array<Array<FloatArray>>,
        labels: List<String>,
        confThreshold: Float = Constants.CONFIDENCE_THRESHOLD,
        iouThreshold: Float = Constants.IOU_THRESHOLD
    ): List<DetectionResult> {
        val numAttributes = output[0].size
        val numBoxes = output[0][0].size
        val numClasses = numAttributes - 4

        val candidates = mutableListOf<DetectionResult>()

        for (i in 0 until numBoxes) {
            var bestClassId = -1
            var bestScore = 0f

            for (c in 0 until numClasses) {
                val score = output[0][4 + c][i]
                if (score > bestScore) {
                    bestScore = score
                    bestClassId = c
                }
            }

            if (bestScore >= confThreshold && bestClassId in labels.indices) {
                val xCenter = output[0][0][i]
                val yCenter = output[0][1][i]
                val w = output[0][2][i]
                val h = output[0][3][i]

                candidates.add(
                    DetectionResult(
                        label = labels[bestClassId],
                        confidence = bestScore,
                        x1 = xCenter - w / 2f,
                        y1 = yCenter - h / 2f,
                        x2 = xCenter + w / 2f,
                        y2 = yCenter + h / 2f
                    )
                )
            }
        }

        return nonMaxSuppression(candidates, iouThreshold)
    }

    private fun nonMaxSuppression(
        boxes: List<DetectionResult>,
        iouThreshold: Float
    ): List<DetectionResult> {
        val sorted = boxes.sortedByDescending { it.confidence }.toMutableList()
        val result = mutableListOf<DetectionResult>()

        while (sorted.isNotEmpty()) {
            val best = sorted.removeAt(0)
            result.add(best)
            sorted.removeAll { iou(best, it) > iouThreshold }
        }

        return result
    }

    private fun iou(a: DetectionResult, b: DetectionResult): Float {
        val interX1 = max(a.x1, b.x1)
        val interY1 = max(a.y1, b.y1)
        val interX2 = min(a.x2, b.x2)
        val interY2 = min(a.y2, b.y2)

        val interArea = max(0f, interX2 - interX1) * max(0f, interY2 - interY1)
        val areaA = (a.x2 - a.x1) * (a.y2 - a.y1)
        val areaB = (b.x2 - b.x1) * (b.y2 - b.y1)

        return interArea / (areaA + areaB - interArea + 1e-6f)
    }
}
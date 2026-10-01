package com.example.isyarat.camera

import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.isyarat.model.DetectionResult
import com.example.isyarat.model.YoloDetector
import android.os.Handler
import android.os.Looper
@Composable
fun CameraScreen() {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var results by remember { mutableStateOf(emptyList<DetectionResult>()) }

    val mainHandler = remember { Handler(Looper.getMainLooper()) }
    val lastSeen = remember { longArrayOf(0L) }

    val detector = remember { YoloDetector(context) }
    val cameraManager = remember {
        CameraManager(lifecycleOwner, detector) { newResults ->
            mainHandler.post {
                val now = System.currentTimeMillis()
                if (newResults.isNotEmpty()) {
                    results = newResults
                    lastSeen[0] = now
                } else if (now - lastSeen[0] > 600) {
                    results = newResults   // kosongkan hanya kalau sudah >0,6 detik tidak ada tangan
                }
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose { cameraManager.stop() }
    }

    Box(
        modifier = Modifier.fillMaxSize().background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Box(modifier = Modifier.fillMaxWidth().aspectRatio(3f / 4f)) {
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { ctx ->
                    PreviewView(ctx).apply {
                        implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                        scaleType = PreviewView.ScaleType.FILL_CENTER
                        cameraManager.start(this)
                    }
                }
            )
            DetectionOverlay(results)

            Text(
                text = "hasil=${results.size} " +
                        (results.firstOrNull()?.let { "${it.label} ${(it.confidence * 100).toInt()}%" } ?: "-"),
                color = Color.Yellow,
                modifier = Modifier.align(Alignment.TopStart)
            )
        }
    }
}
@Composable
fun DetectionOverlay(results: List<DetectionResult>) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val textPaint = android.graphics.Paint().apply {
            color = android.graphics.Color.BLACK
            textSize = 44f
            isAntiAlias = true
        }

        for (r in results) {
            // koordinat 0..1 relatif terhadap frame
            val left = r.x1 * size.width
            val top = r.y1 * size.height
            val right = r.x2 * size.width
            val bottom = r.y2 * size.height

            drawRect(
                color = Color.Green,
                topLeft = Offset(left, top),
                size = Size(right - left, bottom - top),
                style = Stroke(width = 4.dp.toPx())
            )

            val text = "${r.label} ${(r.confidence * 100).toInt()}%"
            val textWidth = textPaint.measureText(text)
            val textTop = (top - 56f).coerceAtLeast(0f)

            drawRect(
                color = Color.Green,
                topLeft = Offset(left, textTop),
                size = Size(textWidth + 16f, 56f)
            )
            drawContext.canvas.nativeCanvas.drawText(text, left + 8f, textTop + 42f, textPaint)
        }
    }
}
package com.example.isyarat.camera

import android.util.Log
import android.util.Size
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.example.isyarat.model.DetectionResult
import com.example.isyarat.model.YoloDetector
import com.example.isyarat.utils.Constants
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraManager(
    private val lifecycleOwner: LifecycleOwner,
    private val yoloDetector: YoloDetector,
    private val onResult: (List<DetectionResult>) -> Unit
) {
    private val cameraExecutor: ExecutorService = Executors.newSingleThreadExecutor()
    private var cameraProvider: ProcessCameraProvider? = null

    @Volatile
    private var stopped = false

    fun start(previewView: PreviewView) {
        val context = previewView.context
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

        cameraProviderFuture.addListener({
            // kalau stop() sudah dipanggil sebelum kamera siap, jangan lanjut
            if (stopped) return@addListener

            val provider = cameraProviderFuture.get()
            cameraProvider = provider

            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }

            val imageAnalyzer = ImageAnalysis.Builder()
                .setTargetResolution(Size(Constants.INPUT_SIZE, Constants.INPUT_SIZE))
                .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888)
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also {
                    it.setAnalyzer(cameraExecutor) { imageProxy ->
                        try {
                            if (!stopped) {
                                val bitmap = ImageUtils.imageProxyToBitmap(imageProxy)
                                val results = yoloDetector.detect(bitmap)
                                Log.d("YOLO", "frame=${bitmap.width}x${bitmap.height} hasil=${results.size}")
                                results.firstOrNull()?.let { r ->
                                    Log.d("YOLO", "top=${r.label} conf=${r.confidence} box=${r.x1},${r.y1},${r.x2},${r.y2}")
                                }
                                onResult(results)
                            }
                        } finally {
                            imageProxy.close()
                        }
                    }
                }

            provider.unbindAll()
            provider.bindToLifecycle(
                lifecycleOwner, CameraSelector.DEFAULT_BACK_CAMERA, preview, imageAnalyzer
            )
        }, ContextCompat.getMainExecutor(context))
    }

    fun stop() {
        stopped = true
        cameraProvider?.unbindAll()
        cameraExecutor.shutdown()
        yoloDetector.close()
    }
}
package com.example.isyarat.camera

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
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraManager(
    private val lifecycleOwner: LifecycleOwner,
    private val yoloDetector: YoloDetector,
    private val onResult: (List<DetectionResult>) -> Unit
) {
    private val cameraExecutor: ExecutorService = Executors.newSingleThreadExecutor()

    fun start(previewView: PreviewView) {
        val context = previewView.context
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }

            val imageAnalyzer = ImageAnalysis.Builder()
                .setTargetResolution(Size(640, 640))
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also {
                    it.setAnalyzer(cameraExecutor) { imageProxy ->
                        try {
                            val bitmap = ImageUtils.imageProxyToBitmap(imageProxy)
                            val results = yoloDetector.detect(bitmap)
                            onResult(results)
                        } finally {
                            imageProxy.close()
                        }
                    }
                }

            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                lifecycleOwner, CameraSelector.DEFAULT_BACK_CAMERA, preview, imageAnalyzer
            )
        }, ContextCompat.getMainExecutor(context))
    }

    fun stop() {
        cameraExecutor.shutdown()
        yoloDetector.close()
    }
}
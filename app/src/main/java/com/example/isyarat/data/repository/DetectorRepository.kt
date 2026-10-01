package com.example.isyarat.data.repository

import android.content.Context
import androidx.camera.view.PreviewView
import androidx.lifecycle.LifecycleOwner
import com.example.isyarat.camera.CameraManager
import com.example.isyarat.model.DetectionResult
import com.example.isyarat.model.YoloDetector


class DetectorRepository(
    private val lifecycleOwner: LifecycleOwner,
    private val context: Context
) {
    private var cameraManager: CameraManager? = null

    fun startDetection(
        previewView: PreviewView,
        onResult: (List<DetectionResult>) -> Unit
    ) {
        val detector = YoloDetector(context)
        cameraManager = CameraManager(lifecycleOwner, detector, onResult)
        cameraManager?.start(previewView)
    }

    fun stopDetection() {
        cameraManager?.stop()
        cameraManager = null
    }
}
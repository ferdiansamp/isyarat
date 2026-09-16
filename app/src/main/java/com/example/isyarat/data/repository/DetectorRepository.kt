//package com.example.isyarat.data.repository
//
//import android.content.res.AssetManager
//import androidx.camera.view.PreviewView
//import androidx.lifecycle.LifecycleOwner
//import com.example.isyarat.camera.CameraManager
//import com.example.isyarat.model.DetectionResult
//import com.example.isyarat.model.YoloDetector
//
///**
// * Satu pintu masuk buat frontend.
// * Frontend cukup panggil startDetection(), tidak perlu tahu
// * detail CameraX atau TensorFlow Lite di baliknya.
// */
//class DetectorRepository(
//    private val lifecycleOwner: LifecycleOwner,
//    private val assetManager: AssetManager
//) {
//    private var cameraManager: CameraManager? = null
//
//    fun startDetection(
//        previewView: PreviewView,
//        onResult: (List<DetectionResult>) -> Unit
//    ) {
//        val detector = YoloDetector(assetManager)
//        cameraManager = CameraManager(lifecycleOwner, detector, onResult)
//        cameraManager?.start(previewView)
//    }
//
//    fun stopDetection() {
//        cameraManager?.stop()
//        cameraManager = null
//    }
//}
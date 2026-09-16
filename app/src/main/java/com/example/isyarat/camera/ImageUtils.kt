//package com.example.isyarat.camera
//
//import android.graphics.Bitmap
//import android.graphics.BitmapFactory
//import android.graphics.ImageFormat
//import android.graphics.Matrix
//import android.graphics.Rect
//import android.graphics.YuvImage
//import androidx.camera.core.ImageProxy
//import java.io.ByteArrayOutputStream
//
//object ImageUtils {
//
//    fun imageProxyToBitmap(imageProxy: ImageProxy): Bitmap {
//        val yBuffer = imageProxy.planes[0].buffer
//        val uBuffer = imageProxy.planes[1].buffer
//        val vBuffer = imageProxy.planes[2].buffer
//
//        val ySize = yBuffer.remaining()
//        val uSize = uBuffer.remaining()
//        val vSize = vBuffer.remaining()
//
//        val nv21 = ByteArray(ySize + uSize + vSize)
//        yBuffer.get(nv21, 0, ySize)
//        vBuffer.get(nv21, ySize, vSize)
//        uBuffer.get(nv21, ySize + vSize, uSize)
//
//        val yuvImage = YuvImage(nv21, ImageFormat.NV21, imageProxy.width, imageProxy.height, null)
//        val out = ByteArrayOutputStream()
//        yuvImage.compressToJpeg(Rect(0, 0, imageProxy.width, imageProxy.height), 100, out)
//        val imageBytes = out.toByteArray()
//
//        val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
//        return rotateBitmap(bitmap, imageProxy.imageInfo.rotationDegrees)
//    }
//
//    private fun rotateBitmap(bitmap: Bitmap, degrees: Int): Bitmap {
//        if (degrees == 0) return bitmap
//        val matrix = Matrix().apply { postRotate(degrees.toFloat()) }
//        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
//    }
//}
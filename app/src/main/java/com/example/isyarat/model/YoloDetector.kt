//package com.example.isyarat.model
//
//import android.content.res.AssetManager
//import android.graphics.Bitmap
//import com.example.isyarat.utils.Constants
//import org.tensorflow.lite.Interpreter
//import org.tensorflow.lite.support.common.FileUtil
//import java.nio.ByteBuffer
//import java.nio.ByteOrder
//
//class YoloDetector(
//    assetManager: AssetManager,
//    modelPath: String = Constants.MODEL_PATH,
//    labelsPath: String = Constants.LABELS_PATH
//) {
//    private val interpreter: Interpreter
//    private val labels: List<String>
//
//    init {
//        val model = FileUtil.loadMappedFile(assetManager, modelPath)
//        val options = Interpreter.Options().apply { numThreads = 4 }
//        interpreter = Interpreter(model, options)
//        labels = FileUtil.loadLabels(assetManager, labelsPath)
//    }
//
//    fun detect(bitmap: Bitmap): List<DetectionResult> {
//        val inputSize = Constants.INPUT_SIZE
//        val resized = Bitmap.createScaledBitmap(bitmap, inputSize, inputSize, true)
//        val inputBuffer = convertBitmapToByteBuffer(resized, inputSize)
//
//        // shape output YOLOv8n default: [1, 4 + numClasses, 8400]
//        // CEK ULANG shape ini pas model asli sudah di-export (pakai Netron atau print shape)
//        val numBoxes = 8400
//        val numAttributes = 4 + labels.size
//        val outputBuffer = Array(1) { Array(numAttributes) { FloatArray(numBoxes) } }
//
//        interpreter.run(inputBuffer, outputBuffer)
//
//        return PostProcessor.process(outputBuffer, labels)
//    }
//
//    private fun convertBitmapToByteBuffer(bitmap: Bitmap, inputSize: Int): ByteBuffer {
//        val byteBuffer = ByteBuffer.allocateDirect(4 * inputSize * inputSize * 3)
//        byteBuffer.order(ByteOrder.nativeOrder())
//
//        val pixels = IntArray(inputSize * inputSize)
//        bitmap.getPixels(pixels, 0, inputSize, 0, 0, inputSize, inputSize)
//
//        for (pixel in pixels) {
//            byteBuffer.putFloat((pixel shr 16 and 0xFF) / 255.0f) // R
//            byteBuffer.putFloat((pixel shr 8 and 0xFF) / 255.0f)  // G
//            byteBuffer.putFloat((pixel and 0xFF) / 255.0f)        // B
//        }
//
//        return byteBuffer
//    }
//
//    fun close() {
//        interpreter.close()
//    }
//}
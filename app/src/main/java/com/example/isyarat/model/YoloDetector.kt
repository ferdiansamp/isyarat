package com.example.isyarat.model

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.util.Log
import com.example.isyarat.utils.Constants
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

class YoloDetector(
    context: Context,
    modelPath: String = Constants.MODEL_PATH,
    labelsPath: String = Constants.LABELS_PATH
) {
    private val interpreter: Interpreter
    private val labels: List<String>

    @Volatile
    private var closed = false

    init {
        val model = loadModelFile(context, modelPath)
        val options = Interpreter.Options().apply { numThreads = 4 }
        interpreter = Interpreter(model, options)
        labels = context.assets.open(labelsPath).bufferedReader().readLines()
            .map { it.trim() }
            .filter { it.isNotEmpty() }

        val inTensor = interpreter.getInputTensor(0)
        val outTensor = interpreter.getOutputTensor(0)
        Log.d("YOLO", "input=${inTensor.shape().joinToString()} type=${inTensor.dataType()}")
        Log.d("YOLO", "output=${outTensor.shape().joinToString()} type=${outTensor.dataType()}")
        Log.d("YOLO", "jumlah label=${labels.size}")
    }

    private fun loadModelFile(context: Context, path: String): MappedByteBuffer {
        context.assets.openFd(path).use { fd ->
            FileInputStream(fd.fileDescriptor).use { input ->
                return input.channel.map(
                    FileChannel.MapMode.READ_ONLY,
                    fd.startOffset,
                    fd.declaredLength
                )
            }
        }
    }

    @Synchronized
    fun detect(bitmap: Bitmap): List<DetectionResult> {
        if (closed) return emptyList()

        val inputSize = Constants.INPUT_SIZE
        val resized = letterbox(bitmap, inputSize)
        //ngecek NHWC/NCHW , susunan RGB  , R1G1B1/ R1R2R3
        val channelsFirst = interpreter.getInputTensor(0).shape()[1] == 3
        //ngubah bitmap ke bytebuffer
        val inputBuffer = convertBitmapToByteBuffer(resized, inputSize, channelsFirst)

        //otomatis ngecek output
        val outputShape = interpreter.getOutputTensor(0).shape()
        val numAttributes = outputShape[1]
        val numBoxes = outputShape[2]
        val outputBuffer = Array(1) { Array(numAttributes) { FloatArray(numBoxes) } }

        interpreter.run(inputBuffer, outputBuffer)

        var maxScore = 0f
        for (a in 4 until numAttributes) {
            for (b in 0 until numBoxes) {
                maxScore = maxOf(maxScore, outputBuffer[0][a][b])
            }
        }
        Log.d("YOLO", "maxScore=$maxScore")

        val raw = PostProcessor.process(outputBuffer, labels)

        // petakan koordinat dari ruang letterbox 416x416 kembali ke frame asli (0..1)
        val size = inputSize.toFloat()
        val scale = minOf(size / bitmap.width, size / bitmap.height)
        val contentW = bitmap.width * scale
        val contentH = bitmap.height * scale
        val padX = (size - contentW) / 2f
        val padY = (size - contentH) / 2f

        fun toPx(v: Float) = if (v <= 2f) v * size else v

        return raw.map { r ->
            DetectionResult(
                label = r.label,
                confidence = r.confidence,
                x1 = ((toPx(r.x1) - padX) / contentW).coerceIn(0f, 1f),
                y1 = ((toPx(r.y1) - padY) / contentH).coerceIn(0f, 1f),
                x2 = ((toPx(r.x2) - padX) / contentW).coerceIn(0f, 1f),
                y2 = ((toPx(r.y2) - padY) / contentH).coerceIn(0f, 1f)
            )
        }
    }

    private fun letterbox(src: Bitmap, size: Int): Bitmap {
        val scale = minOf(size.toFloat() / src.width, size.toFloat() / src.height)
        val w = (src.width * scale).toInt()
        val h = (src.height * scale).toInt()
        val out = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(out)
        canvas.drawColor(Color.rgb(114, 114, 114))
        val scaled = Bitmap.createScaledBitmap(src, w, h, true)
        canvas.drawBitmap(scaled, (size - w) / 2f, (size - h) / 2f, null)
        return out
    }

    private fun convertBitmapToByteBuffer(
        bitmap: Bitmap,
        inputSize: Int,
        channelsFirst: Boolean
    ): ByteBuffer {
        val buf = ByteBuffer.allocateDirect(4 * inputSize * inputSize * 3)
        buf.order(ByteOrder.nativeOrder())

        val pixels = IntArray(inputSize * inputSize)
        bitmap.getPixels(pixels, 0, inputSize, 0, 0, inputSize, inputSize)

        if (channelsFirst) {
            for (p in pixels) buf.putFloat((p shr 16 and 0xFF) / 255.0f)
            for (p in pixels) buf.putFloat((p shr 8 and 0xFF) / 255.0f)
            for (p in pixels) buf.putFloat((p and 0xFF) / 255.0f)
        } else {
            for (p in pixels) {
                buf.putFloat((p shr 16 and 0xFF) / 255.0f)
                buf.putFloat((p shr 8 and 0xFF) / 255.0f)
                buf.putFloat((p and 0xFF) / 255.0f)
            }
        }
        buf.rewind()
        return buf
    }

    @Synchronized
    fun close() {
        if (closed) return
        closed = true
        interpreter.close()
    }
}
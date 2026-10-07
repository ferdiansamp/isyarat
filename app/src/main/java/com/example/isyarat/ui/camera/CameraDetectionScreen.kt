package com.example.isyarat.ui.camera

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.isyarat.HeaderSection
import com.example.isyarat.camera.DetectionOverlay
import com.example.isyarat.data.repository.DetectorRepository
import com.example.isyarat.model.DetectionResult

private const val HOLD_FRAMES = 5
private const val SPACE_AFTER_MS = 1500L
private const val REARM_AFTER_MS = 500L

@Composable
fun ToTextScreen(
    recognizedText: String,
    onTextChange: (String) -> Unit
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current


    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) ==
                    PackageManager.PERMISSION_GRANTED
        )
    }
    var isFullScreen by remember { mutableStateOf(false) }

    var results by remember { mutableStateOf(emptyList<DetectionResult>()) }
    var detectedLabel by remember { mutableStateOf("") }
    var detectedConfidence by remember { mutableStateOf(0f) }

    val candidate = remember { arrayOf<String?>(null) }
    val count = remember { intArrayOf(0) }
    val armed = remember { booleanArrayOf(true) }
    val lastHandTime = remember { longArrayOf(0L) }
    val lastSeen = remember { longArrayOf(0L) }

    val latestText by rememberUpdatedState(recognizedText)
    val latestOnTextChange by rememberUpdatedState(onTextChange)

    val addSpace = remember {
        {
            if (latestText.isNotEmpty() && !latestText.endsWith(" ")) {
                latestOnTextChange("$latestText ")
            }
        }
    }

    val append = remember {
        { label: String ->
            if (label.length > 1) {
                val prefix = if (latestText.isNotEmpty() && !latestText.endsWith(" ")) " " else ""
                latestOnTextChange("$latestText$prefix$label ")
            } else {
                latestOnTextChange(latestText + label)
            }
        }
    }

    // kotak
    val onDetectionResult: (List<DetectionResult>) -> Unit = remember {
        { newResults ->

            val now = System.currentTimeMillis()
            val top = newResults.maxByOrNull { it.confidence }

            if (newResults.isNotEmpty()) {
                results = newResults
                lastSeen[0] = now
            } else if (now - lastSeen[0] > 600) {
                results = newResults
            }

            if (top == null) {
                val absent = now - lastHandTime[0]
                if (lastHandTime[0] > 0 && absent >= REARM_AFTER_MS) {
                    candidate[0] = null
                    count[0] = 0
                    armed[0] = true
                }
                if (lastHandTime[0] > 0 && absent >= SPACE_AFTER_MS) {
                    addSpace()
                    lastHandTime[0] = 0L
                }
            } else {
                lastHandTime[0] = now
                detectedLabel = top.label
                detectedConfidence = top.confidence

                if (top.label == candidate[0]) {
                    count[0]++
                } else {
                    candidate[0] = top.label
                    count[0] = 1
                    armed[0] = true
                }
                if (count[0] >= HOLD_FRAMES && armed[0]) {
                    append(top.label)
                    armed[0] = false
                }
            }
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { hasCameraPermission = it }
    )

    LaunchedEffect(Unit) {
        if (!hasCameraPermission) permissionLauncher.launch(Manifest.permission.CAMERA)
    }

// mengsatukan 2 view
    val cameraContent = remember {
        movableContentOf {
            Box(modifier = Modifier.fillMaxWidth().aspectRatio(3f / 4f)) {
                CameraPreviewView(onDetection = onDetectionResult)
                DetectionOverlay(results)
            }
        }
    }

    if (isFullScreen && hasCameraPermission) {
        Box(
            modifier = Modifier.fillMaxSize().background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            // Panggil movable content di sini
            cameraContent()

            Row(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(16.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.Black.copy(alpha = 0.4f))
                    .clickable { isFullScreen = false }
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Kembali", tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Back", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            }

            Text(
                text = recognizedText.ifEmpty { "..." },
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .padding(16.dp)
            )
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            HeaderSection()

            Column {
                Text(text = "Isyarat ke Text", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "penerjemah kamera instan", fontSize = 14.sp, color = MaterialTheme.colorScheme.tertiary)
            }

            // Panggil placeholder dan lempar movable content ke dalamnya
            CameraScannerPlaceholder(
                hasPermission = hasCameraPermission,
                onClick = { if (hasCameraPermission) isFullScreen = true },
                cameraContent = { cameraContent() }
            )

            TranslationResultCard(
                resultText = recognizedText.ifEmpty { "Belum ada huruf terdeteksi" },
                gestureInfo = if (detectedLabel.isNotEmpty())
                    "Gerakan terdeteksi: $detectedLabel (${(detectedConfidence * 100).toInt()}%)"
                else
                    "Arahkan tangan ke kamera untuk mulai mendeteksi",
                onCopy = { clipboardManager.setText(AnnotatedString(recognizedText)) },
                onReset = {
                    onTextChange("")
                    detectedLabel = ""
                    detectedConfidence = 0f
                    candidate[0] = null
                    count[0] = 0
                    armed[0] = true
                }
            )

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = { addSpace() },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) { Text("Spasi") }

                OutlinedButton(
                    onClick = { if (recognizedText.isNotEmpty()) onTextChange(recognizedText.dropLast(1)) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                ) { Text("Hapus") }
            }

            if (!hasCameraPermission) {
                Button(
                    onClick = { permissionLauncher.launch(Manifest.permission.CAMERA) },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().height(56.dp)
                ) {
                    Icon(Icons.Default.CameraAlt, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Beri Akses Kamera", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                }
            }

            Text(
                text = "Tahan tiap huruf sekitar 1 detik. Turunkan tangan 1,5 detik untuk spasi otomatis. Hasil juga tampil di tab Ke Layar.",
                fontSize = 10.sp, color = MaterialTheme.colorScheme.tertiary, textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)
            )
        }
    }
}

@Composable
fun CameraScannerPlaceholder(
    hasPermission: Boolean,
    onClick: () -> Unit,
    cameraContent: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(3f / 4f)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.background)
            .clickable { onClick() }
    ) {
        if (hasPermission) {
            cameraContent()
        } else {
            Text(
                text = "Menunggu Akses Kamera...",
                color = Color.Gray.copy(alpha = 0.5f),
                modifier = Modifier.align(Alignment.Center)
            )
        }

        // Bracket Kamera
        val bracketColor = Color.White
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeWidth = 4.dp.toPx()
            val bracketLength = 40.dp.toPx()
            val padding = 40.dp.toPx()

            drawLine(bracketColor, Offset(padding, padding), Offset(padding + bracketLength, padding), strokeWidth)
            drawLine(bracketColor, Offset(padding, padding), Offset(padding, padding + bracketLength), strokeWidth)
            drawLine(bracketColor, Offset(size.width - padding, padding), Offset(size.width - padding - bracketLength, padding), strokeWidth)
            drawLine(bracketColor, Offset(size.width - padding, padding), Offset(size.width - padding, padding + bracketLength), strokeWidth)
            drawLine(bracketColor, Offset(padding, size.height - padding), Offset(padding + bracketLength, size.height - padding), strokeWidth)
            drawLine(bracketColor, Offset(padding, size.height - padding), Offset(padding, size.height - padding - bracketLength), strokeWidth)
            drawLine(bracketColor, Offset(size.width - padding, size.height - padding), Offset(size.width - padding - bracketLength, size.height - padding), strokeWidth)
            drawLine(bracketColor, Offset(size.width - padding, size.height - padding), Offset(size.width - padding, size.height - padding - bracketLength), strokeWidth)
        }
    }
}

@Composable
fun CameraPreviewView(onDetection: (List<DetectionResult>) -> Unit = {}) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val currentOnDetection by rememberUpdatedState(onDetection)

    val previewView = remember {
        PreviewView(context).apply {
            implementationMode = PreviewView.ImplementationMode.COMPATIBLE
            scaleType = PreviewView.ScaleType.FILL_CENTER
        }
    }
    val detectorRepository = remember { DetectorRepository(lifecycleOwner, context) }

    DisposableEffect(Unit) {
        detectorRepository.startDetection(previewView) { results ->
            currentOnDetection(results)
        }
        onDispose { detectorRepository.stopDetection() }
    }

    AndroidView(factory = { previewView }, modifier = Modifier.fillMaxSize())
}

@Composable
fun TranslationResultCard(
    resultText: String,
    gestureInfo: String,
    onCopy: () -> Unit = {},
    onReset: () -> Unit = {}
) {
    // Isi komponen ini sama persis seperti sebelumnya
    Box(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(MaterialTheme.colorScheme.background).padding(16.dp)) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Description, contentDescription = null, tint = MaterialTheme.colorScheme.secondary, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Hasil Terjemahan\nLangsung", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary, fontSize = 16.sp, lineHeight = 18.sp)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Box(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(Color.White).padding(16.dp)) {
                Column {
                    Text(text = resultText, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(verticalAlignment = Alignment.Top) {
                        Icon(Icons.Default.Gesture, contentDescription = null, tint = MaterialTheme.colorScheme.secondary, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = gestureInfo, fontSize = 12.sp, color = MaterialTheme.colorScheme.onTertiary, lineHeight = 16.sp)
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                ActionPill(icon = Icons.Default.ContentCopy, text = "Salin", onClick = onCopy)
                ActionPill(icon = Icons.Default.Refresh, text = "Ulangi", onClick = onReset)
            }
        }
    }
}

@Composable
fun ActionPill(icon: ImageVector, text: String, onClick: () -> Unit = {}) {
    Row(
        modifier = Modifier.clip(RoundedCornerShape(8.dp)).background(Color.White).clickable { onClick() }.padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = text, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.tertiary)
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = text, fontSize = 12.sp, color = MaterialTheme.colorScheme.tertiary, fontWeight = FontWeight.SemiBold)
    }
}
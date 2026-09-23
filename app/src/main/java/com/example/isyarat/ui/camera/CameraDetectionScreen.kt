package com.example.isyarat.ui.camera

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.isyarat.HeaderSection

@Composable
fun ToTextScreen() {
    val context = LocalContext.current

    // 1. State untuk mengecek apakah izin kamera sudah diberikan
    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    // STATE BARU: Mengontrol apakah mode layar penuh aktif atau tidak
    var isFullScreen by remember { mutableStateOf(false) }

    // 2. Launcher untuk meminta izin ke user
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            hasCameraPermission = isGranted
        }
    )

    // 3. Meminta izin secara otomatis saat layar ini dibuka
    LaunchedEffect(Unit) {
        if (!hasCameraPermission) {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    // JIKA MODE LAYAR PENUH AKTIF
    if (isFullScreen && hasCameraPermission) {
        Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
            // Kamera menempati seluruh area layar
            CameraPreviewView()

            // Tombol "Back" di kiri atas
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.Black.copy(alpha = 0.4f)) // Sedikit transparan agar mudah dibaca di atas kamera
                    .clickable { isFullScreen = false }
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Kembali", tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Back", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
    // JIKA MODE NORMAL AKTIF
    else {
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
                Text(
                    text = "Isyarat ke Text",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "penerjemah kamera instan",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.tertiary
                )
            }

            // 4. Meneruskan status izin dan aksi klik ke Placeholder Kamera
            CameraScannerPlaceholder(
                hasPermission = hasCameraPermission,
                onClick = {
                    if (hasCameraPermission) {
                        isFullScreen = true // Memicu transisi ke layar penuh saat area ditekan
                    }
                }
            )

            TranslationResultCard()

            Button(
                onClick = {
                    if (!hasCameraPermission) {
                        permissionLauncher.launch(Manifest.permission.CAMERA)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Icon(if (hasCameraPermission) Icons.Default.PauseCircleOutline else Icons.Default.CameraAlt, contentDescription = "Jeda")
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (hasCameraPermission) "Jeda Deteksi" else "Beri Akses Kamera",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Text(
                text = "Kamera memproses isyarat secara langsung di perangkat secara aman.",
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.tertiary,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
            )
        }
    }
}

@Composable
fun CameraScannerPlaceholder(hasPermission: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f) // Membuatnya persegi
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.background)
            .clickable { onClick() } // Menjadikan keseluruhan area kotak dapat ditekan
    ) {
        // 5. Jika diberi izin, tampilkan Kamera Asli. Jika tidak, tampilkan teks.
        if (hasPermission) {
            CameraPreviewView()
        } else {
            Text(
                text = "Menunggu Akses Kamera...",
                color = Color.Gray.copy(alpha = 0.5f),
                modifier = Modifier.align(Alignment.Center)
            )
        }

        // Menggambar garis siku (brackets) di ATAS kamera
        val bracketColor = Color.White
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeWidth = 4.dp.toPx()
            val bracketLength = 40.dp.toPx()
            val padding = 40.dp.toPx()

            // Top Left
            drawLine(color = bracketColor, start = Offset(padding, padding), end = Offset(padding + bracketLength, padding), strokeWidth = strokeWidth)
            drawLine(color = bracketColor, start = Offset(padding, padding), end = Offset(padding, padding + bracketLength), strokeWidth = strokeWidth)

            // Top Right
            drawLine(color = bracketColor, start = Offset(size.width - padding, padding), end = Offset(size.width - padding - bracketLength, padding), strokeWidth = strokeWidth)
            drawLine(color = bracketColor, start = Offset(size.width - padding, padding), end = Offset(size.width - padding, padding + bracketLength), strokeWidth = strokeWidth)

            // Bottom Left
            drawLine(color = bracketColor, start = Offset(padding, size.height - padding), end = Offset(padding + bracketLength, size.height - padding), strokeWidth = strokeWidth)
            drawLine(color = bracketColor, start = Offset(padding, size.height - padding), end = Offset(padding, size.height - padding - bracketLength), strokeWidth = strokeWidth)

            // Bottom Right
            drawLine(color = bracketColor, start = Offset(size.width - padding, size.height - padding), end = Offset(size.width - padding - bracketLength, size.height - padding), strokeWidth = strokeWidth)
            drawLine(color = bracketColor, start = Offset(size.width - padding, size.height - padding), end = Offset(size.width - padding, size.height - padding - bracketLength), strokeWidth = strokeWidth)
        }
    }
}

// 6. Fungsi Composable untuk Menjalankan CameraX
@Composable
fun CameraPreviewView() {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    AndroidView(
        factory = { ctx ->
            val previewView = PreviewView(ctx)
            val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)

            cameraProviderFuture.addListener({
                val cameraProvider = cameraProviderFuture.get()

                // Menyiapkan surface kamera
                val preview = Preview.Builder().build().also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }

                // Gunakan kamera belakang secara default
                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                try {
                    // Unbind use cases sebelum re-binding
                    cameraProvider.unbindAll()

                    // Bind kamera ke lifecycle Compose
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        preview
                    )
                } catch (exc: Exception) {
                    exc.printStackTrace()
                }
            }, ContextCompat.getMainExecutor(ctx))

            previewView
        },
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
fun TranslationResultCard() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Column {
            // Header Result
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Description, contentDescription = null, tint = MaterialTheme.colorScheme.secondary, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Hasil Terjemahan\nLangsung",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 16.sp,
                    lineHeight = 18.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // White Box with Text
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                Column {
                    Text(
                        text = "\"Halo, senang bertemu dengan Anda\"",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(verticalAlignment = Alignment.Top) {
                        Icon(Icons.Default.Gesture, contentDescription = null, tint = MaterialTheme.colorScheme.secondary, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Gerakan terdeteksi: Salam pembuka + Senang",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onTertiary,
                            lineHeight = 16.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Action Buttons Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ActionPill(icon = Icons.Default.ContentCopy, text = "Salin")
                ActionPill(icon = Icons.Default.Save, text = "Simpan")
                ActionPill(icon = Icons.Default.Refresh, text = "Ulangi")
            }
        }
    }
}

@Composable
fun ActionPill(icon: ImageVector, text: String) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White)
            .clickable { /* TODO: Action onClick */ }
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = text, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.tertiary)
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = text, fontSize = 12.sp, color = MaterialTheme.colorScheme.tertiary, fontWeight = FontWeight.SemiBold)
    }
}
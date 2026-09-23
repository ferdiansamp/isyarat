package com.example.isyarat.ui.camera

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Gesture
import androidx.compose.material.icons.filled.PauseCircleOutline
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.isyarat.HeaderSection

// INI LAMAN KAMERA KE TEKS
@Composable
fun ToTextScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 20.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // 1. Header (Reused style from Home)
        HeaderSection() // Menggunakan HeaderSection dari file MainActivity sebelumnya

        // 2. Title Section
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

        // 3. Camera Placeholder (Tempat Anda akan memasukkan CameraX nanti)
        CameraScannerPlaceholder()

        // 4. Live Translation Result
        TranslationResultCard()

        // 5. Action Button (Jeda/Mulai Deteksi)
        Button(
            onClick = { /* TODO: Pause/Resume Camera/ML detection */ },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Icon(Icons.Default.PauseCircleOutline, contentDescription = "Jeda")
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Jeda Deteksi", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        }

        // 6. Footer Info
        Text(
            text = "Kamera memproses isyarat secara langsung di perangkat secara aman.",
            fontSize = 10.sp,
            color = MaterialTheme.colorScheme.tertiary,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)
        )
    }
}

@Composable
fun CameraScannerPlaceholder() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f) // Membuatnya persegi
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Menggambar garis siku (brackets) seperti di desain
        val bracketColor = Color.Gray
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

        Text(
            text = "Area Kamera",
            color = Color.Gray.copy(alpha = 0.5f),
            modifier = Modifier.align(Alignment.Center)
        )
    }
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
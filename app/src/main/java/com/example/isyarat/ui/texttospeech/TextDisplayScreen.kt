import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.PanTool
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.isyarat.HeaderSection

// --- TAMBAHAN WARNA UNTUK RIWAYAT ---
val CyanBadgeBg = Color(0xFFC4EFFF)
val PurpleBadgeBg = Color(0xFFE0E7FF)
val LightBlueActionBg = Color(0xFFEEF2FF)
val SearchBarBg = Color(0xFFE5E7EB) // Abu-abu terang sesuai gambar

// --- DATA CLASS UNTUK RIWAYAT ---
data class HistoryModel(
    val id: Int,
    val type: String, // "isyarat_ke_teks" atau "teks_ke_layar"
    val timestamp: String,
    val text: String
)

val dummyHistory = listOf(
    HistoryModel(1, "isyarat_ke_teks", "Hari ini, 09:15 WIB", "\"Selamat pagi, boleh saya bertanya jadwal dokter hari ini?\""),
    HistoryModel(2, "isyarat_ke_teks", "Hari ini, 09:15 WIB", "\"Selamat pagi, boleh saya bertanya jadwal dokter hari ini?\""),
    HistoryModel(3, "teks_ke_layar", "Kemarin, 16:40 WIB", "\"Terima kasih banyak, sangat membantu!\"")
)

// INI LAMAN HISTORY
@Composable
fun HistoryScreen() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 20.dp),
        contentPadding = PaddingValues(top = 24.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // 1. Header (Gunakan dari MainActivity sebelumnya)
        item { HeaderSection() }

        // 2. Title Section
        item {
            Column {
                Text(
                    text = "Riwayat Terjemahan",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Daftar percakapan dan terjemahan yang telah disimpan",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
        }

        // 3. Search Bar & Filter Placeholders (Berdasarkan blok abu-abu di gambar)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Kotak abu-abu panjang (Search Bar)
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(SearchBarBg)
                )

                // Kotak abu-abu kecil (Filter)
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(SearchBarBg)
                )
            }
        }

        // 4. Filter Chips (Blok biru gelap di gambar)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                repeat(3) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(24.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.primary)
                    )
                }
            }
        }

        // 5. List Riwayat
        items(dummyHistory) { item ->
            HistoryCardItem(item)
        }

        // 6. Privacy Footer
        item {
            PrivacyFooter()
        }

        // 7. Clear History Button
        item {
            ClearHistoryButton()
        }
    }
}

@Composable
fun HistoryCardItem(history: HistoryModel) {
    val isIsyarat = history.type == "isyarat_ke_teks"
    val badgeBg = if (isIsyarat) CyanBadgeBg else PurpleBadgeBg
    val badgeIcon = if (isIsyarat) Icons.Default.PanTool else Icons.Default.Fullscreen
    val badgeText = if (isIsyarat) "Isyarat → Teks" else "Teks → Layar"

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.background) // Background card abu-abu
            .padding(16.dp)
    ) {
        Column {
            // Header Card: Badge & Waktu
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Badge
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(badgeBg)
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(badgeIcon, contentDescription = null, tint = MaterialTheme.colorScheme.tertiary, modifier = Modifier.size(12.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = badgeText, fontSize = 10.sp, color = MaterialTheme.colorScheme.tertiary, fontWeight = FontWeight.SemiBold)
                }

                // Waktu
                Text(text = history.timestamp, fontSize = 12.sp, color = MaterialTheme.colorScheme.onTertiary)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Teks Riwayat
            Text(
                text = history.text,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.tertiary,
                fontWeight = FontWeight.Medium,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Action Buttons Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    // Tombol Khusus "Teks ke Layar" (Tampilkan Lagi)
                    if (!isIsyarat) {
                        ActionHistoryButton(
                            text = "Tampilkan Lagi",
                            icon = Icons.Default.Fullscreen,
                            bgColor = MaterialTheme.colorScheme.primary,
                            textColor = Color.White
                        )
                    }

                    // Tombol Default "Salin"
                    ActionHistoryButton(
                        text = "Salin",
                        icon = Icons.Default.ContentCopy,
                        bgColor = LightBlueActionBg,
                        textColor = MaterialTheme.colorScheme.primary
                    )
                }

                // Ikon Hapus
                Icon(
                    Icons.Default.DeleteOutline,
                    contentDescription = "Hapus",
                    tint = MaterialTheme.colorScheme.onTertiary,
                    modifier = Modifier
                        .size(20.dp)
                        .clickable { /* TODO: Hapus item */ }
                )
            }
        }
    }
}

@Composable
fun ActionHistoryButton(text: String, icon: ImageVector, bgColor: Color, textColor: Color) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(bgColor)
            .clickable { /* TODO Action */ }
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = textColor, modifier = Modifier.size(14.dp))
        Spacer(modifier = Modifier.width(6.dp))
        Text(text = text, fontSize = 12.sp, color = textColor, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun PrivacyFooter() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(contentAlignment = Alignment.TopEnd) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(LightBlueActionBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.VerifiedUser, contentDescription = "Privasi", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(24.dp))
            }
            // Titik hijau indikator aman
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF0D9488)) // Teal Hijau
                    .border(2.dp, Color.White, CircleShape)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Privasi Terjaga di Perangkat",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Semua data riwayat tersimpan aman secara luring pada perangkat Anda tanpa dikirim ke server publik.",
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onTertiary,
            textAlign = TextAlign.Center,
            lineHeight = 16.sp
        )
    }
}

@Composable
fun ClearHistoryButton() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(LightBlueActionBg)
            .clickable { /* TODO: Hapus Semua Action */ }
            .padding(vertical = 14.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Default.DeleteOutline, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = "Kosongkan Semua Riwayat", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
    }
}
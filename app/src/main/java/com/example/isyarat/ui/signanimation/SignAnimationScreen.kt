import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.ScreenRotation
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.isyarat.HeaderSection

// Tambahan Warna
val LightCyanBg = Color(0xFFC4EFFF)

// INI LAMAN HURUF KE HURUF YANG LEBIH BESAR
@Composable
fun ToScreenScreen() {
    // State untuk input teks
    var messageText by remember {
        mutableStateOf("Permisi, apakah trans jateng pemberhentian kampus teknik sudah lewat?")
    }

    // State untuk ukuran teks
    var selectedSize by remember { mutableStateOf("Ekstra Besar") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 20.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // 1. Header (Gunakan dari MainActivity)
        HeaderSection()

        // 2. Title Section
        Column {
            Text(
                text = "Text ke Layar",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "penerjemah kamera instan", // Sesuai teks di desain
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.tertiary
            )
        }

        // 3. Preview Section
        SectionTitleWithIcon(icon = Icons.Default.Visibility, title = "PREVIEW LAYAR KOMUNIKASI")
        CommunicationPreviewCard(text = messageText)

        // 4. Input Section
        SectionTitleWithIcon(icon = Icons.Default.Edit, title = "Tulis Pesan Cepat")
        MessageInputBox(text = messageText, onTextChange = { messageText = it })

        // 5. Settings Section
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SectionTitleWithIcon(icon = Icons.Default.Tune, title = "Pengaturan Tampilan", paddingBottom = 0.dp)
            Row(modifier = Modifier.clickable { /* Reset action */ }) {
                Icon(Icons.Default.Refresh, contentDescription = "Reset", tint = MaterialTheme.colorScheme.secondary, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "Reset", color = MaterialTheme.colorScheme.secondary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            }
        }
        DisplaySizeSelector(
            selectedOption = selectedSize,
            onOptionSelected = { selectedSize = it }
        )

        // 6. Tips Section
        TipsCard()

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun SectionTitleWithIcon(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String, paddingBottom: androidx.compose.ui.unit.Dp = 8.dp) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = paddingBottom)) {
        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            fontSize = 14.sp
        )
    }
}

@Composable
fun CommunicationPreviewCard(text: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.primary)
            .padding(16.dp)
    ) {
        Column {
            // Header Card
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.ScreenRotation, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Tampilan Normal", color = Color.White, fontSize = 12.sp)
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White.copy(alpha = 0.2f))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                        .clickable { /* TODO: Rotate 180 degrees logic */ }
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Sync, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Putar 180°", color = Color.White, fontSize = 12.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Main Display Text
            Text(
                text = text.uppercase(),
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Black,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                lineHeight = 32.sp,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Footer Card
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Mode Komunikasi Langsung", color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
                Row {
                    Box(modifier = Modifier.size(6.dp).clip(androidx.compose.foundation.shape.CircleShape).background(Color.White))
                    Spacer(modifier = Modifier.width(4.dp))
                    Box(modifier = Modifier.size(6.dp).clip(androidx.compose.foundation.shape.CircleShape).background(Color.White))
                }
            }
        }
    }
}

@Composable
fun MessageInputBox(text: String, onTextChange: (String) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        BasicTextField(
            value = text,
            onValueChange = onTextChange,
            textStyle = TextStyle(
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.tertiary,
                lineHeight = 24.sp
            ),
            modifier = Modifier.fillMaxWidth(),
            decorationBox = { innerTextField ->
                if (text.isEmpty()) {
                    Text("Ketik pesan Anda di sini...", color = MaterialTheme.colorScheme.onTertiary)
                }
                innerTextField()
            }
        )
    }
}

@Composable
fun DisplaySizeSelector(selectedOption: String, onOptionSelected: (String) -> Unit) {
    val options = listOf("Sedang", "Besar", "Ekstra Besar")

    Column {
        Text(text = "Ukuran Teks Layar", fontSize = 12.sp, color = MaterialTheme.colorScheme.tertiary, modifier = Modifier.padding(bottom = 8.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFF3F4F6)) // Warna background abu-abu sangat terang
                .padding(4.dp)
        ) {
            options.forEach { text ->
                val isSelected = selectedOption == text
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent)
                        .clickable { onOptionSelected(text) }
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = text,
                        color = if (isSelected) Color.White else MaterialTheme.colorScheme.tertiary,
                        fontSize = 14.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }
    }
}

@Composable
fun TipsCard() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(LightCyanBg)
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.Top) {
            Icon(Icons.Default.Info, contentDescription = "Tips", tint = MaterialTheme.colorScheme.secondary)
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(text = "Tips Berkomunikasi di Tempat Ramai", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.tertiary, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Arahkan layar ponsel ke lawan bicara dan aktifkan rotasi 180° agar mereka bisa langsung membaca tanpa Anda harus membalikkan seluruh genggaman ponsel.",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.secondary,
                    lineHeight = 18.sp
                )
            }
        }
    }
}
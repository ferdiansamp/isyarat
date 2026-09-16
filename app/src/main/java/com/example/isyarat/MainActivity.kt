package com.example.isyarat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.geometry.Offset

// --- TEMA WARNA ---
val PrimaryBlue = Color(0xFF1E3A8A)
val TealAction = Color(0xFF0F768E)
val LightGrayBg = Color(0xFFE5E7EB)
val DarkText = Color(0xFF1F2937)
val GrayText = Color(0xFF6B7280)

// --- DATA CLASS UNTUK DINAMIS ---
data class TranslationMode(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val buttonText: String
)

data class RecentActivity(
    val typeTitle: String,
    val isSignToText: Boolean,
    val time: String,
    val contentText: String,
    val trailingIcon: ImageVector
)

// --- MOCK DATA ---
val modeList = listOf(
    TranslationMode(
        title = "Isyarat ke Text",
        description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc vulputate libero et velit",
        icon = Icons.Default.Videocam,
        buttonText = "Mulai Kamera"
    ),
    TranslationMode(
        title = "Text ke Layar",
        description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc vulputate libero et velit",
        icon = Icons.Default.Fullscreen,
        buttonText = "Mulai Kamera"
    )
)

val activityList = listOf(
    RecentActivity("Isyarat → Teks", true, "10 mnt lalu", "\"Selamat pagi, apa kabar?\"", Icons.Default.VolumeUp),
    RecentActivity("Teks → Layar", false, "1 jam lalu", "\"Di mana halte bus terdekat?\"", Icons.Default.Fullscreen),
    RecentActivity("Isyarat → Teks", true, "Kemarin", "\"Terima kasih banyak atas bantuannya\"", Icons.Default.VolumeUp)
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                MainScreen()
            }
        }
    }
}

// INI MAIN SCREEN
@Composable
fun MainScreen() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("home") { HomeScreen() }
            composable("to_text") { ToTextScreen() }
            composable("to_screen") { DummyScreen("Ke Layar") }
            composable("history") { DummyScreen("Riwayat") }
//            composable("settings") { DummyScreen("Pengaturan") }
        }
    }
}


// INI BUAT LAMAN BERANDA
@Composable
fun HomeScreen() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 20.dp),
        contentPadding = PaddingValues(top = 24.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // 1. Header & Greeting
        item {
            HeaderSection()
            Spacer(modifier = Modifier.height(16.dp))
            GreetingSection()
        }

        // 2. Banner
        item { BannerSection() }

        // 3. Translation Modes
        item { SectionHeader("Pilih Mode Terjemahan", "2 Mode Tersedia") }
        items(modeList) { mode ->
            TranslationModeCard(mode)
        }

        // 4. Recent Activities
        item { SectionHeader("Aktivitas Terakhir", "Lihat Semua >", linkColor = TealAction) }
        items(activityList) { activity ->
            ActivityCard(activity)
        }
    }
}

// INI HEADER
@Composable
fun HeaderSection() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = Icons.Default.PanTool, // Placeholder Logo
            contentDescription = "Logo",
            tint = PrimaryBlue,
            modifier = Modifier.size(32.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "Isyarat",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = PrimaryBlue
        )
    }
}

@Composable
fun GreetingSection() {
    Column {
        Text(
            text = "Halo, Teman Isyarat",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = PrimaryBlue
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Siap berkomunikasi tanpa batas hari ini?",
            fontSize = 16.sp,
            color = DarkText
        )
    }
}

@Composable
fun BannerSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(PrimaryBlue)
            .padding(20.dp)
    ) {
        Column {
            Text(
                text = "Terjemahkan Isyarat dengan Mudah",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Jembatan komunikasi inklusif antara SIBI/BISINDO dan teks tulisan secara instan dan akurat.",
                color = Color.White.copy(alpha = 0.9f),
                fontSize = 14.sp,
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
fun SectionHeader(title: String, subtitle: String, linkColor: Color = DarkText) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        Text(text = title, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = DarkText)
        Text(text = subtitle, fontSize = 12.sp, color = linkColor)
    }
}

@Composable
fun TranslationModeCard(mode: TranslationMode) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(LightGrayBg)
            .padding(16.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(verticalAlignment = Alignment.Top) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(TealAction),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(mode.icon, contentDescription = null, tint = Color.White)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = mode.title, fontWeight = FontWeight.Bold, color = PrimaryBlue)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = mode.description, fontSize = 12.sp, color = GrayText, lineHeight = 16.sp)
                    }
                }
                Icon(Icons.Default.ArrowForward, contentDescription = null, tint = TealAction)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Box(
                modifier = Modifier
                    .align(Alignment.End)
                    .clip(RoundedCornerShape(8.dp))
                    .background(TealAction)
                    .clickable { /* TODO Action */ }
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = mode.buttonText, color = Color.White, fontSize = 12.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(Icons.Default.ArrowForward, contentDescription = null, tint = Color.White, modifier = Modifier.size(12.dp))
                }
            }
        }
    }
}

@Composable
fun ActivityCard(activity: RecentActivity) {
    val iconBgColor = if (activity.isSignToText) Color(0xFFBBE5ED) else Color(0xFFDCD6F7)
    val badgeBgColor = if (activity.isSignToText) TealAction else PrimaryBlue
    val iconMain = if (activity.isSignToText) Icons.Default.PanTool else Icons.Default.TextFields

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(LightGrayBg)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(iconBgColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(iconMain, contentDescription = null, tint = badgeBgColor)
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(badgeBgColor)
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(text = activity.typeTitle, color = Color.White, fontSize = 10.sp)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = activity.time, fontSize = 12.sp, color = GrayText)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = activity.contentText, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = DarkText)
        }
        Icon(activity.trailingIcon, contentDescription = null, tint = GrayText)
    }
}

// INI TOMBOL NAVIGASI
@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        Triple("Beranda", Icons.Default.Home, "home"),
        Triple("Ke Teks", Icons.Default.PanTool, "to_text"),
        Triple("Ke Layar", Icons.Default.Fullscreen, "to_screen"),
        Triple("Riwayat", Icons.Default.History, "history"),
//        Triple("Pengaturan", Icons.Default.Settings, "settings")
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(
        containerColor = PrimaryBlue,
        contentColor = Color.White,
        tonalElevation = 8.dp
    ) {
        items.forEach { (title, icon, route) ->
            NavigationBarItem(
                icon = { Icon(icon, contentDescription = title) },
                label = { Text(text = title, fontSize = 10.sp) },
                selected = currentRoute == route,
                onClick = {
                    if (currentRoute != route) {
                        navController.navigate(route) {
                            popUpTo("home") { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = PrimaryBlue,
                    unselectedIconColor = Color.White.copy(alpha = 0.7f),
                    selectedTextColor = Color.White,
                    unselectedTextColor = Color.White.copy(alpha = 0.7f),
                    indicatorColor = Color.White
                )
            )
        }
    }
}


//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.*
//import androidx.compose.material3.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.vector.ImageVector
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp


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
                color = PrimaryBlue
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "penerjemah kamera instan",
                fontSize = 14.sp,
                color = DarkText
            )
        }

        // 3. Camera Placeholder (Tempat Anda akan memasukkan CameraX nanti)
        CameraScannerPlaceholder()

        // 4. Live Translation Result
        TranslationResultCard()

        // 5. Action Button (Jeda/Mulai Deteksi)
        Button(
            onClick = { /* TODO: Pause/Resume Camera/ML detection */ },
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
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
            color = DarkText,
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
            .background(LightGrayBg)
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
            .background(LightGrayBg)
            .padding(16.dp)
    ) {
        Column {
            // Header Result
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Description, contentDescription = null, tint = TealAction, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Hasil Terjemahan\nLangsung",
                    fontWeight = FontWeight.Bold,
                    color = PrimaryBlue,
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
                        color = PrimaryBlue
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(verticalAlignment = Alignment.Top) {
                        Icon(Icons.Default.Gesture, contentDescription = null, tint = TealAction, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Gerakan terdeteksi: Salam pembuka + Senang",
                            fontSize = 12.sp,
                            color = GrayText,
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
        Icon(icon, contentDescription = text, modifier = Modifier.size(16.dp), tint = DarkText)
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = text, fontSize = 12.sp, color = DarkText, fontWeight = FontWeight.SemiBold)
    }
}


// INI DUMMY DOANG :v
@Composable
fun DummyScreen(title: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "Halaman $title", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = PrimaryBlue)
    }
}
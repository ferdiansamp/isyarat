package com.example.isyarat

import com.example.isyarat.ui.camera.ToTextScreen
import HistoryScreen
import ToScreenScreen
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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.Composable

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
            composable("to_screen") { ToScreenScreen() }
            composable("history") { HistoryScreen() }
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
        item { SectionHeader("Aktivitas Terakhir", "Lihat Semua >", linkColor = MaterialTheme.colorScheme.secondary) }
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
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(32.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "Isyarat",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
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
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Siap berkomunikasi tanpa batas hari ini?",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.tertiary
        )
    }
}

@Composable
fun BannerSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.primary)
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
fun SectionHeader(title: String, subtitle: String, linkColor: Color = MaterialTheme.colorScheme.tertiary) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        Text(text = title, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.tertiary)
        Text(text = subtitle, fontSize = 12.sp, color = linkColor)
    }
}

@Composable
fun TranslationModeCard(mode: TranslationMode) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.background)
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
                            .background(MaterialTheme.colorScheme.secondary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(mode.icon, contentDescription = null, tint = Color.White)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = mode.title, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = mode.description, fontSize = 12.sp, color = MaterialTheme.colorScheme.onTertiary, lineHeight = 16.sp)
                    }
                }
                Icon(Icons.Default.ArrowForward, contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Box(
                modifier = Modifier
                    .align(Alignment.End)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.secondary)
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
    val badgeBgColor = if (activity.isSignToText) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary
    val iconMain = if (activity.isSignToText) Icons.Default.PanTool else Icons.Default.TextFields

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.background)
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
                Text(text = activity.time, fontSize = 12.sp, color = MaterialTheme.colorScheme.onTertiary)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = activity.contentText, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = MaterialTheme.colorScheme.tertiary)
        }
        Icon(activity.trailingIcon, contentDescription = null, tint = MaterialTheme.colorScheme.onTertiary)
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
        containerColor = MaterialTheme.colorScheme.primary,
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
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = Color.White.copy(alpha = 0.7f),
                    selectedTextColor = Color.White,
                    unselectedTextColor = Color.White.copy(alpha = 0.7f),
                    indicatorColor = Color.White
                )
            )
        }
    }
}

// INI DUMMY DOANG :V
@Composable
fun DummyScreen(title: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "Halaman $title", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
    }
}
package com.example.henshinphone.feature

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.henshinphone.feature.storage.LocalStore

@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    onOpenUserCodes: () -> Unit
) {
    val context = LocalContext.current

    // ✅ 真正绑定 LocalStore
    var soundEnabled by remember {
        mutableStateOf(LocalStore.isSoundEnabled(context))
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0E0E0E))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            Text(
                text = "SETTINGS",
                color = Color.White,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Divider(color = Color(0xFF333333))

            SettingRowWithSwitch(
                title = "Sound Effect",
                subtitle = "变身音效开关",
                checked = soundEnabled,
                onCheckedChange = { enabled ->
                    soundEnabled = enabled
                    LocalStore.setSoundEnabled(context, enabled)
                }
            )

            Divider(color = Color(0xFF333333))

            SettingRow(
                title = "User Codes",
                subtitle = "自定义变身密码（FAIZ）",
                highlight = true,
                onClick = onOpenUserCodes
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onBack,
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6A4FB3)
                )
            ) {
                Text(
                    text = "BACK",
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)
                )
            }
        }
    }
}

@Composable
private fun SettingRow(
    title: String,
    subtitle: String,
    highlight: Boolean = false,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = if (highlight) Color(0xFF241A3A) else Color.Transparent,
                shape = RoundedCornerShape(14.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        Text(text = title, color = Color.White, fontWeight = FontWeight.Medium)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = subtitle, color = Color(0xFF9A9A9A))
    }
}

@Composable
private fun SettingRowWithSwitch(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF1C1C1C), RoundedCornerShape(14.dp))
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, color = Color.White, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = subtitle, color = Color(0xFF9A9A9A))
        }

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}

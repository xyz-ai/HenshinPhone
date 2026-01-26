package com.example.henshinphone.feature

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    onOpenUserCodes: () -> Unit = {},
    onOpenAnimations: () -> Unit = {}
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0E0E0E))
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 32.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {

            Text(
                text = "SETTINGS",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            SettingRowWithSwitch(
                title = "Sound Effect",
                subtitle = "变身音效开关",
                checked = AppSettings.soundEnabled,
                onCheckedChange = { enabled ->
                    AppSettings.soundEnabled = enabled
                }
            )

            Divider(color = Color(0xFF333333))

            SettingRow(
                title = "User Codes",
                subtitle = "自定义变身密码",
                highlight = true,
                onClick = onOpenUserCodes
            )

            SettingRow(
                title = "Animations",
                subtitle = "变身动画管理",
                onClick = onOpenAnimations
            )

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = onBack,
                modifier = Modifier.align(Alignment.Start),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6A4FB3)
                )
            ) {
                Text(
                    text = "BACK",
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
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
    onClick: (() -> Unit)? = null
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = if (highlight) Color(0xFF2A1E4F) else Color.Transparent,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(enabled = onClick != null) {
                onClick?.invoke()
            }
            .padding(vertical = 14.dp, horizontal = 12.dp)
    ) {
        Text(text = title, color = Color.White, fontWeight = FontWeight.Medium)
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = subtitle,
            color = Color(0xFF9A9A9A),
            style = MaterialTheme.typography.bodySmall
        )
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
            .padding(vertical = 12.dp, horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(text = title, color = Color.White, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = subtitle,
                color = Color(0xFF9A9A9A),
                style = MaterialTheme.typography.bodySmall
            )
        }

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}

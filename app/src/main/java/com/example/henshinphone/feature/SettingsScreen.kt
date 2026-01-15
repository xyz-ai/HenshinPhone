package com.example.henshinphone.feature

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SettingsScreen(
    onBack: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .width(600.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {

            Text(
                text = "SETTINGS",
                style = MaterialTheme.typography.headlineMedium
            )

            SettingItem(
                title = "Language Display",
                sub = "语言显示 / 言語表示"
            )

            SettingItem(
                title = "Sound Effect",
                sub = "音效开关 / サウンド"
            )

            SettingItem(
                title = "Transformation Mode",
                sub = "变身模式 / 変身モード"
            )
        }

        Button(
            onClick = onBack,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(24.dp)
        ) {
            Text("BACK")
        }
    }
}

@Composable
private fun SettingItem(
    title: String,
    sub: String
) {
    Column {
        Text(text = title)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = sub, style = MaterialTheme.typography.bodyMedium)
    }
}


package com.example.henshinphone.feature

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings

@Composable
fun DeviceSelectorScreen(
    onDeviceSelected: () -> Unit,
    onOpenSettings: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {

        // ─────────────────────────
        // Top-Left: Rider Selector
        // ─────────────────────────
        Row(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 16.dp, top = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            RiderItem(
                name = "FAIZ",
                isSelected = true,
                onClick = onDeviceSelected
            )

            // 未来扩展用（现在不要打开）
            /*
            RiderItem(
                name = "KAIXA",
                isSelected = false,
                onClick = { }
            )
            */
        }

        // ─────────────────────────
        // Bottom-Right: Settings
        // ─────────────────────────
        IconButton(
            onClick = onOpenSettings,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .size(56.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Settings,
                contentDescription = "Settings",
                tint = Color.White
            )
        }
    }
}

@Composable
private fun RiderItem(
    name: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val color = if (isSelected) {
        Color(0xFFE53935) // Faiz Red（占位）
    } else {
        Color(0xFF555555)
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .size(72.dp)
            .clickable(onClick = onClick)
    ) {

        // Icon placeholder（之后换成真实腰带图）
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(color, CircleShape)
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = name,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}

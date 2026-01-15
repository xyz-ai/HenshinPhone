package com.example.henshinphone.feature

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.henshinphone.R

@Composable
fun DeviceSelectorScreen(
    onDeviceSelected: () -> Unit,
    onOpenSettings: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {

        // 1) Background
        Image(
            painter = painterResource(id = R.drawable.bg_selector_panel),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // 2) Top-left: belt icons row
        Row(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 24.dp, top = 22.dp),
            horizontalArrangement = Arrangement.spacedBy(26.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BeltIcon(
                label = "FAIZ",
                resId = R.drawable.belt_faiz,
                selected = true,
                onClick = onDeviceSelected
            )

            BeltIcon(
                label = "KAIXA",
                resId = R.drawable.belt_kaixa,
                selected = false,
                onClick = { /* TODO */ }
            )

            BeltIcon(
                label = "DELTA",
                resId = R.drawable.belt_delta,
                selected = false,
                onClick = { /* TODO */ }
            )

            BeltIcon(
                label = "MORE",
                resId = R.drawable.belt_more,
                selected = false,
                onClick = { /* TODO */ }
            )
        }

        // ✅ 3) 只保留右下角齿轮按钮：点击进入 Settings Screen
        IconButton(
            onClick = onOpenSettings,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 22.dp, bottom = 18.dp)
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
private fun BeltIcon(
    label: String,
    resId: Int,
    selected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(120.dp)
            .clickable(onClick = onClick)
    ) {
        Image(
            painter = painterResource(id = resId),
            contentDescription = label,
            modifier = Modifier
                .height(64.dp)
                .fillMaxWidth(),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = if (selected) Color.White else Color(0xFF9A9A9A),
            letterSpacing = 1.sp
        )

        if (selected) {
            Spacer(modifier = Modifier.height(6.dp))
            Box(
                modifier = Modifier
                    .width(70.dp)
                    .height(2.dp)
                    .background(Color(0xFFE53935))
            )
        }
    }
}

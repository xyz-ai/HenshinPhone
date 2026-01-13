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
                onClick = { /* 先占位，后续接多腰带 */ }
            )

            BeltIcon(
                label = "DELTA",
                resId = R.drawable.belt_delta,
                selected = false,
                onClick = { /* 占位 */ }
            )

            BeltIcon(
                label = "MORE",
                resId = R.drawable.belt_more,
                selected = false,
                onClick = { /* 占位 */ }
            )
        }

        // 3) Bottom-right: settings panel (matches your mock)
        SettingsPanel(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 22.dp, bottom = 20.dp),
            onClick = onOpenSettings
        )

        // 4) Bottom-right gear button (optional but matches style)
        IconButton(
            onClick = onOpenSettings,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 18.dp, bottom = 14.dp)
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
        // 腰带透明 PNG
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

        // 简单“选中底线”提示（先不做复杂光效）
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

@Composable
private fun SettingsPanel(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .width(560.dp)
            .height(180.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xAA101010))
            .clickable(onClick = onClick)
            .padding(18.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(18.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            SettingRow(
                titleEn = "Volume Control",
                subCjk = "音量设置 / 音量調整"
            )

            DividerLine()

            SettingRow(
                titleEn = "Code & Animation",
                subCjk = "数字变身 / コード変身"
            )
        }
    }
}

@Composable
private fun SettingRow(
    titleEn: String,
    subCjk: String
) {
    Row(
        verticalAlignment = Alignment.Top,
        modifier = Modifier.fillMaxWidth()
    ) {
        // 左侧小图标占位（后续可替换成你自己的 PNG）
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFF2A2A2A))
        )

        Spacer(modifier = Modifier.width(14.dp))

        Column {
            Text(
                text = titleEn,
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = subCjk,
                fontSize = 16.sp,
                color = Color(0xFFDDDDDD)
            )
        }
    }
}

@Composable
private fun DividerLine() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(Color(0x33FFFFFF))
    )
}

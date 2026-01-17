package com.example.henshinphone.feature

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.henshinphone.R

@Composable
fun DeviceScreen(
    rules: List<TransformationRule>,
    onTransformationSelected: (TransformationRule) -> Unit,
    onBack: () -> Unit,
    onOpenSettings: () -> Unit
) {
    var input by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {

        // ===============================
        // 1️⃣ Faiz Phone 外壳（纯视觉层）
        // ===============================
        androidx.compose.foundation.Image(
            painter = painterResource(R.drawable.faiz_phone_base),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(0.85f),
            contentScale = androidx.compose.ui.layout.ContentScale.Fit
        )

        // ===============================
        // 2️⃣ 屏幕显示区（红色 555）
        // ===============================
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                // ⚠️ 这个 offset 是“屏幕区域位置”，后面可以微调
                .offset(y = (-120).dp)
                .width(240.dp)
                .height(70.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (input.isEmpty()) "···" else input,
                fontSize = 36.sp,
                color = Color(0xFFE53935),
                letterSpacing = 4.sp
            )
        }

        // ===============================
        // 3️⃣ 数字键盘（覆盖在外壳上）
        // ===============================
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = 120.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            (1..9).chunked(3).forEach { row ->
                Row(horizontalArrangement = Arrangement.spacedBy(18.dp)) {
                    row.forEach { number ->
                        FaizKeyButton(
                            number = number,
                            onClick = {
                                if (input.length < 3) {
                                    input += number.toString()
                                    rules.find { it.code == input }?.let {
                                        input = ""
                                        onTransformationSelected(it)
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }

        // ===============================
        // 4️⃣ 返回按钮（真正返回上一级）
        // ===============================
        IconButton(
            onClick = onBack,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(20.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = Color.White
            )
        }

        // ===============================
        // 5️⃣ 设置按钮
        // ===============================
        IconButton(
            onClick = onOpenSettings,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "Settings",
                tint = Color.White
            )
        }
    }
}

@Composable
private fun FaizKeyButton(
    number: Int,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(64.dp)
            .clip(CircleShape)
            .background(Color(0xFF1F1F1F))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = number.toString(),
            fontSize = 22.sp,
            color = Color.White
        )
    }
}

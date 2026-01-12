package com.example.henshinphone.feature

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import kotlinx.coroutines.delay

@Composable
fun TransformationScreen(
    code: String
) {
    // 发光脉冲动画
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")

    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
    )

    val scale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {

        // 红色能量光晕
        Box(
            modifier = Modifier
                .size(280.dp)
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                    alpha = glowAlpha
                }
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFFFF1744),
                            Color.Transparent
                        )
                    ),
                    shape = CircleShape
                )
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 555 数字
            Text(
                text = code,
                fontSize = 72.sp,
                fontWeight = FontWeight.Black,
                color = Color(0xFFFF1744),
                modifier = Modifier
                    .shadow(
                        elevation = 20.dp,
                        shape = RoundedCornerShape(8.dp),
                        ambientColor = Color.Red,
                        spotColor = Color.Red
                    )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "TRANSFORMATION",
                color = Color.White,
                fontSize = 14.sp,
                letterSpacing = 2.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "IN PROGRESS",
                color = Color(0xFFFF5252),
                fontSize = 12.sp,
                letterSpacing = 3.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 能量扫描条
            LinearProgressIndicator(
                modifier = Modifier
                    .width(180.dp)
                    .height(6.dp),
                color = Color(0xFFFF1744),
                trackColor = Color.DarkGray
            )
        }
    }
}

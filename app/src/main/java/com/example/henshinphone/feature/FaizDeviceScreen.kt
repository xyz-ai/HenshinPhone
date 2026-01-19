package com.example.henshinphone.feature

import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import com.example.henshinphone.R
import kotlin.math.roundToInt

// ================================
// 🔢 数字热区定义（纯透明）
// ================================

private data class HitArea(
    val digit: String,
    val cx: Float,
    val cy: Float,
    val w: Float,
    val h: Float
)

/**
 * Faiz Phone 真机风格热区
 * —— 只对应图片上的实体按键
 */
private fun buildFaizKeypadHitAreas(): List<HitArea> {

    // 👉 键盘整体区域（右侧）
    val left = 0.66f
    val right = 0.93f
    val top = 0.30f
    val bottom = 0.80f

    val cols = 3
    val rows = 4

    val cellW = (right - left) / cols
    val cellH = (bottom - top) / rows

    // 按键稍微小于格子，避免误触边框
    val keyW = cellW * 0.72f
    val keyH = cellH * 0.72f

    val layout = listOf(
        listOf("1", "2", "3"),
        listOf("4", "5", "6"),
        listOf("7", "8", "9"),
        listOf("", "0", "")
    )

    return buildList {
        layout.forEachIndexed { row, line ->
            line.forEachIndexed { col, digit ->
                if (digit.isNotEmpty()) {
                    add(
                        HitArea(
                            digit = digit,
                            cx = left + cellW * (col + 0.5f),
                            cy = top + cellH * (row + 0.5f),
                            w = keyW,
                            h = keyH
                        )
                    )
                }
            }
        }
    }
}

// ================================
// 📱 Faiz Device Screen（最终版）
// ================================

@Composable
fun FaizDeviceScreen(
    onTransformSuccess: (TransformationRule) -> Unit
) {
    var inputCode by remember { mutableStateOf("") }
    var imageSize by remember { mutableStateOf(IntSize.Zero) }
    val hitAreas = remember { buildFaizKeypadHitAreas() }
    val density = LocalDensity.current

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // =========================
        // 腰带主体（含真实键盘）
        // =========================
        Image(
            painter = painterResource(R.drawable.faiz_phone_base),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .onSizeChanged { imageSize = it },
            contentScale = ContentScale.Fit
        )

        // =========================
        // 透明热区层
        // =========================
        if (imageSize != IntSize.Zero) {

            val wDp = with(density) { imageSize.width.toDp() }
            val hDp = with(density) { imageSize.height.toDp() }

            Box(
                modifier = Modifier.size(wDp, hDp)
            ) {
                hitAreas.forEach { area ->

                    val areaWpx = imageSize.width * area.w
                    val areaHpx = imageSize.height * area.h

                    val offsetX =
                        imageSize.width * area.cx - areaWpx / 2f
                    val offsetY =
                        imageSize.height * area.cy - areaHpx / 2f

                    Box(
                        modifier = Modifier
                            .offset {
                                IntOffset(
                                    offsetX.roundToInt(),
                                    offsetY.roundToInt()
                                )
                            }
                            .size(
                                with(density) { areaWpx.toDp() },
                                with(density) { areaHpx.toDp() }
                            )
                            .background(Color.Red.copy(alpha = 0.35f))
                            .clickable {
                                inputCode += area.digit

                                // === 命中检测 ===
                                TransformationRepository
                                    .findRule(BeltType.FAIZ, inputCode)
                                    ?.let { rule ->
                                        inputCode = ""
                                        onTransformSuccess(rule)
                                    }
                            }
                    )
                }
            }
        }
    }
}

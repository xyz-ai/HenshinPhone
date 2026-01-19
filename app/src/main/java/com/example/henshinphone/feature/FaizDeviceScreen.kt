package com.example.henshinphone.feature

import androidx.compose.foundation.Image
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

// ===== 数字热区定义 =====

private data class HitArea(
    val digit: String,
    val centerXRatio: Float,
    val centerYRatio: Float,
    val widthRatio: Float,
    val heightRatio: Float
)

private fun buildFaizHitAreas(): List<HitArea> {

    // 基于 Faiz Phone 外观的经验比例
    val left = 0.26f
    val right = 0.74f
    val top = 0.56f
    val bottom = 0.88f

    val columns = 3
    val rows = 4

    val cellWidth = (right - left) / columns
    val cellHeight = (bottom - top) / rows

    val keyWidth = cellWidth * 0.8f
    val keyHeight = cellHeight * 0.75f

    val digits = listOf(
        listOf("1", "2", "3"),
        listOf("4", "5", "6"),
        listOf("7", "8", "9"),
        listOf("", "0", "")
    )

    return buildList {
        digits.forEachIndexed { rowIndex, row ->
            row.forEachIndexed { colIndex, digit ->
                if (digit.isNotEmpty()) {
                    add(
                        HitArea(
                            digit = digit,
                            centerXRatio = left + cellWidth * (colIndex + 0.5f),
                            centerYRatio = top + cellHeight * (rowIndex + 0.5f),
                            widthRatio = keyWidth,
                            heightRatio = keyHeight
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun FaizDeviceScreen(
    onTransformSuccess: (TransformationRule) -> Unit
) {
    var inputCode by remember { mutableStateOf("") }
    var imageSize by remember { mutableStateOf(IntSize.Zero) }
    val hitAreas = remember { buildFaizHitAreas() }
    val density = LocalDensity.current

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(R.drawable.faiz_phone_base),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .onSizeChanged { imageSize = it },
            contentScale = ContentScale.Fit
        )

        if (imageSize != IntSize.Zero) {
            val widthDp = with(density) { imageSize.width.toDp() }
            val heightDp = with(density) { imageSize.height.toDp() }

            Box(
                modifier = Modifier.size(widthDp, heightDp)
            ) {
                hitAreas.forEach { area ->
                    val areaWidthPx = imageSize.width * area.widthRatio
                    val areaHeightPx = imageSize.height * area.heightRatio

                    val offsetXPx =
                        imageSize.width * area.centerXRatio - areaWidthPx / 2f
                    val offsetYPx =
                        imageSize.height * area.centerYRatio - areaHeightPx / 2f

                    Box(
                        modifier = Modifier
                            .offset {
                                IntOffset(
                                    x = offsetXPx.roundToInt(),
                                    y = offsetYPx.roundToInt()
                                )
                            }
                            .size(
                                with(density) { areaWidthPx.toDp() },
                                with(density) { areaHeightPx.toDp() }
                            )
                            .clickable {
                                inputCode += area.digit

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

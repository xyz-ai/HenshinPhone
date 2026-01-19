package com.example.henshinphone.feature

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import com.example.henshinphone.R
import com.example.henshinphone.data.BeltType
import com.example.henshinphone.data.TransformationRepository
import com.example.henshinphone.data.TransformationRule
import kotlin.math.roundToInt

private data class HitArea(
    val digit: String,
    val centerXRatio: Float,
    val centerYRatio: Float,
    val widthRatio: Float,
    val heightRatio: Float
)

private data class KeypadRatios(
    val left: Float,
    val right: Float,
    val top: Float,
    val bottom: Float,
    val keyWidthScale: Float,
    val keyHeightScale: Float
)

private fun buildFaizHitAreas(): List<HitArea> {
    val keypad = KeypadRatios(
        left = 0.26f,
        right = 0.74f,
        top = 0.56f,
        bottom = 0.88f,
        keyWidthScale = 0.8f,
        keyHeightScale = 0.75f
    )

    val columns = 3
    val rows = 4
    val cellWidth = (keypad.right - keypad.left) / columns
    val cellHeight = (keypad.bottom - keypad.top) / rows
    val keyWidth = cellWidth * keypad.keyWidthScale
    val keyHeight = cellHeight * keypad.keyHeightScale

    val digits = listOf(
        listOf("1", "2", "3"),
        listOf("4", "5", "6"),
        listOf("7", "8", "9"),
        listOf("", "0", "")
    )

    return buildList {
        digits.forEachIndexed { rowIndex, row ->
            row.forEachIndexed { columnIndex, digit ->
                if (digit.isNotEmpty()) {
                    val centerX = keypad.left + cellWidth * (columnIndex + 0.5f)
                    val centerY = keypad.top + cellHeight * (rowIndex + 0.5f)
                    add(
                        HitArea(
                            digit = digit,
                            centerXRatio = centerX,
                            centerYRatio = centerY,
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
            val imageWidthDp = with(density) { imageSize.width.toDp() }
            val imageHeightDp = with(density) { imageSize.height.toDp() }
            Box(
                modifier = Modifier
                    .size(imageWidthDp, imageHeightDp)
                    .align(Alignment.Center)
            ) {
                hitAreas.forEach { area ->
                    val areaWidthPx = imageSize.width * area.widthRatio
                    val areaHeightPx = imageSize.height * area.heightRatio
                    val offsetXPx = imageSize.width * area.centerXRatio - areaWidthPx / 2f
                    val offsetYPx = imageSize.height * area.centerYRatio - areaHeightPx / 2f
                    val areaWidthDp = with(density) { areaWidthPx.toDp() }
                    val areaHeightDp = with(density) { areaHeightPx.toDp() }

                    Box(
                        modifier = Modifier
                            .offset {
                                IntOffset(
                                    x = offsetXPx.roundToInt(),
                                    y = offsetYPx.roundToInt()
                                )
                            }
                            .size(areaWidthDp, areaHeightDp)
                            .clickable {
                                inputCode += area.digit
                                val rule = TransformationRepository.findByCode(
                                    belt = BeltType.FAIZ,
                                    code = inputCode
                                )
                                if (rule != null) {
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

package com.example.henshinphone.feature

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.unit.IntSize
import com.example.henshinphone.R

private data class KeyHitArea(
    val digit: Int,
    val leftRatio: Float,
    val topRatio: Float,
    val rightRatio: Float,
    val bottomRatio: Float
)

private val faizKeypadAreas = run {
    val keypadLeft = 0.24f
    val keypadRight = 0.76f
    val keypadTop = 0.56f
    val keypadBottom = 0.90f
    val columns = 3
    val rows = 4
    val cellWidth = (keypadRight - keypadLeft) / columns
    val cellHeight = (keypadBottom - keypadTop) / rows

    val digits = listOf(
        1, 2, 3,
        4, 5, 6,
        7, 8, 9,
        -1, 0, -1
    )

    digits.mapIndexedNotNull { index, digit ->
        if (digit == -1) return@mapIndexedNotNull null
        val row = index / columns
        val column = index % columns
        val left = keypadLeft + column * cellWidth
        val top = keypadTop + row * cellHeight
        KeyHitArea(
            digit = digit,
            leftRatio = left,
            topRatio = top,
            rightRatio = left + cellWidth,
            bottomRatio = top + cellHeight
        )
    }
}

@Composable
fun FaizDeviceScreen(
    onTransformSuccess: (TransformationRule) -> Unit
) {
    var inputCode by remember { mutableStateOf("") }
    var imageSize by remember { mutableStateOf(IntSize.Zero) }
    val density = LocalDensity.current

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Image(
            painter = painterResource(id = R.drawable.faiz_phone_base),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxSize(0.9f)
                .onSizeChanged { size ->
                    imageSize = size
                }
        )

        if (imageSize != IntSize.Zero) {
            val imageWidth = with(density) { imageSize.width.toDp() }
            val imageHeight = with(density) { imageSize.height.toDp() }
            val interactionSource = remember { MutableInteractionSource() }

            Box(modifier = Modifier.size(imageWidth, imageHeight)) {
                faizKeypadAreas.forEach { area ->
                    val left = with(density) { (imageSize.width * area.leftRatio).toDp() }
                    val top = with(density) { (imageSize.height * area.topRatio).toDp() }
                    val width = with(density) {
                        (imageSize.width * (area.rightRatio - area.leftRatio)).toDp()
                    }
                    val height = with(density) {
                        (imageSize.height * (area.bottomRatio - area.topRatio)).toDp()
                    }

                    Box(
                        modifier = Modifier
                            .offset(x = left, y = top)
                            .size(width, height)
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null
                            ) {
                                inputCode += area.digit.toString()
                                val rule = TransformationRepository.findByCode(
                                    belt = BeltType.FAIZ,
                                    code = inputCode
                                )
                                if (rule != null) {
                                    onTransformSuccess(rule)
                                    inputCode = ""
                                }
                            }
                    )
                }
            }
        }
    }
}

private fun TransformationRepository.findByCode(
    belt: BeltType,
    code: String
): TransformationRule? = findRule(belt, code)

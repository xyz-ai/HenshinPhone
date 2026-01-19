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
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.example.henshinphone.R
import com.example.henshinphone.data.TransformationRepository
import com.example.henshinphone.data.TransformationRule
import kotlin.math.roundToInt

private data class HitArea(
    val digit: Int,
    val leftRatio: Float,
    val topRatio: Float,
    val widthRatio: Float,
    val heightRatio: Float
)

@Composable
fun FaizDeviceScreen(
    onTransformSuccess: (TransformationRule) -> Unit
) {
    var inputCode by remember { mutableStateOf("") }
    var imageSize by remember { mutableStateOf(IntSize.Zero) }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.faiz_phone_base),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxSize(0.85f)
                .onSizeChanged { size -> imageSize = size }
        )

        if (imageSize != IntSize.Zero) {
            val density = LocalDensity.current
            val imageWidthDp = with(density) { imageSize.width.toDp() }
            val imageHeightDp = with(density) { imageSize.height.toDp() }
            val hitAreas = remember { buildFaizKeypadHitAreas() }

            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(imageWidthDp, imageHeightDp)
            ) {
                hitAreas.forEach { area ->
                    val leftPx = imageSize.width * area.leftRatio
                    val topPx = imageSize.height * area.topRatio
                    val widthPx = imageSize.width * area.widthRatio
                    val heightPx = imageSize.height * area.heightRatio

                    Box(
                        modifier = Modifier
                            .size(
                                with(density) { widthPx.toDp() },
                                with(density) { heightPx.toDp() }
                            )
                            .offset { IntOffset(leftPx.roundToInt(), topPx.roundToInt()) }
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                inputCode += area.digit.toString()
                                TransformationRepository.findByCode(
                                    belt = BeltType.FAIZ,
                                    code = inputCode
                                )?.let { rule ->
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

private fun buildFaizKeypadHitAreas(): List<HitArea> {
    val keypadLeft = 0.25f
    val keypadTop = 0.47f
    val keypadWidth = 0.5f
    val keypadHeight = 0.38f

    val columnWidth = keypadWidth / 3f
    val rowHeight = keypadHeight / 4f

    val hitAreas = mutableListOf<HitArea>()
    var digit = 1

    repeat(3) { row ->
        repeat(3) { column ->
            hitAreas += HitArea(
                digit = digit,
                leftRatio = keypadLeft + column * columnWidth,
                topRatio = keypadTop + row * rowHeight,
                widthRatio = columnWidth,
                heightRatio = rowHeight
            )
            digit += 1
        }
    }

    hitAreas += HitArea(
        digit = 0,
        leftRatio = keypadLeft + columnWidth,
        topRatio = keypadTop + rowHeight * 3f,
        widthRatio = columnWidth,
        heightRatio = rowHeight
    )

    return hitAreas
}

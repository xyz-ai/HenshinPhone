package com.example.henshinphone.feature

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.example.henshinphone.R
import kotlin.math.roundToInt
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.foundation.gestures.detectTapGestures


// 🔧 数字键整体 Y 方向微调（负数 = 向上）
private const val KEY_Y_OFFSET = -0.2f
private const val KEY_X_OFFSET = 0f

// 🧪 是否显示热区调试层
private const val SHOW_KEY_DEBUG = false

/* ---------------------------------------------------
 * 原始图片分辨率（与你取点用的图一致）
 * --------------------------------------------------- */
private const val BASE_W = 2048f
private const val BASE_H = 768f

/* ---------------------------------------------------
 * 热区数据结构
 * --------------------------------------------------- */
private data class KeyHit(
    val digit: String,
    val cx: Float, // 0~1
    val cy: Float  // 0~1
)

/* ---------------------------------------------------
 * Faiz 数字键（你已精修好的版本）
 * --------------------------------------------------- */
private val faizKeys = run {
    val x4 = 1499f
    val y4 = 494f
    val x5 = 1623f
    val y7 = 594f

    val dx = x5 - x4
    val dy = y7 - y4

    val x7 = 1503f
    val y7Anchor = 594f

    fun key(d: String, x: Float, y: Float) =
        KeyHit(d, x / BASE_W, y / BASE_H)

    listOf(
        key("1", x7,            y7Anchor - 1.7f * dy),
        key("2", x7 + 0.9f*dx,  y7Anchor - 1.7f * dy),
        key("3", x7 + 1.8f*dx,  y7Anchor - 1.7f * dy),

        key("4", x7,            y7Anchor - dy),
        key("5", x7 + 0.9f*dx,  y7Anchor - dy),
        key("6", x7 + 1.8f*dx,  y7Anchor - dy),

        key("7", x7,            y7Anchor),
        key("8", x7 + 0.9f*dx,  y7Anchor),
        key("9", x7 + 1.8f*dx,  y7Anchor),
    )
}

/* ---------------------------------------------------
 * 主界面
 * --------------------------------------------------- */
@Composable
fun FaizDeviceScreen(
    onTransformSuccess: (TransformationRule) -> Unit
) {
    var imageSize by remember { mutableStateOf(IntSize.Zero) }
    var inputCode by remember { mutableStateOf("") }

    val density = LocalDensity.current
    val haptic = LocalHapticFeedback.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {

        Box(modifier = Modifier.fillMaxWidth(0.95f)) {

            Image(
                painter = painterResource(R.drawable.faiz_phone_base),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .onSizeChanged { imageSize = it },
                contentScale = ContentScale.FillWidth
            )

            if (imageSize != IntSize.Zero) {

                val hitRadiusPx = imageSize.width * 0.02f   // 🔽 比之前更精致

                faizKeys.forEach { key ->

                    var pressed by remember { mutableStateOf(false) }

                    val scale by animateFloatAsState(
                        targetValue = if (pressed) 0.88f else 1f,
                        animationSpec = tween(durationMillis = 80),
                        label = "keyScale"
                    )

                    val cxPx = imageSize.width * (key.cx + KEY_X_OFFSET)
                    val cyPx = imageSize.height * (key.cy + KEY_Y_OFFSET)

                    Box(
                        modifier = Modifier
                            .offset {
                                IntOffset(
                                    (cxPx - hitRadiusPx).roundToInt(),
                                    (cyPx - hitRadiusPx).roundToInt()
                                )
                            }
                            .size(with(density) { (hitRadiusPx * 2).toDp() })
                            .graphicsLayer {
                                scaleX = scale
                                scaleY = scale
                            }
                            .then(
                                if (SHOW_KEY_DEBUG || pressed) {
                                    Modifier.background(
                                        Color.Red.copy(
                                            alpha = if (pressed) 0.6f else 0.35f
                                        ),
                                        shape = CircleShape
                                    )
                                } else Modifier
                            )
                            .pointerInput(Unit) {
                                detectTapGestures(
                                    onPress = {
                                        pressed = true
                                        haptic.performHapticFeedback(
                                            HapticFeedbackType.TextHandleMove
                                        )
                                        tryAwaitRelease()
                                        pressed = false
                                    },
                                    onTap = {
                                        inputCode += key.digit
                                        TransformationRepository
                                            .findRule(BeltType.FAIZ, inputCode)
                                            ?.let { rule ->
                                                inputCode = ""
                                                onTransformSuccess(rule)
                                            }
                                    }
                                )
                            }
                    )
                }
            }
        }

        Text(
            text = "inputCode=$inputCode",
            color = Color.White,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
        )
    }
}

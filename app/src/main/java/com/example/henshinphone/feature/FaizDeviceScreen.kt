package com.example.henshinphone.feature

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.henshinphone.R
import kotlin.math.roundToInt
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import android.media.MediaPlayer
import androidx.compose.ui.platform.LocalContext
import com.example.henshinphone.feature.rule.TransformationRule

// 🔧 已冻结参数（不要再改）
private const val KEY_Y_OFFSET = -0.2f
private const val KEY_X_OFFSET = 0f
private const val SHOW_KEY_DEBUG = false
private const val SHOW_SCREEN_DEBUG = false

private const val BASE_W = 2048f
private const val BASE_H = 768f

private data class KeyHit(
    val digit: String,
    val cx: Float,
    val cy: Float,
)

/* ------------------------------
 * 已冻结的 Faiz 键位
 * ------------------------------ */
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
        key("1", x7,             y7Anchor - 1.7f * dy),
        key("2", x7 + 0.9f * dx, y7Anchor - 1.7f * dy),
        key("3", x7 + 1.8f * dx, y7Anchor - 1.7f * dy),

        key("4", x7,             y7Anchor - dy),
        key("5", x7 + 0.9f * dx, y7Anchor - dy),
        key("6", x7 + 1.8f * dx, y7Anchor - dy),

        key("7", x7,             y7Anchor),
        key("8", x7 + 0.9f * dx, y7Anchor),
        key("9", x7 + 1.8f * dx, y7Anchor),
    )
}

@Composable
fun FaizDeviceScreen(
    onTransformSuccess: (TransformationRule) -> Unit
) {
    var imageSize by remember { mutableStateOf(IntSize.Zero) }
    var inputCode by remember { mutableStateOf("") }
    val context = LocalContext.current

    val keyClickPlayer = remember {
        MediaPlayer.create(context, R.raw.faiz_key_click)
    }

    val density = LocalDensity.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(0.95f)
        ) {

            /* ---------------- 腰带底图 ---------------- */
            Image(
                painter = painterResource(R.drawable.faiz_phone_base),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .onSizeChanged { imageSize = it },
                contentScale = ContentScale.FillWidth
            )

            if (imageSize != IntSize.Zero) {

                /* =====================================================
                 * ① 中间屏幕反馈（只在这里！）
                 * ===================================================== */
                val screenLeft = imageSize.width * 0.33f
                val screenTop = imageSize.height * 0.315f
                val screenWidth = imageSize.width * 0.35f
                val screenHeight = imageSize.height * 0.3f

                Box(
                    modifier = Modifier
                        .offset {
                            IntOffset(
                                screenLeft.roundToInt(),
                                screenTop.roundToInt()
                            )
                        }
                        .width(with(density) { screenWidth.toDp() })
                        .height(with(density) { screenHeight.toDp() })
                ) {

                    // 🔴 屏幕泛光
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .then(
                                if (SHOW_SCREEN_DEBUG) {
                                    Modifier.background(
                                        Color.Red.copy(alpha = 0.25f)
                                    )
                                } else {
                                    Modifier
                                }
                            )

                    )

                    // ➖ 扫描线
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(2.dp)
                            .align(Alignment.Center)
                            .then(
                                if (SHOW_SCREEN_DEBUG) {
                                    Modifier.background(
                                        Color.Red.copy(alpha = 0.8f)
                                    )
                                } else {
                                    Modifier
                                }
                            )

                    )

                    // 🔢 输入数字显示（最多 3 位）
                    Text(
                        text = inputCode.takeLast(3),
                        color = Color(0xFFFF3B3B),
                        fontSize = 65.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 15.sp,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                /* =====================================================
                 * ② 右侧按键热区（完全冻结）
                 * ===================================================== */
                val hitRadiusPx = imageSize.width * 0.025f

                faizKeys.forEach { key ->
                    val cxPx = imageSize.width * (key.cx + KEY_X_OFFSET)
                    val cyPx = imageSize.height * (key.cy + KEY_Y_OFFSET)
                    val haptic = LocalHapticFeedback.current
                    Box(
                        modifier = Modifier
                            .offset {
                                IntOffset(
                                    (cxPx - hitRadiusPx).roundToInt(),
                                    (cyPx - hitRadiusPx).roundToInt()
                                )
                            }
                            .size(
                                with(density) { (hitRadiusPx * 2).toDp() }
                            )
                            .then(
                                if (SHOW_KEY_DEBUG) {
                                    Modifier.background(
                                        Color.Red.copy(alpha = 0.35f),
                                        shape = CircleShape
                                    )
                                } else Modifier
                            )
                            .clickable {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                // 🔊 播放按键音
                                if (keyClickPlayer.isPlaying) {
                                    keyClickPlayer.seekTo(0)
                                }
                                keyClickPlayer.start()

                                inputCode += key.digit

                                if (inputCode.length > 3) {
                                    inputCode = inputCode.takeLast(3)
                                }

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

        /* -------- 调试显示 -------- */
        Text(
            text = "inputCode=$inputCode",
            color = Color.White,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
        )
    }
}

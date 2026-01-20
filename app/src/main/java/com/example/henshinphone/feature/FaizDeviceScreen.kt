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
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.example.henshinphone.R
import kotlin.math.roundToInt

// 🔧 数字键整体 Y 方向微调（负数 = 向上）
private const val KEY_Y_OFFSET = -0.2f
private const val KEY_X_OFFSET = -0.0f
// 🧪 是否显示热区调试层
private const val SHOW_KEY_DEBUG = true

/* ---------------------------------------------------
 * 1️⃣ 原始图片分辨率（非常关键）
 * 👉 必须与你拿坐标时用的那张图一致
 * --------------------------------------------------- */
private const val BASE_W = 2048f
private const val BASE_H = 768f

/* ---------------------------------------------------
 * 2️⃣ 热区数据结构（图片归一化坐标）
 * --------------------------------------------------- */
private data class KeyHit(
    val digit: String,
    val cx: Float, // 0~1
    val cy: Float, // 0~1
)

/* ---------------------------------------------------
 * 3️⃣ Faiz 数字键（使用你给的真实坐标）
 * --------------------------------------------------- */
private val faizKeys = run {
    // 以 4/5 作为左右间距标准，以 4/7 作为上下间距标准
    val x4 = 1499f
    val y4 = 494f
    val x5 = 1623f
    val y7 = 594f

    val dx = x5 - x4           // 左右间距
    val dy = y7 - y4           // 上下间距

    // 以 7 作为“位置标准”（锚点）
    val x7 = 1503f
    val y7Anchor = 594f

    fun key(d: String, x: Float, y: Float) =
        KeyHit(d, x / BASE_W, y / BASE_H)

    listOf(
        // 第一排（在 7 的上两排）
        key("1", x7,         y7Anchor - 1.7f * dy),
        key("2", x7 + 0.9f*dx,    y7Anchor - 1.7f * dy),
        key("3", x7 + 1.8f*dx,  y7Anchor - 1.7f * dy),

        // 第二排（在 7 的上一排）
        key("4", x7,         y7Anchor - dy),
        key("5", x7 + 0.9f*dx,    y7Anchor - dy),
        key("6", x7 + 1.9f*dx,  y7Anchor - 0.9f*dy),

        // 第三排（7 所在排）
        key("7", 0.99f*x7,         0.99f*y7Anchor),
        key("8", x7 + 0.9f*dx,    0.99f*y7Anchor),
        key("9", x7 + 1.8f*dx,  0.99f*y7Anchor)
    )
}

/* ---------------------------------------------------
 * 4️⃣ 主界面
 * --------------------------------------------------- */
@Composable
fun FaizDeviceScreen(
    onTransformSuccess: (TransformationRule) -> Unit
) {
    var imageSize by remember { mutableStateOf(IntSize.Zero) }
    var inputCode by remember { mutableStateOf("") }

    val density = LocalDensity.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {

        /* ===============================
         * 腰带容器：唯一坐标系
         * =============================== */
        Box(
            modifier = Modifier
                .fillMaxWidth(0.95f)
        ) {

            /* -------- 腰带图片 -------- */
            Image(
                painter = painterResource(R.drawable.faiz_phone_base),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .onSizeChanged { imageSize = it },
                contentScale = ContentScale.FillWidth
            )

            /* -------- 热区层（跟着图片） -------- */
            if (imageSize != IntSize.Zero) {

                val hitRadiusPx = imageSize.width * 0.025f

                faizKeys.forEach { key ->

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
                            .size(
                                with(density) { (hitRadiusPx * 2).toDp() }
                            )
                            // 🔴 调试可视化（以后可以删）
                            .then(
                                if (SHOW_KEY_DEBUG) {
                                    Modifier.background(
                                        Color.Red.copy(alpha = 0.35f),
                                        shape = CircleShape
                                    )
                                } else {
                                    Modifier
                                }
                            )

                            .clickable {
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
            }
        }

        /* -------- 调试显示输入 -------- */
        Text(
            text = "inputCode=$inputCode",
            color = Color.White,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
        )
    }
}

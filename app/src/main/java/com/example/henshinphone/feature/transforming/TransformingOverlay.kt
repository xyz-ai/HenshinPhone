package com.example.henshinphone.feature.transforming

import android.net.Uri
import android.widget.VideoView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.example.henshinphone.feature.AppSettings
import com.example.henshinphone.feature.rule.TransformationRule
import kotlinx.coroutines.delay

private enum class Phase {
    PRELUDE,
    VIDEO,
    DONE
}

@Composable
fun TransformingOverlay(
    rule: TransformationRule,
    onFinished: () -> Unit
) {
    val context = LocalContext.current

    var phase by remember { mutableStateOf(Phase.PRELUDE) }
    var flashAlpha by remember { mutableStateOf(0f) }

    // ✅ 关键：videoUri 为空时，不进入 VIDEO，直接结束（避免 NPE）
    val videoUriStr = rule.videoUri

    // ===== 前摇 & 闪光 =====
    LaunchedEffect(Unit) {

        flashAlpha = 1f
        delay(120)
        flashAlpha = 0f

        delay(600)

        flashAlpha = 1f
        delay(200)
        flashAlpha = 0f

        // 前摇总时长 ≈ 2s
        delay(1800)

        phase = if (videoUriStr.isNullOrBlank()) Phase.DONE else Phase.VIDEO
    }

    // DONE：立即收尾
    LaunchedEffect(phase) {
        if (phase == Phase.DONE) {
            onFinished()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {

        // ===== 闪光层 =====
        if (flashAlpha > 0f) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White.copy(alpha = flashAlpha))
            )
        }

        // ===== 视频播放 =====
        if (phase == Phase.VIDEO && !videoUriStr.isNullOrBlank()) {
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { ctx ->
                    VideoView(ctx).apply {

                        // ✅ 这里保证非空再 parse
                        setVideoURI(Uri.parse(videoUriStr))

                        setOnPreparedListener { mp ->
                            val volume = if (AppSettings.soundEnabled) 1f else 0f
                            start()
                            mp.setVolume(volume, volume)

                        }

                        setOnCompletionListener {
                            onFinished()
                        }

                        setOnErrorListener { _, _, _ ->
                            // ✅ 播放失败也别崩，直接结束
                            onFinished()
                            true
                        }
                    }
                }
            )
        }
    }
}

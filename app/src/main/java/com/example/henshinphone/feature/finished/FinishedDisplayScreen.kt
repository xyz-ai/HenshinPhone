package com.example.henshinphone.feature.finished

import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Text
import com.example.henshinphone.R
import com.example.henshinphone.feature.rule.TransformationRule

@Composable
fun FinishedDisplayScreen(
    rule: TransformationRule,
    onBackToSelector: () -> Unit
) {
    var showBack by remember { mutableStateOf(false) }
    val context = LocalContext.current

    // ✅ 安全加载 Bitmap
    val finishBitmap = remember(rule.finishImageUri) {
        rule.finishImageUri?.let { uriStr ->
            try {
                val uri = Uri.parse(uriStr)
                context.contentResolver.openInputStream(uri)?.use { stream ->
                    BitmapFactory.decodeStream(stream)
                }
            } catch (e: Exception) {
                null
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .pointerInput(Unit) {
                detectTapGestures(
                    onDoubleTap = { showBack = true }
                )
            }
    ) {

        if (finishBitmap != null) {
            Image(
                bitmap = finishBitmap.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier.fillMaxHeight(),
                contentScale = ContentScale.Fit // ✅ 完整显示
            )

        } else {
            Image(
                painter = painterResource(R.drawable.faiz_finished),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        if (showBack) {
            Text(
                text = "RETURN",
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(9.dp)
                    .clickable { onBackToSelector() }
            )
        }
    }
}

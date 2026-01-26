
package com.example.henshinphone.feature.finished

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Text
import com.example.henshinphone.R

@Composable
fun FinishedDisplayScreen(
    onBackToSelector: () -> Unit
) {
    var showBack by remember { mutableStateOf(false) }

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

        Image(
            painter = painterResource(R.drawable.faiz_finished),
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )

        if (showBack) {
            Text(
                text = "RETURN",
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(24.dp)
                    .clickable { onBackToSelector() }
            )
        }
    }
}

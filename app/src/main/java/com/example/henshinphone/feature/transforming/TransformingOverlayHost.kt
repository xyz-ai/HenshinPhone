package com.example.henshinphone.feature.transforming

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.henshinphone.feature.rule.TransformationRule

@Composable
fun TransformingOverlayHost(
    content: @Composable (
        startTransform: (TransformationRule, onFinished: () -> Unit) -> Unit
    ) -> Unit
) {
    var activeRule by remember { mutableStateOf<TransformationRule?>(null) }
    var onFinishedCallback by remember { mutableStateOf<(() -> Unit)?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {

        content { rule, onFinished ->
            activeRule = rule
            onFinishedCallback = onFinished
        }

        activeRule?.let { rule ->
            TransformingOverlay(
                rule = rule,
                onFinished = {
                    activeRule = null
                    onFinishedCallback?.invoke()
                    onFinishedCallback = null
                }
            )
        }
    }
}

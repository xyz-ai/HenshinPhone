package com.example.henshinphone.feature

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun DeviceScreen(
    rules: List<TransformationRule>,
    onTransformationSelected: (TransformationRule) -> Unit,
    onOpenSettings: () -> Unit
) {
    var input by remember { mutableStateOf("") }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        // 中央数字显示
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = input,
                style = MaterialTheme.typography.headlineLarge
            )

            Spacer(modifier = Modifier.height(24.dp))

            (1..9).chunked(3).forEach { row ->
                Row {
                    row.forEach { number ->
                        Button(
                            modifier = Modifier
                                .padding(4.dp)
                                .size(64.dp),
                            onClick = {
                                input += number.toString()
                                rules.find { it.code == input }?.let {
                                    onTransformationSelected(it)
                                }
                            }
                        ) {
                            Text(number.toString())
                        }
                    }
                }
            }
        }

        // 右下角设置按钮（浮层）
        IconButton(
            onClick = onOpenSettings,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "Settings"
            )
        }
    }
}


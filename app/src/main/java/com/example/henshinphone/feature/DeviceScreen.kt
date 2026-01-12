package com.example.henshinphone.feature

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.henshinphone.data.TransformationRule

@Composable
fun DeviceScreen(
    rules: List<TransformationRule>,
    onTransformationSelected: (String) -> Unit
) {
    var input by remember { mutableStateOf("") }
    val validCodes = remember(rules) { rules.map { it.code }.toSet() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF050505))
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            // 顶部显示输入
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF151515), RoundedCornerShape(16.dp))
                    .padding(20.dp)
            ) {
                Text(
                    text = "INPUT",
                    color = Color(0xFFAAAAAA),
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = if (input.isEmpty()) "---" else input,
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // 数字键盘
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                val rows = listOf(
                    listOf("1", "2", "3"),
                    listOf("4", "5", "6"),
                    listOf("7", "8", "9"),
                    listOf("0", "ENTER")
                )

                rows.forEach { row ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        row.forEach { label ->
                            val isEnter = label == "ENTER"

                            Button(
                                onClick = {
                                    if (isEnter) {
                                        if (validCodes.contains(input)) {
                                            onTransformationSelected(input)
                                        }
                                        input = ""
                                    } else {
                                        input += label
                                    }
                                },
                                modifier = Modifier
                                    .weight(if (isEnter) 2f else 1f)
                                    .aspectRatio(if (isEnter) 2.2f else 1f),
                                shape = if (isEnter) {
                                    RoundedCornerShape(28.dp)
                                } else {
                                    CircleShape
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isEnter)
                                        Color(0xFF00C2FF)
                                    else
                                        Color(0xFF1E1E1E),
                                    contentColor = if (isEnter)
                                        Color.Black
                                    else
                                        Color.White
                                )
                            ) {
                                Text(
                                    text = label,
                                    fontSize = if (isEnter) 16.sp else 20.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

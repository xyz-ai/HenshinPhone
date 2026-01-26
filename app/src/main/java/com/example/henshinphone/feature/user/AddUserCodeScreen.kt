package com.example.henshinphone.feature.user

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.henshinphone.feature.BeltType
import com.example.henshinphone.feature.rule.TransformationRule
import com.example.henshinphone.feature.rule.UserRuleStore

@Composable
fun AddUserCodeScreen(
    onBack: () -> Unit,
    onSaved: () -> Unit
) {
    // ===============================
    // 1️⃣ 输入状态
    // ===============================
    var code by remember { mutableStateOf("") }

    // SAF 选择结果
    var videoUri by remember { mutableStateOf<Uri?>(null) }
    var finishImageUri by remember { mutableStateOf<Uri?>(null) }

    // ===============================
    // 2️⃣ SAF 启动器
    // ===============================

    // 🎥 选择 MP4
    val videoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        uri?.let {
            videoUri = it
        }
    }

    // 🖼 选择 finish.png / jpg
    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        uri?.let {
            finishImageUri = it
        }
    }

    // ===============================
    // 3️⃣ UI
    // ===============================
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(24.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {

            Text(
                text = "ADD USER CODE",
                color = Color.White,
                style = MaterialTheme.typography.headlineSmall
            )

            // ===== 输入 3 位数字密码 =====
            OutlinedTextField(
                value = code,
                onValueChange = {
                    if (it.length <= 3 && it.all { c -> c.isDigit() }) {
                        code = it
                    }
                },
                label = { Text("3-digit Code") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                singleLine = true
            )

            // ===== 选择变身动画 =====
            Button(
                onClick = {
                    videoPicker.launch(arrayOf("video/*"))
                }
            ) {
                Text(
                    text = if (videoUri == null)
                        "Select Transform Video"
                    else
                        "Transform Video Selected"
                )
            }

            // ===== 选择定格图（可选）=====
            Button(
                onClick = {
                    imagePicker.launch(arrayOf("image/*"))
                }
            ) {
                Text(
                    text = if (finishImageUri == null)
                        "Select Finish Image (Optional)"
                    else
                        "Finish Image Selected"
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // ===== 保存 =====
            Button(
                enabled = code.length == 3 && videoUri != null,
                onClick = {
                    UserRuleStore.addRule(
                        TransformationRule(
                            belt = BeltType.FAIZ,
                            code = code,
                            name = "Custom $code",
                            videoUri = videoUri!!.toString(),
                            finishImageUri = finishImageUri?.toString()
                        )
                    )
                    onSaved()
                }
            ) {
                Text("SAVE")
            }

            // ===== 取消 =====
            Button(
                onClick = onBack,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.DarkGray
                )
            ) {
                Text("CANCEL")
            }
        }
    }
}

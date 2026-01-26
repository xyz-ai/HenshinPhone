package com.example.henshinphone.feature.user
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.henshinphone.feature.BeltType
import com.example.henshinphone.feature.rule.TransformationRule
import com.example.henshinphone.feature.rule.UserRuleStore
import com.example.henshinphone.feature.storage.LocalStore

@Composable
fun AddUserCodeScreen(
    belt: BeltType,
    onBack: () -> Unit,
    onSaved: () -> Unit
) {
    val context = LocalContext.current

    var code by remember { mutableStateOf("") }
    var selectedVideoUri by remember { mutableStateOf<Uri?>(null) }
    var selectedFinishImageUri by remember { mutableStateOf<Uri?>(null) }

    val pickVideoLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
            uri?.let {
                context.contentResolver.takePersistableUriPermission(
                    it,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
                selectedVideoUri = it
            }
        }


    val pickImageLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) {
            selectedFinishImageUri = it
        }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {

            Text(
                text = "ADD USER CODE (${belt.name})",
                color = Color.White,
                style = MaterialTheme.typography.headlineSmall
            )

            OutlinedTextField(
                value = code,
                onValueChange = {
                    if (it.length <= 3 && it.all(Char::isDigit)) {
                        code = it
                    }
                },
                label = { Text("3-digit Code") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.LightGray,
                    cursorColor = Color.White
                )
            )


            Button(onClick = {
                pickVideoLauncher.launch(arrayOf("video/*"))
            }) {
                Text(selectedVideoUri?.let { "Video Selected" } ?: "Select Transform Video")
            }

            Button(onClick = {
                pickImageLauncher.launch(arrayOf("image/*"))
            }) {
                Text(selectedFinishImageUri?.let { "Image Selected" } ?: "Select Finish Image")
            }

            Button(
                enabled = code.length == 3,
                onClick = {
                    // 保存变身规则
                    UserRuleStore.addRule(
                        context,
                        TransformationRule(
                            belt = belt,
                            code = code,
                            name = "Custom $code",
                            videoUri = selectedVideoUri?.toString()
                                ?: "android.resource://com.example.henshinphone/raw/faiz_transform_video",
                            finishImageUri = selectedFinishImageUri?.toString()
                                ?: "android.resource://com.example.henshinphone/drawable/faiz_transform_main"
                        )
                    )

                    // ✅ 正确：读取 → 追加 → 保存
                    val oldCodes = LocalStore.getFaizCodes(context)
                    val newCodes = if (oldCodes.contains(code)) {
                        oldCodes
                    } else {
                        oldCodes + code
                    }
                    LocalStore.saveFaizCodes(context, newCodes)

                    onSaved()
                }
            ) {
                Text("SAVE")
            }


            Button(onClick = onBack) {
                Text("CANCEL")
            }
        }
    }
}

package com.example.henshinphone.ui

import androidx.compose.runtime.*
import androidx.compose.material3.*
import com.example.henshinphone.feature.*
import com.example.henshinphone.data.TransformationRepository

sealed class Screen {
    object Selector : Screen()
    object Device : Screen()
    data class Transform(val code: String) : Screen()
    data class Play(val code: String) : Screen()
    object Settings : Screen()
}

@Composable
fun HenshinPhoneApp() {
    var screen by remember { mutableStateOf<Screen>(Screen.Selector) }

    when (val s = screen) {

        /* 选择变身器 */
        Screen.Selector -> {
            DeviceSelectorScreen(
                onDeviceSelected = {
                    screen = Screen.Device
                },
                onOpenSettings = {
                    screen = Screen.Settings
                }
            )
        }

        /* 数字输入界面 */
        Screen.Device -> {
            DeviceScreen(
                rules = TransformationRepository.rules,
                onTransformationSelected = { code ->
                    screen = Screen.Transform(code)
                }
            )
        }

        /* 变身中（中间态，可做特效 UI） */
        is Screen.Transform -> {
            TransformationScreen(
                code = s.code
            )
            // 这里你后面可以加 LaunchedEffect 自动跳转
            LaunchedEffect(s.code) {
                screen = Screen.Play(s.code)
            }
        }

        /* 播放音效 / 视频 */
        is Screen.Play -> {
            MediaPlaybackScreen(
                code = s.code
            )
        }

        /* 设置页 */
        Screen.Settings -> {
            SettingsScreen(
                onBack = {
                    screen = Screen.Selector
                }
            )
        }
    }
}

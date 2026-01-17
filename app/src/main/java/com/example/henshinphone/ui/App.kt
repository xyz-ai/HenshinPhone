package com.example.henshinphone.ui

import androidx.compose.runtime.*
import com.example.henshinphone.feature.*

sealed class Screen {
    object Selector : Screen()
    data class Device(val belt: BeltType) : Screen()
    data class Transforming(val rule: TransformationRule) : Screen()
    data class Playback(val rule: TransformationRule) : Screen()
    object Settings : Screen()
}

@Composable
fun HenshinPhoneApp() {

    var screen by remember { mutableStateOf<Screen>(Screen.Selector) }

    when (val s = screen) {

        // ===============================
        // 1️⃣ 腰带选择界面
        // ===============================
        Screen.Selector -> DeviceSelectorScreen(
            onDeviceSelected = { belt ->
                screen = Screen.Device(belt)
            },
            onOpenSettings = {
                screen = Screen.Settings
            }
        )

        // ===============================
        // 2️⃣ 设备界面（Faiz Phone）
        // ===============================
        is Screen.Device -> DeviceScreen(
            belt = s.belt,
            onTransformationSelected = { rule ->
                screen = Screen.Transforming(rule)
            },
            onBack = {
                screen = Screen.Selector
            },
            onOpenSettings = {
                screen = Screen.Settings
            }
        )

        // ===============================
        // 3️⃣ 变身中（不可返回）
        // ===============================
        is Screen.Transforming -> TransformationScreen(
            rule = s.rule,
            onFinished = {
                screen = Screen.Playback(s.rule)
            }
        )

        // ===============================
        // 4️⃣ 变身完成 / 视频播放
        // ===============================
        is Screen.Playback -> MediaPlaybackScreen(
            rule = s.rule,
            onBack = {
                screen = Screen.Device(BeltType.FAIZ)
            }
        )

        // ===============================
        // 5️⃣ 设置界面
        // ===============================
        Screen.Settings -> SettingsScreen(
            onBack = {
                screen = Screen.Selector
            }
        )
    }
}

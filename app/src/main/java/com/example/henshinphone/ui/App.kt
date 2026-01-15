package com.example.henshinphone.ui

import androidx.compose.runtime.*
import com.example.henshinphone.feature.*

sealed class Screen {
    object Selector : Screen()
    object Device : Screen()
    data class Transforming(val rule: TransformationRule) : Screen()
    data class Playback(val rule: TransformationRule) : Screen()
    object Settings : Screen()
}

@Composable
fun HenshinPhoneApp() {

    var screen by remember { mutableStateOf<Screen>(Screen.Selector) }
    var lastNonSettings by remember { mutableStateOf<Screen>(Screen.Selector) }

    val rules = TransformationRepository.rules

    fun openSettings() {
        // 记录进入设置前的页面
        lastNonSettings = screen
        screen = Screen.Settings
    }

    fun closeSettings() {
        screen = lastNonSettings
    }

    when (val s = screen) {

        Screen.Selector -> DeviceSelectorScreen(
            onDeviceSelected = { screen = Screen.Device },
            onOpenSettings = { openSettings() }
        )

        Screen.Device -> DeviceScreen(
            rules = rules,
            onTransformationSelected = { rule ->
                screen = Screen.Transforming(rule)
            },
            onOpenSettings = { openSettings() }
        )

        is Screen.Transforming -> TransformationScreen(
            rule = s.rule,
            onFinished = {
                screen = Screen.Playback(s.rule)
            }
        )

        is Screen.Playback -> MediaPlaybackScreen(
            rule = s.rule,
            onBack = { screen = Screen.Device }
        )

        Screen.Settings -> SettingsScreen(
            onBack = { closeSettings() }
        )
    }
}

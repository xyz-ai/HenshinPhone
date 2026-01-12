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
    val rules = TransformationRepository.rules

    when (val s = screen) {

        Screen.Selector -> DeviceSelectorScreen(
            onDeviceSelected = { screen = Screen.Device },
            onOpenSettings = { screen = Screen.Settings }
        )

        Screen.Device -> DeviceScreen(
            rules = rules,
            onTransformationSelected = { rule ->
                screen = Screen.Transforming(rule)
            },
            onOpenSettings = { screen = Screen.Settings }
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
            onBack = { screen = Screen.Device }
        )
    }
}

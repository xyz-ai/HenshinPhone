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

    // ===== 轻量 Back Stack =====
    val stack = remember {
        mutableStateListOf<Screen>(Screen.Selector)
    }

    val current = stack.last()

    fun push(screen: Screen) {
        stack.add(screen)
    }

    fun pop() {
        if (stack.size > 1) {
            stack.removeLast()
        }
    }

    when (val s = current) {

        Screen.Selector -> DeviceSelectorScreen(
            onDeviceSelected = { belt ->
                push(Screen.Device(belt))
            },
            onOpenSettings = {
                push(Screen.Settings)
            }
        )

        is Screen.Device -> when (s.belt) {
            BeltType.FAIZ -> FaizDeviceScreen(
                onCodeConfirmed = { rule ->
                    push(Screen.Transforming(rule))
                },
                onOpenSettings = {
                    push(Screen.Settings)
                }
            )
            // 以后加别的腰带，只在这里加分支
            else -> {
                // 占位
            }
        }

        is Screen.Transforming -> TransformationScreen(
            rule = s.rule,
            onFinished = {
                push(Screen.Playback(s.rule))
            }
        )

        is Screen.Playback -> MediaPlaybackScreen(
            rule = s.rule,
            onBack = {
                pop() // 回到 Transforming 前的 Device
            }
        )

        Screen.Settings -> SettingsScreen(
            onBack = {
                pop() // 回到进入设置前的界面
            }
        )
    }
}

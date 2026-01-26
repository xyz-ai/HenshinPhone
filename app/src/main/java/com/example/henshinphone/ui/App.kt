package com.example.henshinphone.ui

import androidx.compose.runtime.*
import com.example.henshinphone.feature.*
import com.example.henshinphone.feature.finished.FinishedDisplayScreen
import com.example.henshinphone.feature.rule.TransformationRule
import com.example.henshinphone.feature.transforming.TransformingOverlayHost
import com.example.henshinphone.feature.user.AddUserCodeScreen
import com.example.henshinphone.feature.user.UserCodesScreen

@Composable
fun HenshinPhoneApp() {

    var screen by remember { mutableStateOf<Screen>(Screen.Selector) }
    var lastTransformRule by remember { mutableStateOf<TransformationRule?>(null) }

    TransformingOverlayHost { startTransform ->

        when (val s = screen) {

            /** =========================
             *  腰带选择界面
             *  ========================= */
            Screen.Selector -> DeviceSelectorScreen(
                onDeviceSelected = { belt ->
                    screen = Screen.Device(belt)
                },
                onOpenSettings = {
                    screen = Screen.Settings
                }
            )

            /** =========================
             *  腰带设备界面
             *  ========================= */
            is Screen.Device -> {
                when (s.belt) {
                    BeltType.FAIZ -> {
                        FaizDeviceScreen(
                            onTransformSuccess = { rule ->
                                lastTransformRule = rule
                                startTransform(rule) {
                                    screen = Screen.Finished
                                }
                            }
                        )
                    }

                    // 预留：后续腰带
                    else -> {
                        // TODO: other belts
                    }
                }
            }

            /** =========================
             *  变身完成展示
             *  ========================= */
            Screen.Finished -> {
                lastTransformRule?.let { rule ->
                    FinishedDisplayScreen(
                        rule = rule,
                        onBackToSelector = {
                            screen = Screen.Selector
                        }
                    )
                }
            }

            /** =========================
             *  设置界面
             *  ========================= */
            Screen.Settings -> {
                SettingsScreen(
                    onBack = { screen = Screen.Selector },
                    onOpenUserCodes = { screen = Screen.UserCodes }
                )
            }

            /** =========================
             *  用户密码列表（当前固定 FAIZ）
             *  ========================= */
            Screen.UserCodes -> {
                UserCodesScreen(
                    onBack = { screen = Screen.Settings },
                    onAddNew = {
                        // 当前阶段：固定从 FAIZ 进入
                        screen = Screen.AddUserCode(BeltType.FAIZ)
                    }
                )
            }

            /** =========================
             *  添加用户密码
             *  ========================= */
            is Screen.AddUserCode -> {
                AddUserCodeScreen(
                    belt = s.belt,
                    onBack = { screen = Screen.UserCodes },
                    onSaved = { screen = Screen.UserCodes }
                )
            }
        }
    }
}

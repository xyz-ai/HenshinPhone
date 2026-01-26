package com.example.henshinphone.ui

import androidx.compose.runtime.*
import com.example.henshinphone.feature.*
import com.example.henshinphone.feature.finished.FinishedDisplayScreen
import com.example.henshinphone.feature.transforming.TransformingOverlayHost
import com.example.henshinphone.feature.user.AddUserCodeScreen

/**
 * 应用入口 Composable
 * ⚠️ 这里只做“页面调度 + Overlay 挂载”
 */
@Composable
fun HenshinPhoneApp() {

    var screen by remember { mutableStateOf<Screen>(Screen.Selector) }

    // 🔴 全局变身 Overlay（不属于任何 Screen）
    TransformingOverlayHost { startTransform ->

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
            // 2️⃣ 设备界面（按腰带分发）
            // ===============================
            is Screen.Device -> {
                when (s.belt) {

                    BeltType.FAIZ -> {
                        FaizDeviceScreen(
                            onTransformSuccess = { rule ->
                                // 🔥 不切 Screen，直接触发 Overlay
                                startTransform(rule) {
                                    // 变身演出结束 → 进入定格完成态
                                    screen = Screen.Finished
                                }
                            }
                        )
                    }

                    BeltType.KAIXA -> {
                        // TODO: KaixaDeviceScreen
                    }

                    BeltType.DELTA -> {
                        // TODO: DeltaDeviceScreen
                    }
                }
            }

            // ===============================
            // 3️⃣ 变身完成定格界面
            // ===============================
            Screen.Finished -> {
                FinishedDisplayScreen(
                    onBackToSelector = {
                        screen = Screen.Selector
                    }
                )
            }

            // ===============================
            // 4️⃣ 设置界面
            // ===============================
            Screen.Settings -> {
                SettingsScreen(
                    onBack = {
                        screen = Screen.Selector
                    },
                    onOpenUserCodes = {
                        screen = Screen.UserCodes
                    }
                )
            }
            // ===============================
            // 5️⃣ 用户自定义变身密码列表
            // ===============================
            Screen.UserCodes -> {
                UserCodesScreen(
                    onBack = {
                        screen = Screen.Settings
                    },
                    onAdd = { screen = Screen.AddUserCode }
                )
            }
            // ===============================
            // 6️⃣ 添加用户自定义密码
            // ===============================
            Screen.AddUserCode -> {
                AddUserCodeScreen(
                    onBack = {
                        screen = Screen.UserCodes
                    },
                    onSaved = {
                        screen = Screen.UserCodes
                    }
                )
            }

        }
    }
}

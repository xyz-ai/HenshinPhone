package com.example.henshinphone.feature

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

/**
 * 全局应用设置（Compose 可观察）
 */
object AppSettings {

    /**
     * 是否开启声音
     * ⚠️ 必须是 mutableStateOf，否则 UI 不会刷新
     */
    var soundEnabled by mutableStateOf(true)
}

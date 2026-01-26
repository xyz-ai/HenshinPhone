package com.example.henshinphone.feature

import android.content.Context

object AppSettings {

    private const val PREF_NAME = "henshin_settings"
    private const val KEY_SOUND = "sound_enabled"

    // 默认值
    var soundEnabled: Boolean = true
        private set

    /** 必须在 App 启动时调用一次 */
    fun load(context: Context) {
        val sp = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        soundEnabled = sp.getBoolean(KEY_SOUND, true)
    }

    fun setSoundEnabled(
        context: Context,
        enabled: Boolean
    ) {
        soundEnabled = enabled
        val sp = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        sp.edit()
            .putBoolean(KEY_SOUND, enabled)
            .apply()
    }
}

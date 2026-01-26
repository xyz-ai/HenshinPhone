package com.example.henshinphone.feature.storage

import android.content.Context

object LocalStore {

    private const val PREF = "henshin_phone"
    private const val KEY_SOUND = "sound_enabled"
    private const val KEY_FAIZ_CODES = "faiz_user_codes"

    fun isSoundEnabled(ctx: Context): Boolean =
        ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE)
            .getBoolean(KEY_SOUND, true)

    fun setSoundEnabled(ctx: Context, enabled: Boolean) {
        ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE)
            .edit()
            .putBoolean(KEY_SOUND, enabled)
            .apply()
    }

    // ===== 用户自定义 Faiz 密码 =====

    fun getFaizCodes(ctx: Context): List<String> {
        val prefs = ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE)
        return prefs
            .getStringSet(KEY_FAIZ_CODES, emptySet())
            ?.toList()
            ?: emptyList()
    }

    fun saveFaizCodes(ctx: Context, codes: List<String>) {
        ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE)
            .edit()
            .putStringSet(KEY_FAIZ_CODES, codes.toSet())
            .apply()
    }

    fun clearFaizCodes(ctx: Context) {
        ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE)
            .edit()
            .remove(KEY_FAIZ_CODES)
            .apply()
    }
}

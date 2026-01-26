package com.example.henshinphone.feature.rule

import android.R
import com.example.henshinphone.feature.BeltType

data class TransformationRule(
    val belt: BeltType,
    val code: String,
    val name: String,

    // SAF / 本地 / assets 都统一用 Uri 字符串
    val videoUri: String?,

    // 可选：完成定格图
    val finishImageUri: String?
)

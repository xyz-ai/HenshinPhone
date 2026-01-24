package com.example.henshinphone.feature.rule

import com.example.henshinphone.feature.BeltType

/**
 * 一条“变身规则”
 *
 * 当前阶段：先保持最小可用字段（belt/code/name/videoAsset/soundAsset）
 * 后续阶段再逐步把 videoAsset/soundAsset 抽象成 AnimationSource / SoundSource 等。
 */
data class TransformationRule(
    val belt: BeltType,
    val code: String,
    val name: String,
    val videoAsset: String,
    val soundAsset: String
)

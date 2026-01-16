package com.example.henshinphone.feature

data class TransformationRule(
    val belt: BeltType,      // 属于哪个腰带
    val code: String,        // 数字组合，如 "555"
    val name: String,        // 形态名
    val videoAsset: String,  // MP4
    val soundAsset: String   // 音效
)

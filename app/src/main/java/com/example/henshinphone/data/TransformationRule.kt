package com.example.henshinphone.data

/**
 * 单个变身规则
 * code = 输入的数字（如 555）
 * name = 显示名称
 * videoAsset / soundAsset = 对应资源文件名
 */
data class TransformationRule(
    val code: String,
    val name: String,
    val videoAsset: String,
    val soundAsset: String
)

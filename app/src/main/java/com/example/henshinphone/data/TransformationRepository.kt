package com.example.henshinphone.data

/**
 * 变身规则仓库
 * 后续如果支持“用户自定义”，只改这里
 */
object TransformationRepository {

    val rules: List<TransformationRule> = listOf(
        TransformationRule(
            code = "555",
            name = "FAIZ TRANSFORMATION",
            videoAsset = "faiz_transformation.mp4",
            soundAsset = "faiz_transformation.mp3"
        )
    )
}

package com.example.henshinphone.feature

object TransformationRepository {

    val rules = listOf(
        TransformationRule(
            code = "555",
            name = "Faiz",
            videoAsset = "faiz_555.mp4",
            soundAsset = "faiz_start.wav"
        )
    )

    fun findByCode(code: String): TransformationRule? {
        return rules.find { it.code == code }
    }
}

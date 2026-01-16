package com.example.henshinphone.feature

object TransformationRepository {

    private val rules = listOf(

        // ===== Faiz =====
        TransformationRule(
            belt = BeltType.FAIZ,
            code = "555",
            name = "Faiz",
            videoAsset = "faiz_555.mp4",
            soundAsset = "faiz_start.wav"
        ),

        // ===== Kaixa（示例，后续你加资源即可）=====
        TransformationRule(
            belt = BeltType.KAIXA,
            code = "555",
            name = "Kaixa",
            videoAsset = "kaixa_555.mp4",
            soundAsset = "kaixa_start.wav"
        )
    )

    fun findRule(
        belt: BeltType,
        code: String
    ): TransformationRule? {
        return rules.find {
            it.belt == belt && it.code == code
        }
    }
}

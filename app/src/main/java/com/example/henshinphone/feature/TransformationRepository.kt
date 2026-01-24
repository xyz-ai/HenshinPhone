package com.example.henshinphone.feature

import com.example.henshinphone.feature.rule.TransformationRule

object TransformationRepository {

    /**
     * ⚠️ 重要原则：
     * rules 保持 private，UI 永远不直接接触底层集合
     * UI 只能调用 getRulesForBelt / findRule 这类 API
     */
    private val rules: Map<BeltType, List<TransformationRule>> = mapOf(
        BeltType.FAIZ to listOf(
            TransformationRule(
                belt = BeltType.FAIZ,
                code = "555",
                name = "Faiz",
                videoAsset = "faiz_transformation.mp4",
                soundAsset = "faiz_transformation.mp3"
            )
        )

        // 以后可扩展：
        // BeltType.KAIXA to listOf(...)
        // BeltType.DELTA to listOf(...)
    )

    /** 获取某条腰带的全部规则（只读视图） */
    fun getRulesForBelt(belt: BeltType): List<TransformationRule> {
        return rules[belt].orEmpty()
    }

    /** 按「腰带 + 输入码」查规则（核心 API） */
    fun findRule(belt: BeltType, code: String): TransformationRule? {
        return rules[belt]?.find { it.code == code }
    }
}

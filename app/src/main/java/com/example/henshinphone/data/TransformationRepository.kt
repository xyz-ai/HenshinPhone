package com.example.henshinphone.feature

object TransformationRepository {

    /**
     * ⚠️ 注意：
     * rules 是 private
     * UI 永远不能直接访问集合本身
     */
    private val rules: Map<BeltType, List<TransformationRule>> = mapOf(

        BeltType.FAIZ to listOf(
            TransformationRule(
                code = "555",
                name = "Faiz",
                videoAsset = "faiz_transformation.mp4",
                soundAsset = "faiz_transformation.mp3"
            )
        ),

        // 以后可以加
        // BeltType.KAIXA to listOf(...)
    )

    /** 获取某条腰带的全部规则（只读） */
    fun getRulesForBelt(belt: BeltType): List<TransformationRule> {
        return rules[belt].orEmpty()
    }

    /** 按“腰带 + 输入码”查规则（最常用） */
    fun findRule(
        belt: BeltType,
        code: String
    ): TransformationRule? {
        return rules[belt]?.find { it.code == code }
    }
}

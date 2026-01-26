package com.example.henshinphone.feature

import com.example.henshinphone.R
import com.example.henshinphone.feature.rule.TransformationRule
import com.example.henshinphone.feature.rule.UserRuleStore

object TransformationRepository {

    private val rules: Map<BeltType, List<TransformationRule>> = mapOf(
        BeltType.FAIZ to listOf(
            TransformationRule(
                belt = BeltType.FAIZ,
                code = "555",
                name = "Faiz",
                videoUri = "android.resource://com.example.henshinphone/raw/faiz_transform_video",
                finishImageUri = "android.resource://com.example.henshinphone/drawable/faiz_transform_main"
            )
        )
    )

    fun getRulesForBelt(belt: BeltType): List<TransformationRule> {
        return rules[belt].orEmpty()
    }

    fun findRule(belt: BeltType, code: String): TransformationRule? {
        return UserRuleStore.findRule(belt, code)
            ?: rules[belt]?.find { it.code == code }
    }
}

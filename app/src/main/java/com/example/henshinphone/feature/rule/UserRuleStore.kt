package com.example.henshinphone.feature.rule

import androidx.compose.runtime.mutableStateListOf
import com.example.henshinphone.feature.BeltType

/**
 * 用户自定义规则（内存态）
 * ⚠️ 不做持久化，后续统一接 DataStore
 */
object UserRuleStore {

    private val userRules = mutableStateListOf<TransformationRule>()

    fun addRule(rule: TransformationRule) {
        // 同腰带 + 同密码 → 覆盖
        userRules.removeAll {
            it.belt == rule.belt && it.code == rule.code
        }
        userRules.add(rule)
    }

    fun getRulesForBelt(belt: BeltType): List<TransformationRule> {
        return userRules.filter { it.belt == belt }
    }

    fun findRule(belt: BeltType, code: String): TransformationRule? {
        return userRules.find {
            it.belt == belt && it.code == code
        }
    }
}

package com.example.henshinphone.feature.rule

import android.content.Context
import com.example.henshinphone.feature.BeltType
import com.example.henshinphone.feature.storage.LocalStore

object UserRuleStore {

    private val rulesByBelt: MutableMap<BeltType, MutableList<TransformationRule>> =
        mutableMapOf()

    // ✅ 给“仅有 code 的用户规则”一个默认可执行资源，避免任何 null 资源进入变身链路
    private const val DEFAULT_FAIZ_VIDEO =
        "android.resource://com.example.henshinphone/raw/faiz_transform_video"
    private const val DEFAULT_FAIZ_FINISH =
        "android.resource://com.example.henshinphone/drawable/faiz_transform_main"

    fun loadFromLocal(context: Context) {
        rulesByBelt.clear()

        val codes = LocalStore.getFaizCodes(context)

        val list = codes.map { code ->
            TransformationRule(
                belt = BeltType.FAIZ,
                code = code,
                name = "Custom $code",
                videoUri = DEFAULT_FAIZ_VIDEO,
                finishImageUri = DEFAULT_FAIZ_FINISH
            )
        }.toMutableList()

        rulesByBelt[BeltType.FAIZ] = list
    }

    fun addRule(context: Context, rule: TransformationRule) {
        val list = rulesByBelt.getOrPut(rule.belt) { mutableListOf() }

        // ✅ 兜底：如果外部传进来仍是 null，也补默认资源，避免后续任何 NPE
        val safeRule =
            if (rule.belt == BeltType.FAIZ) {
                rule.copy(
                    videoUri = rule.videoUri ?: DEFAULT_FAIZ_VIDEO,
                    finishImageUri = rule.finishImageUri ?: DEFAULT_FAIZ_FINISH
                )
            } else {
                rule
            }

        list.removeAll { it.code == safeRule.code }
        list.add(safeRule)

        if (safeRule.belt == BeltType.FAIZ) {
            val codes = list.map { it.code }
            LocalStore.saveFaizCodes(context, codes)
        }
    }

    fun getRulesForBelt(belt: BeltType): List<TransformationRule> {
        return rulesByBelt[belt]?.toList().orEmpty()
    }

    fun findRule(belt: BeltType, code: String): TransformationRule? {
        return rulesByBelt[belt]?.find { it.code == code }
    }

    fun removeRule(context: Context, belt: BeltType, code: String) {
        val list = rulesByBelt[belt] ?: return
        list.removeAll { it.code == code }

        if (belt == BeltType.FAIZ) {
            val codes = list.map { it.code }
            LocalStore.saveFaizCodes(context, codes)
        }
    }
}

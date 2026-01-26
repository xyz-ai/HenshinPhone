package com.example.henshinphone.feature

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.henshinphone.feature.TransformationRepository
import com.example.henshinphone.feature.rule.UserRuleStore

@Composable
fun UserCodesScreen(
    belt: BeltType = BeltType.FAIZ,
    onBack: () -> Unit,
    onAdd: () -> Unit
) {
    val builtInRules = TransformationRepository.getRulesForBelt(belt)
    val userRules = UserRuleStore.getRulesForBelt(belt)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0E0E0E))
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Text(
                text = "USER CODES",
                color = Color.White,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            // ===== 内置规则 =====
            Text(
                text = "BUILT-IN",
                color = Color(0xFFAAAAAA),
                style = MaterialTheme.typography.labelLarge
            )

            builtInRules.forEach { rule ->
                CodeRow(
                    code = rule.code,
                    name = rule.name,
                    tag = "DEFAULT"
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ===== 用户规则 =====
            Text(
                text = "USER",
                color = Color(0xFFAAAAAA),
                style = MaterialTheme.typography.labelLarge
            )

            if (userRules.isEmpty()) {
                Text(
                    text = "No user codes yet",
                    color = Color(0xFF666666),
                    modifier = Modifier.padding(top = 8.dp)
                )
            } else {
                userRules.forEach { rule ->
                    CodeRow(
                        code = rule.code,
                        name = rule.name,
                        tag = "USER"
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = onAdd,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6A4FB3)
                )
            ) {
                Text("ADD CODE")
            }

            Button(
                onClick = onBack,
                modifier = Modifier.align(Alignment.Start),
                shape = RoundedCornerShape(50)
            ) {
                Text("BACK")
            }
        }
    }
}

@Composable
private fun CodeRow(
    code: String,
    name: String,
    tag: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color(0xFF1A1A1A),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Column {
            Text(
                text = code,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = name,
                color = Color(0xFF999999),
                style = MaterialTheme.typography.bodySmall
            )
        }

        Text(
            text = tag,
            color = if (tag == "USER") Color(0xFF7C4DFF) else Color(0xFF777777),
            style = MaterialTheme.typography.labelSmall
        )
    }
}

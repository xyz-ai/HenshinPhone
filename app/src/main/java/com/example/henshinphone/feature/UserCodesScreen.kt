package com.example.henshinphone.feature.user

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.henshinphone.feature.BeltType
import com.example.henshinphone.feature.storage.LocalStore
import com.example.henshinphone.feature.rule.UserRuleStore

@Composable
fun UserCodesScreen(
    onBack: () -> Unit,
    onAddNew: () -> Unit
) {
    val context = LocalContext.current

    var codes by remember { mutableStateOf<List<String>>(emptyList()) }
    var loaded by remember { mutableStateOf(false) }

    // ✅ 延迟加载，避免组合期 crash
    LaunchedEffect(Unit) {
        codes = LocalStore.getFaizCodes(context)
        loaded = true
    }

    fun refresh() {
        codes = LocalStore.getFaizCodes(context)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Text(
                text = "USER CODES (FAIZ)",
                color = Color.White,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Button(
                onClick = onAddNew,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6A4FB3))
            ) {
                Text("ADD CODE", color = Color.White)
            }

            Divider(color = Color(0xFF333333))

            if (!loaded) {
                Text("Loading...", color = Color.Gray)
            } else if (codes.isEmpty()) {
                Text("No user codes yet.", color = Color.Gray)
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(codes) { code ->
                        CodeItem(
                            code = code,
                            onDelete = {
                                UserRuleStore.removeRule(context, BeltType.FAIZ, code)
                                LocalStore.clearFaizCodes(context)
                                refresh()
                            }
                        )
                    }
                }
            }

            Button(onClick = onBack) {
                Text("BACK")
            }
        }
    }
}

@Composable
private fun CodeItem(
    code: String,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF1C1C1C), MaterialTheme.shapes.medium)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = code,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f)
        )

        Text(
            text = "DELETE",
            color = Color(0xFFFF6B6B),
            modifier = Modifier.clickable { onDelete() }
        )
    }
}

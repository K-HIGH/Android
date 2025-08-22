package com.khigh.seniormap.ui.screens.guardian.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.khigh.seniormap.ui.screens.guardian.components.GuardianData

/**
 * 보호인 목록 컴포넌트
 * 
 * @param guardians 보호인 목록 데이터
 * @param modifier 레이아웃 수정자
 * @param onGuardianClick 보호인 클릭 이벤트 콜백
 */
@Composable
fun GuardianList(
    guardians: List<GuardianData>,
    modifier: Modifier = Modifier,
    onGuardianClick: (GuardianData) -> Unit = {}
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(guardians) { guardian ->
            GuardianListItem(
                name = guardian.userName,
                location = guardian.location,
                profileImageRes = guardian.profileImageRes,
                statusIcon = if (guardian.isAtHome) Icons.Default.Home else Icons.Default.LocationOn,
                statusColor = if (guardian.isAtHome) Color(0xFF4CAF50) else Color(0xFFFF9800),
                onClick = { onGuardianClick(guardian) }
            )
        }
    }
}
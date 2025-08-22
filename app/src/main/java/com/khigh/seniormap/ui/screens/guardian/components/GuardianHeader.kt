package com.khigh.seniormap.ui.screens.guardian.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.khigh.seniormap.viewmodel.CaregiverViewModel

/**
 * 보호인 홈 화면 헤더 컴포넌트
 * 
 * @param title 헤더 제목
 * @param modifier 레이아웃 수정자
 * @param onAddClick 추가 버튼 클릭 이벤트 콜백
 */
@Composable
fun GuardianHeader(
    title: String = "나의 피보호인",
    modifier: Modifier = Modifier,
    caregiverViewModel: CaregiverViewModel
) {
    var showAddModal by remember { mutableStateOf(false) }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        
        IconButton(
            onClick = {
                showAddModal = true
            },
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "피보호인 추가",
                modifier = Modifier.size(20.dp)
            )
        }
    }
    
    // 피보호인 추가 모달
    AddCaregiverModal(
        isVisible = showAddModal,
        onDismiss = { showAddModal = false },
        caregiverViewModel = caregiverViewModel
    )
}
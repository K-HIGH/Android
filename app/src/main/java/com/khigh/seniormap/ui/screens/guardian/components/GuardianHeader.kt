package com.khigh.seniormap.ui.screens.guardian.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * 보호인 홈 화면 헤더 컴포넌트
 * 
 * @param title 헤더 제목
 * @param isEditMode 수정 모드 여부
 * @param modifier 레이아웃 수정자
 * @param onEditClick 수정 버튼 클릭 이벤트 콜백
 * @param onAddClick 추가 버튼 클릭 이벤트 콜백
 */
@Composable
fun GuardianHeader(
    title: String = "나의 피보호인",
    isEditMode: Boolean = false,
    modifier: Modifier = Modifier,
    onEditClick: () -> Unit = {},
    onAddClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 제목
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        
        // 수정 버튼과 추가 버튼
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 수정 버튼
            TextButton(
                onClick = onEditClick,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = if (isEditMode) 
                        MaterialTheme.colorScheme.primary 
                    else 
                        MaterialTheme.colorScheme.onSurface
                )
            ) {
                Text(
                    text = if (isEditMode) "완료" else "수정",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
            }
            
            // 추가 버튼
            IconButton(
                onClick = onAddClick,
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
    }
}
package com.khigh.seniormap.ui.screens.guardian.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.khigh.seniormap.R

/**
 * 보호인 지도 화면 헤더 컴포넌트
 * 
 * @param onBackClick 뒤로가기 버튼 클릭 콜백
 * @param onSearchClick 검색 버튼 클릭 콜백
 * @param onFilterClick 필터 버튼 클릭 콜백
 * @param modifier 레이아웃 수정자
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuardianMapHeader(
    onBackClick: () -> Unit,
    onSearchClick: () -> Unit,
    onFilterClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 뒤로가기 버튼
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.1f))
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "뒤로가기",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
            
            // 제목
            Text(
                text = "위치 확인",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            // 우측 버튼들
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // 검색 버튼
                IconButton(
                    onClick = onSearchClick,
                    modifier = Modifier
                        .size(30.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_search_alt),
                        contentDescription = "검색",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                
                // 필터 버튼
                IconButton(
                    onClick = onFilterClick,
                    modifier = Modifier
                        .size(30.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f))
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_filter),
                        contentDescription = "필터",
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        }
    }
} 
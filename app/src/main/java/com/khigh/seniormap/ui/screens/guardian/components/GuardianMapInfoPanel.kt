package com.khigh.seniormap.ui.screens.guardian.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import com.khigh.seniormap.ui.screens.guardian.components.GuardianData
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.khigh.seniormap.R

/**
 * 보호인 지도 화면 정보 패널 컴포넌트
 * 
 * @param selectedGuardian 현재 선택된 피보호인 ID
 * @param onGuardianSelect 피보호인 선택 콜백
 * @param onNavigateClick 경로 안내 버튼 클릭 콜백
 * @param modifier 레이아웃 수정자
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuardianMapInfoPanel(
    selectedGuardian: String?,
    onGuardianSelect: (String) -> Unit,
    onNavigateClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // 임시 피보호인 데이터 (실제로는 ViewModel에서 가져올 데이터)
    val guardians = remember {
        listOf(
            GuardianData(
                userId = "1",
                userName = "김할자",
                location = "집",
                isAtHome = true
            ),
            GuardianData(
                userId = "2",
                userName = "송진호",
                location = "마을",
                isAtHome = false
            ),
            GuardianData(
                userId = "3",
                userName = "나문희",
                location = "집",
                isAtHome = true
            )
        )
    }
    
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // 헤더
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "피보호인 목록",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                // 경로 안내 버튼
                Button(
                    onClick = onNavigateClick,
                    enabled = selectedGuardian != null,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    modifier = Modifier.height(36.dp)
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_navigate),
                        contentDescription = "경로 안내",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "경로 안내",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // 피보호인 선택 버튼들
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(vertical = 4.dp)
            ) {
                items(guardians) { guardian ->
                    GuardianSelectButton(
                        guardian = guardian,
                        isSelected = selectedGuardian == guardian.userId,
                        onClick = { onGuardianSelect(guardian.userId) }
                    )
                }
            }
            
            // 선택된 피보호인 정보
            selectedGuardian?.let { selectedId ->
                val guardian = guardians.find { it.userId == selectedId }
                guardian?.let {
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.1f))
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // 상태 아이콘
                        Icon(
                            imageVector = if (it.isAtHome) Icons.Default.Home else Icons.Default.LocationOn,
                            contentDescription = "위치 상태",
                            tint = if (it.isAtHome) Color(0xFF4CAF50) else Color(0xFFFF9800),
                            modifier = Modifier.size(24.dp)
                        )
                        
                        Spacer(modifier = Modifier.width(12.dp))
                        
                        // 정보
                        Column {
                            Text(
                                text = it.userName,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "현재 위치: ${it.location}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * 피보호인 선택 버튼 컴포넌트
 */
@Composable
private fun GuardianSelectButton(
    guardian: GuardianData,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) 
                MaterialTheme.colorScheme.primary 
            else 
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.1f),
            contentColor = if (isSelected) 
                MaterialTheme.colorScheme.onPrimary 
            else 
                MaterialTheme.colorScheme.onSurface
        ),
        modifier = Modifier.height(84.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        var flag = true // profile 유무
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = if (!flag) ImageVector.vectorResource(R.drawable.ic_profile_circle) else ImageVector.vectorResource(R.drawable.ic_profile_circle),
                contentDescription = "피보호인 프로필",
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(48.dp)
            )
            Text(
                text = guardian.userName,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
            )
        }
    }
} 
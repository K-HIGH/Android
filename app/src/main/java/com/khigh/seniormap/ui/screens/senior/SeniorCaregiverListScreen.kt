package com.khigh.seniormap.ui.screens.senior

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.khigh.seniormap.R

/**
 * 피보호인 보호인 목록 화면
 * 
 * 자신을 케어하는 보호인들의 목록을 표시합니다.
 * 
 * @param onBackClick 뒤로가기 버튼 클릭 콜백
 * @param modifier 레이아웃 수정자
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeniorCaregiverListScreen(
    onBackClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    // Mock 데이터 - 실제로는 ViewModel에서 가져올 데이터
    val caregivers = remember {
        listOf(
            CaregiverInfo(
                id = "1",
                name = "김보호",
                relationship = "딸",
                phone = "010-1234-5678",
                isActive = true,
                lastContact = "2시간 전",
                profileImageRes = R.drawable.ic_profile_circle
            ),
            CaregiverInfo(
                id = "2",
                name = "이돌봄",
                relationship = "아들",
                phone = "010-2345-6789",
                isActive = false,
                lastContact = "1일 전",
                profileImageRes = R.drawable.ic_profile_circle
            ),
            CaregiverInfo(
                id = "3",
                name = "박케어",
                relationship = "손녀",
                phone = "010-3456-7890",
                isActive = true,
                lastContact = "30분 전",
                profileImageRes = R.drawable.ic_profile_circle
            ),
            CaregiverInfo(
                id = "4",
                name = "최관리",
                relationship = "이웃",
                phone = "010-4567-8901",
                isActive = true,
                lastContact = "5시간 전",
                profileImageRes = R.drawable.ic_profile_circle
            )
        )
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            // 상단 앱바
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
                shape = RoundedCornerShape(16.dp),
                shadowElevation = 4.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.9f))
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    // 뒤로가기 버튼
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "뒤로가기",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    
                    // 제목
                    Text(
                        text = "가족 구성원",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    
                    // 빈 공간 (균형을 위해)
                    Spacer(modifier = Modifier.width(48.dp))
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            // 헤더 정보
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.1f)
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_people),
                            contentDescription = "보호인",
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        Text(
                            text = "현재 ${caregivers.size}명의 자녀와",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        
                        Text(
                            text = "연결되어 있습니다",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Text(
                            text = "자녀와 연락하거나 정보를 확인할 수 있습니다",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
            
            // 보호인 목록
            items(caregivers) { caregiver ->
                CaregiverListItem(
                    caregiver = caregiver,
                    onCallClick = { /* TODO: 전화 연결 */ },
                    onMessageClick = { /* TODO: 메시지 전송 */ },
                    onDetailClick = { /* TODO: 상세 정보 보기 */ }
                )
            }
            
            // 하단 여백
            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

/**
 * 보호인 정보 데이터 클래스
 */
data class CaregiverInfo(
    val id: String,
    val name: String,
    val relationship: String,
    val phone: String,
    val isActive: Boolean,
    val lastContact: String,
    val profileImageRes: Int
)

/**
 * 보호인 목록 아이템 컴포넌트
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CaregiverListItem(
    caregiver: CaregiverInfo,
    onCallClick: () -> Unit,
    onMessageClick: () -> Unit,
    onDetailClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onDetailClick,
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // 상단: 프로필 정보
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 프로필 이미지
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(caregiver.profileImageRes),
                        contentDescription = "${caregiver.name} 프로필",
                        modifier = Modifier.size(32.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                
                Spacer(modifier = Modifier.width(16.dp))
                
                // 기본 정보
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = caregiver.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        
                        Spacer(modifier = Modifier.width(8.dp))
                        
                        // 활성 상태 표시
                        Surface(
                            modifier = Modifier.size(8.dp),
                            shape = CircleShape,
                            color = if (caregiver.isActive) {
                                Color(0xFF4CAF50)
                            } else {
                                Color(0xFF9E9E9E)
                            }
                        ) {}
                    }
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Text(
                        text = caregiver.relationship,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    
                    Text(
                        text = "마지막 연락: ${caregiver.lastContact}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // 하단: 액션 버튼들
            // Row(
            //     modifier = Modifier.fillMaxWidth(),
            //     horizontalArrangement = Arrangement.spacedBy(8.dp)
            // ) {
            //     // 전화 버튼
            //     OutlinedButton(
            //         onClick = onCallClick,
            //         modifier = Modifier.weight(1f),
            //         colors = ButtonDefaults.outlinedButtonColors(
            //             contentColor = MaterialTheme.colorScheme.primary
            //         )
            //     ) {
            //         Icon(
            //             imageVector = Icons.Default.Call,
            //             contentDescription = "전화",
            //             modifier = Modifier.size(18.dp)
            //         )
            //         Spacer(modifier = Modifier.width(4.dp))
            //         Text(
            //             text = "전화",
            //             style = MaterialTheme.typography.labelMedium
            //         )
            //     }
                
            //     // 메시지 버튼
            //     OutlinedButton(
            //         onClick = onMessageClick,
            //         modifier = Modifier.weight(1f),
            //         colors = ButtonDefaults.outlinedButtonColors(
            //             contentColor = MaterialTheme.colorScheme.secondary
            //         )
            //     ) {
            //         Icon(
            //             imageVector = Icons.Default.Message,
            //             contentDescription = "메시지",
            //             modifier = Modifier.size(18.dp)
            //         )
            //         Spacer(modifier = Modifier.width(4.dp))
            //         Text(
            //             text = "메시지",
            //             style = MaterialTheme.typography.labelMedium
            //         )
            //     }
            // }
        }
    }
} 
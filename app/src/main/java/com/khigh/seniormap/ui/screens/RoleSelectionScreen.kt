package com.khigh.seniormap.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * 역할 선택 화면 (부모/자녀)
 * 
 * MVP 단계에서 로그인 후 첫 접근 시 사용자 역할을 선택하는 화면
 * Figma 무드보드 테마: 편안한, 안정적인, 뚜렷한, 정돈된, 여백 있는, 단순한
 */
@Composable
fun RoleSelectionScreen(
    onRoleSelected: (Boolean) -> Unit, // true: 보호자, false: 피보호인
    modifier: Modifier = Modifier
) {
    // 무드보드 컬러 팔레트
    val primaryGreen = Color(0xFF98D5B3)
    val accentBlue = Color(0xFFACB8E8) 
    val backgroundColor = Color(0xFFF5F5E5)
    val textPrimary = Color(0xFF1C1C0D)
    val textSecondary = Color(0xFF9E9E47)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // 타이틀 섹션
        Text(
            text = "SeniorMap",
            fontSize = 32.sp,
            fontWeight = FontWeight.ExtraBold,
            color = textPrimary,
            letterSpacing = 0.96.sp,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "안전한 시니어 케어 서비스",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = textSecondary,
            letterSpacing = 0.54.sp,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(48.dp))
        
        // 역할 선택 제목
        Text(
            text = "역할을 선택해주세요",
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold,
            color = textPrimary,
            letterSpacing = 0.72.sp,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // 역할 선택 버튼들
        Column(
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 보호자 선택 카드
            RoleSelectionCard(
                title = "자녀",
                subtitle = "회원님의 자녀",
                description = "회원님의 안전을\n실시간으로 확인할 수 있어요",
                icon = Icons.Default.Person,
                backgroundColor = primaryGreen,
                onClick = { onRoleSelected(true) }
            )
            
            // 피보호인 선택 카드
            RoleSelectionCard(
                title = "부모",
                subtitle = "회원님의 부모님",
                description = "안전한 외출과 응급상황 시\n빠른 도움을 받을 수 있어요",
                icon = Icons.Default.AccountCircle,
                backgroundColor = accentBlue,
                onClick = { onRoleSelected(false) }
            )
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // 안내 텍스트
        Text(
            text = "선택하신 역할에 따라 맞춤형 기능을 제공합니다",
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            color = textSecondary,
            textAlign = TextAlign.Center,
            letterSpacing = 0.42.sp
        )
    }
}

@Composable
private fun RoleSelectionCard(
    title: String,
    subtitle: String,
    description: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    backgroundColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(140.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor.copy(alpha = 0.1f)
        ),
        border = androidx.compose.foundation.BorderStroke(
            width = 2.dp,
            color = backgroundColor
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp,
            pressedElevation = 8.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 아이콘
            Card(
                modifier = Modifier.size(64.dp),
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(
                    containerColor = backgroundColor
                )
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        modifier = Modifier.size(32.dp),
                        tint = Color(0xFF1C1C0D)
                    )
                }
            }
            
            // 텍스트 정보
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF1C1C0D),
                    letterSpacing = 0.6.sp
                )
                
                Text(
                    text = subtitle,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF9E9E47),
                    letterSpacing = 0.42.sp
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = description,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color(0xFF1C1C0D).copy(alpha = 0.7f),
                    lineHeight = 16.sp,
                    letterSpacing = 0.36.sp
                )
            }
        }
    }
} 
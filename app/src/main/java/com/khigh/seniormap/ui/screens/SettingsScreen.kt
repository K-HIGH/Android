package com.khigh.seniormap.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.khigh.seniormap.ui.composables.BottomNavigationBar

/**
 * 환경설정 화면
 */
@Composable
fun SettingsScreen(
    currentRoute: String,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        // 메인 콘텐츠 (스크롤 가능)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF5F5DC)) // 베이지색 배경
                .verticalScroll(rememberScrollState())
                .padding(bottom = 100.dp) // 하단 네비게이션 바 공간 확보
        ) {
            // 헤더
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "환경설정",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
            
            // 계정 섹션
            AccountSection()
            
            // 앱 설정 섹션
            AppSettingsSection()
            
            // 지원 & 도움말 섹션
            SupportHelpSection()
            
            // 정보 섹션
            InformationSection()
            
            // 하단 여백
            Spacer(modifier = Modifier.height(32.dp))
        }
        
        // 하단 고정 네비게이션 바
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        ) {
            BottomNavigationBar(
                currentRoute = currentRoute,
                onNavigate = onNavigate
            )
        }
    }
}

/**
 * 계정 섹션
 */
@Composable
private fun AccountSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        // 섹션 제목
        Text(
            text = "계정",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        // 프로필 정보
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 프로필 이미지
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE0E0E0)),
                contentAlignment = Alignment.Center
            ) {
                // 프로필 이미지 플레이스홀더 (여성 아이콘 대신)
                Text(
                    text = "👤",
                    fontSize = 24.sp
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // 이름과 역할
            Column {
                Text(
                    text = "김유정",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = "Caregiver",
                    fontSize = 14.sp,
                    color = Color(0xFF6B8E23) // 올리브 그린
                )
            }
        }
        
        Spacer(modifier = Modifier.height(20.dp))
        
        // 이메일
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "이메일",
                    fontSize = 14.sp,
                    color = Color.Black
                )
                Text(
                    text = "kimyoojung@email.com",
                    fontSize = 14.sp,
                    color = Color(0xFF6B8E23) // 올리브 그린
                )
            }
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "이메일 편집",
                tint = Color(0xFF6B8E23),
                modifier = Modifier.size(20.dp)
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // 전화번호
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "전화번호",
                    fontSize = 14.sp,
                    color = Color.Black
                )
                Text(
                    text = "+82 10-7777-7777",
                    fontSize = 14.sp,
                    color = Color(0xFF6B8E23) // 올리브 그린
                )
            }
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "전화번호 편집",
                tint = Color(0xFF6B8E23),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

/**
 * 앱 설정 섹션
 */
@Composable
private fun AppSettingsSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        // 섹션 제목
        Text(
            text = "앱 설정",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        // 설정 항목들
        SettingsItem("알림")
        SettingsItem("알림음")
        SettingsItem("화면 설정")
    }
}

/**
 * 지원 & 도움말 섹션
 */
@Composable
private fun SupportHelpSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        // 섹션 제목
        Text(
            text = "지원 & 도움말",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        // 도움말 항목들
        SettingsItem("FAQ")
        SettingsItem("고객 문의")
        SettingsItem("앱 사용 방법")
    }
}

/**
 * 정보 섹션
 */
@Composable
private fun InformationSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        // 섹션 제목
        Text(
            text = "정보",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        // 앱 버전
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "앱 버전",
                fontSize = 14.sp,
                color = Color.Black
            )
            Text(
                text = "1.2.3",
                fontSize = 14.sp,
                color = Color(0xFF6B8E23) // 올리브 그린
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // 구분선
        Divider(
            color = Color(0xFFE0E0E0),
            thickness = 1.dp
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // 이용 약관
        SettingsItem("이용 약관")
        
        // 개인정보처리방침
        SettingsItem("개인정보처리방침")
    }
}

/**
 * 설정 항목 컴포넌트
 */
@Composable
private fun SettingsItem(
    title: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontSize = 14.sp,
            color = Color.Black
        )
        Icon(
            imageVector = Icons.Default.ArrowForward,
            contentDescription = "이동",
            tint = Color(0xFF6B8E23),
            modifier = Modifier.size(16.dp)
        )
    }
}

/**
 * Preview 함수 - UI 확인용
 */
@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun SettingsScreenPreview() {
    SettingsScreen(
        currentRoute = "settings",
        onNavigate = { route ->
            // Preview에서는 실제 네비게이션을 수행하지 않음
        }
    )
}

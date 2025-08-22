package com.khigh.seniormap.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.clickable

/**
 * 하단 네비게이션 바 컴포넌트
 */
@Composable
fun BottomNavigationBar(
    currentRoute: String,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = Color(0xFFF5F5DC), // 베이지색 배경
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
            )
            .padding(top = 8.dp)
            .height(90.dp) // 높이를 조금 더 크게
    ) {
        // 상단 구분선
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Color(0xFFE0E0E0))
        )
        
        // 네비게이션 아이템들
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 홈 탭
            NavigationTab(
                icon = Icons.Default.Home,
                label = "홈",
                isSelected = currentRoute == "home",
                onClick = { onNavigate("home") }
            )
            
            // 지도 탭
            NavigationTab(
                icon = Icons.Default.Place,
                label = "지도",
                isSelected = currentRoute == "map",
                onClick = { onNavigate("map") }
            )
            
            // 설정 탭
            NavigationTab(
                icon = Icons.Default.Settings,
                label = "설정",
                isSelected = currentRoute == "settings",
                onClick = { onNavigate("settings") }
            )
        }
    }
}

/**
 * 개별 네비게이션 탭 컴포넌트
 */
@Composable
private fun NavigationTab(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clickable { onClick() }
            .padding(vertical = 12.dp, horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 아이콘
        Icon(
            imageVector = icon,
            contentDescription = label,
            modifier = Modifier.size(28.dp),
            tint = if (isSelected) {
                Color(0xFF8B4513) // 선택된 경우 진한 갈색
            } else {
                Color(0xFF9CAF88) // 선택되지 않은 경우 연한 올리브 그린
            }
        )
        
        Spacer(modifier = Modifier.height(6.dp))
        
        // 라벨
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) {
                Color(0xFF8B4513) // 선택된 경우 진한 갈색
            } else {
                Color(0xFF9CAF88) // 선택되지 않은 경우 연한 올리브 그린
            }
        )
    }
} 
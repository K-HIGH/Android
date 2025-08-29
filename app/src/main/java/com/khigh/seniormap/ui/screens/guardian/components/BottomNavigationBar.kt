package com.khigh.seniormap.ui.screens.guardian.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.List
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.khigh.seniormap.R

/**
 * 하단 네비게이션 바 컴포넌트
 * 
 * @param selectedTab 현재 선택된 탭
 * @param modifier 레이아웃 수정자
 * @param onTabSelected 탭 선택 이벤트 콜백
 */
@Composable
fun BottomNavigationBar(
    selectedTab: Int = 0,
    modifier: Modifier = Modifier,
    onTabSelected: (Int) -> Unit = {}
) {
    NavigationBar(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        // 홈 탭
        NavigationBarItem(
            icon = { 
                Icon(
                    imageVector = if (selectedTab == 0) Icons.Filled.Home else Icons.Outlined.Home,
                    contentDescription = "홈",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(24.dp)
                ) 
            },
            label = { 
                Text(
                    text = "홈",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal
                ) 
            },
            selected = selectedTab == 0,
            onClick = { onTabSelected(0) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.primary,
                selectedTextColor = MaterialTheme.colorScheme.primary,
                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
            )
        )
        
        // 지도 탭
        NavigationBarItem(
            icon = { 
                Icon(
                    imageVector = if (selectedTab == 1) ImageVector.vectorResource(R.drawable.ic_map_focused) else ImageVector.vectorResource(R.drawable.ic_map),
                    contentDescription = "지도",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(24.dp)
                ) 
            },
            label = { 
                Text(
                    text = "지도",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal
                ) 
            },
            selected = selectedTab == 1,
            onClick = { onTabSelected(1) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.primary,
                selectedTextColor = MaterialTheme.colorScheme.primary,
                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
            )
        )
        
        // 설정 탭
        NavigationBarItem(
            icon = { 
                Icon(
                    imageVector = if (selectedTab == 2) Icons.Filled.List else Icons.Outlined.List,
                    contentDescription = "설정",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(24.dp)
                ) 
            },
            label = { 
                Text(
                    text = "설정",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = if (selectedTab == 2) FontWeight.Bold else FontWeight.Normal
                ) 
            },
            selected = selectedTab == 2,
            onClick = { onTabSelected(2) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.primary,
                selectedTextColor = MaterialTheme.colorScheme.primary,
                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
            )
        )
    }
}
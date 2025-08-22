package com.khigh.seniormap.ui.screens.guardian.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * 보호인 목록 아이템 컴포넌트
 * 
 * @param name 보호인 이름
 * @param location 현재 위치
 * @param profileImageRes 프로필 이미지 리소스 ID (null이면 기본 아이콘 사용)
 * @param statusIcon 상태 아이콘
 * @param statusColor 상태 색상
 * @param modifier 레이아웃 수정자
 * @param onClick 클릭 이벤트 콜백
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuardianListItem(
    name: String,
    location: String,
    profileImageRes: Int? = null,
    statusIcon: ImageVector = Icons.Default.Home,
    statusColor: Color = Color(0xFF4CAF50),
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 프로필 이미지 또는 기본 아이콘
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape),
                contentAlignment = Alignment.Center
            ) {
                if (profileImageRes != null) {
                    Image(
                        painter = painterResource(id = profileImageRes),
                        contentDescription = "$name 프로필",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    // 기본 프로필 아이콘
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "기본 프로필",
                            modifier = Modifier
                                .size(24.dp)
                                .padding(12.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // 이름과 위치 정보
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = location,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            // 상태 아이콘
            Icon(
                imageVector = statusIcon,
                contentDescription = "상태",
                modifier = Modifier.size(24.dp),
                tint = statusColor
            )
        }
    }
}
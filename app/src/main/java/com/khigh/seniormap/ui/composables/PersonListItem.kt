package com.khigh.seniormap.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.khigh.seniormap.ui.model.Person
import com.khigh.seniormap.ui.model.PersonStatus

/**
 * 부모님 정보를 표시하는 리스트 아이템 컴포넌트
 */
@Composable
fun PersonListItem(
    person: Person,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
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
            // 프로필 이미지
            ProfileImage(
                imageUrl = person.profileImageUrl,
                modifier = Modifier.size(56.dp)
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // 이름과 상태 정보
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = person.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = person.status.displayName,
                        style = MaterialTheme.typography.bodyMedium,
                        color = person.status.color,
                        fontWeight = FontWeight.Medium
                    )
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    // 상태에 따른 아이콘
                    StatusIcon(status = person.status)
                }
            }
            
            // 우측 화살표 아이콘
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = "상세보기",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

/**
 * 프로필 이미지를 표시하는 컴포넌트
 */
@Composable
private fun ProfileImage(
    imageUrl: String?,
    modifier: Modifier = Modifier
) {
    if (imageUrl != null) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUrl)
                .crossfade(true)
                .build(),
            contentDescription = "프로필 이미지",
            modifier = modifier.clip(CircleShape),
            contentScale = ContentScale.Crop
        )
    } else {
        // 기본 프로필 이미지
        Box(
            modifier = modifier
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "기본 프로필",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

/**
 * 상태에 따른 아이콘을 표시하는 컴포넌트
 */
@Composable
private fun StatusIcon(status: PersonStatus) {
    val icon: ImageVector = when (status) {
        PersonStatus.HOME -> Icons.Default.Home
        PersonStatus.OUTING -> Icons.Default.LocationOn
        PersonStatus.WORK -> Icons.Default.Build
        PersonStatus.HOSPITAL -> Icons.Default.Info
        PersonStatus.PENDING -> Icons.Default.Person
    }
    
    Icon(
        imageVector = icon,
        contentDescription = status.displayName,
        tint = status.color,
        modifier = Modifier.size(16.dp)
    )
} 
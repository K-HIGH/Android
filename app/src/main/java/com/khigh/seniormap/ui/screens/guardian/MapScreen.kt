package com.khigh.seniormap.ui.screens.guardian

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.khigh.seniormap.ui.screens.guardian.components.GuardianData
import com.khigh.seniormap.ui.screens.LocationUtils

/**
 * 지도 화면 컴포넌트
 * 
 * 피보호인들의 현재 위치와 상태를 지도와 함께 표시합니다.
 * 현재는 지도 부분을 빈칸으로 두고 위치 정보만 표시합니다.
 * 
 * @param guardians 피보호인 목록 데이터
 * @param modifier 레이아웃 수정자
 */
@Composable
fun MapScreen(
    guardians: List<GuardianData>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 검색바
        SearchBar()
        
        // 지도 영역 (현재는 빈칸)
        MapPlaceholder()
        
        // 피보호인 위치 정보
        ProtectedPersonLocationList(guardians = guardians)
    }
}

/**
 * 검색바 컴포넌트
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchBar() {
    var searchText by remember { mutableStateOf("") }
    
    OutlinedTextField(
        value = searchText,
        onValueChange = { searchText = it },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        placeholder = {
            Text("주소를 검색하세요")
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "검색"
            )
        },
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = "현재 위치"
            )
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline
        ),
        shape = RoundedCornerShape(12.dp)
    )
}

/**
 * 지도 플레이스홀더 (현재는 빈칸)
 */
@Composable
private fun MapPlaceholder() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = "지도",
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Text(
                text = "지도가 여기에 표시됩니다",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Text(
                text = "지도 구현 예정",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
            )
        }
    }
}

/**
 * 피보호인 위치 정보 목록
 */
@Composable
private fun ProtectedPersonLocationList(guardians: List<GuardianData>) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        Text(
            text = "피보호인 위치",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(guardians) { guardian ->
                ProtectedPersonLocationCard(guardian = guardian)
            }
        }
    }
}

/**
 * 피보호인 위치 정보 카드
 */
@Composable
private fun ProtectedPersonLocationCard(guardian: GuardianData) {
    // 현재 위치가 있는 경우 거리 계산, 없으면 기본값
    val distance = if (guardian.currentLatitude != null && guardian.currentLongitude != null) {
        LocationUtils.calculateDistance(
            guardian.homeLatitude, guardian.homeLongitude,
            guardian.currentLatitude, guardian.currentLongitude
        )
    } else {
        0.0
    }
    
    // 집에 있는지 여부 판단
    val isAtHome = if (guardian.currentLatitude != null && guardian.currentLongitude != null) {
        LocationUtils.isAtHome(
            guardian.homeLatitude, guardian.homeLongitude,
            guardian.currentLatitude, guardian.currentLongitude
        )
    } else {
        guardian.isAtHome // 기존 데이터 사용
    }
    
    Card(
        modifier = Modifier.width(160.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 프로필 이미지 (현재는 기본 아이콘)
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "프로필",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // 이름
            Text(
                text = guardian.name,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            // 상태 (집/외출)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "위치",
                    modifier = Modifier.size(16.dp),
                    tint = if (isAtHome) Color(0xFF4CAF50) else Color(0xFFFF9800)
                )
                
                Text(
                    text = if (isAtHome) "집" else "외출",
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isAtHome) Color(0xFF4CAF50) else Color(0xFFFF9800),
                    fontWeight = FontWeight.Medium
                )
            }
            
            Spacer(modifier = Modifier.height(4.dp))
            
            // 거리 정보
            if (guardian.currentLatitude != null && guardian.currentLongitude != null) {
                Text(
                    text = "집으로부터 ${LocationUtils.formatDistance(distance)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                Text(
                    text = "위치 정보 없음",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

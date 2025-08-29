package com.khigh.seniormap.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.khigh.seniormap.ui.model.Person
import com.khigh.seniormap.ui.model.PersonStatus
import androidx.compose.ui.tooling.preview.Preview

/**
 * 부모님 프로필 화면
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonProfileScreen(
    person: Person,
    onNavigateBack: () -> Unit,
    onEditProfile: () -> Unit,
    onDeleteProfile: () -> Unit,
    onCall: () -> Unit,
    onMessage: () -> Unit,
    onViewLocationHistory: () -> Unit,
    onStatusChange: (PersonStatus) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var currentStatus by remember { mutableStateOf(person.status) }
    var showStatusDialog by remember { mutableStateOf(false) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("프로필") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "뒤로가기"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onEditProfile) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "프로필 편집"
                        )
                    }
                    IconButton(onClick = onDeleteProfile) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "프로필 삭제"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // 프로필 정보 섹션
            ProfileInfoSection(
                person = person,
                currentStatus = currentStatus
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // 지도 섹션
            MapSection()
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // 상태 및 액션 섹션
            StatusAndActionSection(
                currentStatus = currentStatus,
                onStatusClick = { showStatusDialog = true },
                onCall = onCall,
                onMessage = onMessage
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // 위치 히스토리 섹션
            LocationHistorySection(
                onViewHistory = onViewLocationHistory
            )
            
            Spacer(modifier = Modifier.height(24.dp))
        }
        
        // 상태 변경 다이얼로그
        if (showStatusDialog) {
            StatusChangeDialog(
                currentStatus = currentStatus,
                onStatusSelected = { newStatus ->
                    currentStatus = newStatus
                    onStatusChange(newStatus)
                    showStatusDialog = false
                },
                onDismiss = { showStatusDialog = false }
            )
        }
    }
}

/**
 * 프로필 정보 섹션
 */
@Composable
private fun ProfileInfoSection(
    person: Person,
    currentStatus: PersonStatus,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 프로필 이미지
        if (person.profileImageUrl != null) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(person.profileImageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = "프로필 이미지",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        } else {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "기본 프로필",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(48.dp)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // 이름
        Text(
            text = person.name,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // 나이
        person.age?.let { age ->
            Text(
                text = "${age}세",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        
        // 현재 상태 표시
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(currentStatus.color.copy(alpha = 0.1f))
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Icon(
                imageVector = getStatusIcon(currentStatus),
                contentDescription = currentStatus.displayName,
                tint = currentStatus.color,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = currentStatus.displayName,
                style = MaterialTheme.typography.bodyMedium,
                color = currentStatus.color,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

/**
 * 지도 섹션
 */
@Composable
private fun MapSection(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "현재 위치",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // 지도 플레이스홀더 (실제 지도 구현 시 교체)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "지도",
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "지도가 여기에 표시됩니다",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

/**
 * 상태 및 액션 섹션
 */
@Composable
private fun StatusAndActionSection(
    currentStatus: PersonStatus,
    onStatusClick: () -> Unit,
    onCall: () -> Unit,
    onMessage: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        // 상태 정보 (클릭 가능)
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "상태",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )
            
            Text(
                text = "편집",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable { onStatusClick() }
            )
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // 현재 상태 표시 (클릭 가능)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .clickable { onStatusClick() }
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = getStatusIcon(currentStatus),
                    contentDescription = currentStatus.displayName,
                    tint = currentStatus.color,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = currentStatus.displayName,
                    style = MaterialTheme.typography.titleMedium,
                    color = currentStatus.color,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "상태 편집",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // 액션 버튼들
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // 전화 버튼
            Button(
                onClick = onCall,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFFD54F) // 노란색
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Phone,
                    contentDescription = "전화",
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("전화")
            }
            
            // 문자 버튼
            Button(
                onClick = onMessage,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFFD54F) // 노란색
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = "문자",
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("문자")
            }
        }
    }
}

/**
 * 위치 히스토리 섹션
 */
@Composable
private fun LocationHistorySection(
    onViewHistory: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "위치 히스토리",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )
            
            IconButton(onClick = onViewHistory) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "위치 히스토리 보기",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

/**
 * 상태 변경 다이얼로그
 */
@Composable
private fun StatusChangeDialog(
    currentStatus: PersonStatus,
    onStatusSelected: (PersonStatus) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("상태 변경")
        },
        text = {
            Column {
                Text(
                    text = "현재 상태: ${currentStatus.displayName}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "새로운 상태를 선택하세요:",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        },
        confirmButton = {
            Column {
                PersonStatus.values().forEach { status ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onStatusSelected(status) }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = getStatusIcon(status),
                            contentDescription = status.displayName,
                            tint = status.color,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = status.displayName,
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (status == currentStatus) 
                                MaterialTheme.colorScheme.primary 
                            else 
                                MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("취소")
            }
        }
    )
}

/**
 * 상태에 따른 아이콘을 반환하는 함수
 */
private fun getStatusIcon(status: PersonStatus) = when (status) {
    PersonStatus.HOME -> Icons.Default.Home
    PersonStatus.OUTING -> Icons.Default.LocationOn
    PersonStatus.WORK -> Icons.Default.Build
    PersonStatus.HOSPITAL -> Icons.Default.Info
    PersonStatus.PENDING -> Icons.Default.Person
}

/**
 * Preview 함수 - UI 확인용
 */
@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PersonProfileScreenPreview() {
    val samplePerson = Person(
        id = "1",
        name = "김혜자",
        status = PersonStatus.HOME,
        age = 78,
        relationship = "어머니"
    )
    
    PersonProfileScreen(
        person = samplePerson,
        onNavigateBack = {},
        onEditProfile = {},
        onDeleteProfile = {},
        onCall = {},
        onMessage = {},
        onViewLocationHistory = {},
        onStatusChange = {}
    )
} 
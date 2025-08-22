package com.khigh.seniormap.ui.screens.guardian

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.khigh.seniormap.ui.screens.guardian.components.GuardianData

/**
 * 피보호인 정보 수정 화면
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditGuardianScreen(
    guardianData: GuardianData,
    onNavigateBack: () -> Unit = {},
    onSave: (GuardianData) -> Unit = {},
    modifier: Modifier = Modifier
) {
    // 수정할 데이터 상태
    var name by remember { mutableStateOf(guardianData.name) }
    var location by remember { mutableStateOf(guardianData.location) }
    var phoneNumber by remember { mutableStateOf("010-1234-5678") } // 임시 데이터
    var emergencyContact by remember { mutableStateOf("010-9876-5432") } // 임시 데이터
    var address by remember { mutableStateOf("서울시 강남구 테헤란로 123") } // 임시 데이터
    var notes by remember { mutableStateOf("특별한 주의사항이 있습니다.") } // 임시 데이터
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("피보호인 수정") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "뒤로 가기"
                        )
                    }
                },
                actions = {
                    TextButton(
                        onClick = {
                            // 수정된 데이터로 저장
                            val updatedGuardian = guardianData.copy(
                                name = name,
                                location = location
                            )
                            onSave(updatedGuardian)
                            onNavigateBack()
                        }
                    ) {
                        Text(
                            text = "저장",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // 프로필 이미지 섹션
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .clickable { /* TODO: 이미지 선택 */ },
                    contentAlignment = Alignment.Center
                ) {
                    if (guardianData.profileImageRes != null) {
                        Image(
                            painter = painterResource(id = guardianData.profileImageRes),
                            contentDescription = "프로필 이미지",
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
                                imageVector = Icons.Default.Edit,
                                contentDescription = "프로필 이미지 선택",
                                modifier = Modifier
                                    .size(40.dp)
                                    .padding(20.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "프로필 이미지 선택",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            // 기본 정보 섹션
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "기본 정보",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    EditField(
                        label = "이름",
                        value = name,
                        onValueChange = { name = it }
                    )
                    
                    EditField(
                        label = "현재 위치",
                        value = location,
                        onValueChange = { location = it }
                    )
                    
                    EditField(
                        label = "연락처",
                        value = phoneNumber,
                        keyboardType = KeyboardType.Phone,
                        onValueChange = { phoneNumber = it }
                    )
                }
            }
            
            // 비상 연락처 섹션
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "비상 연락처",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    EditField(
                        label = "비상 연락처",
                        value = emergencyContact,
                        keyboardType = KeyboardType.Phone,
                        onValueChange = { emergencyContact = it }
                    )
                    
                    EditField(
                        label = "주소",
                        value = address,
                        onValueChange = { address = it }
                    )
                }
            }
            
            // 기타 정보 섹션
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "기타 정보",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    EditField(
                        label = "특이사항",
                        value = notes,
                        onValueChange = { notes = it }
                    )
                }
            }
            
            // 하단 여백
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

/**
 * 편집 가능한 입력 필드 컴포넌트
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditField(
    label: String,
    value: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
            keyboardType = keyboardType
        ),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline
        )
    )
}

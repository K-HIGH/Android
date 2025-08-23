package com.khigh.seniormap.ui.screens.guardian

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * 피보호인 추가 화면
 * 
 * 새로운 피보호인을 등록할 수 있는 화면입니다.
 * 프로필 사진, 기본 정보, 비상 연락처, 추가 정보를 입력받습니다.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProtectedPersonScreen(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit = {},
    onSave: (ProtectedPersonData) -> Unit = {}
) {
    // 입력 데이터 상태
    var name by remember { mutableStateOf(TextFieldValue("")) }
    var phoneNumber by remember { mutableStateOf(TextFieldValue("")) }
    var homePhone by remember { mutableStateOf(TextFieldValue("")) }
    var address by remember { mutableStateOf(TextFieldValue("")) }
    var emergencyContact by remember { mutableStateOf(TextFieldValue("")) }
    var relationship by remember { mutableStateOf(TextFieldValue("")) }
    var notes by remember { mutableStateOf(TextFieldValue("")) }
    
    // 프로필 사진 상태
    var hasProfileImage by remember { mutableStateOf(false) }
    
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        text = "피보호인 추가",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "뒤로 가기"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            
            // 프로필 사진 섹션
            ProfileImageSection(
                hasImage = hasProfileImage,
                onAddPhoto = {
                    // TODO: 카메라/갤러리에서 사진 선택 구현
                    hasProfileImage = true
                }
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // 기본 정보 섹션
            InputField(
                label = "성함",
                value = name,
                onValueChange = { name = it },
                placeholder = "입력"
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            InputField(
                label = "전화번호",
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                placeholder = "입력"
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            InputField(
                label = "집전화",
                value = homePhone,
                onValueChange = { homePhone = it },
                placeholder = "입력"
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            InputField(
                label = "집 주소",
                value = address,
                onValueChange = { address = it },
                placeholder = "검색",
                isSearchable = true
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // 비상 연락처 섹션
            SectionHeader(title = "비상 연락처")
            
            Spacer(modifier = Modifier.height(16.dp))
            
            InputField(
                label = "비상 연락처 번호",
                value = emergencyContact,
                onValueChange = { emergencyContact = it },
                placeholder = "입력"
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            InputField(
                label = "관계",
                value = relationship,
                onValueChange = { relationship = it },
                placeholder = "입력"
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // 추가 정보 섹션
            SectionHeader(title = "추가 정보")
            
            Spacer(modifier = Modifier.height(16.dp))
            
            NotesInputField(
                label = "비고",
                value = notes,
                onValueChange = { notes = it },
                placeholder = "입력"
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // 승인 요청 버튼
            Button(
                onClick = {
                    val protectedPersonData = ProtectedPersonData(
                        name = name.text,
                        phoneNumber = phoneNumber.text,
                        homePhone = homePhone.text,
                        address = address.text,
                        emergencyContact = emergencyContact.text,
                        relationship = relationship.text,
                        notes = notes.text
                    )
                    onSave(protectedPersonData)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFFD700) // 밝은 노란색
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "승인 요청",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

/**
 * 프로필 사진 섹션
 */
@Composable
private fun ProfileImageSection(
    hasImage: Boolean,
    onAddPhoto: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 프로필 이미지
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(
                    if (hasImage) Color(0xFFE0E0E0) else Color(0xFFF5F5F5)
                )
                .border(
                    width = 2.dp,
                    color = if (hasImage) Color(0xFFE0E0E0) else Color(0xFFCCCCCC),
                    shape = CircleShape
                )
                .clickable { onAddPhoto() },
            contentAlignment = Alignment.Center
        ) {
            if (hasImage) {
                // TODO: 실제 이미지 표시
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "프로필 사진",
                    modifier = Modifier.size(48.dp),
                    tint = Color.Gray
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "사진 추가",
                    modifier = Modifier.size(48.dp),
                    tint = Color.Gray
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "+ 사진 추가",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "빠른 식별을 위한 사진을 추가하세요",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
    }
}

/**
 * 입력 필드 컴포넌트
 */
@Composable
private fun InputField(
    label: String,
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    placeholder: String,
    isSearchable: Boolean = false
) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium,
            color = Color.Black
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(
                    color = Color(0xFFF8F8F8),
                    shape = RoundedCornerShape(8.dp)
                )
                .border(
                    width = 1.dp,
                    color = Color(0xFFE0E0E0),
                    shape = RoundedCornerShape(8.dp)
                )
                .clickable { if (isSearchable) { /* TODO: 주소 검색 화면 열기 */ } }
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    color = if (value.text.isEmpty()) Color.Gray else Color.Black
                ),
                decorationBox = { innerTextField ->
                    if (value.text.isEmpty()) {
                        Text(
                            text = placeholder,
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.Gray
                        )
                    }
                    innerTextField()
                }
            )
        }
    }
}

/**
 * 비고 입력 필드 (여러 줄)
 */
@Composable
private fun NotesInputField(
    label: String,
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    placeholder: String
) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium,
            color = Color.Black
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .background(
                    color = Color(0xFFF8F8F8),
                    shape = RoundedCornerShape(8.dp)
                )
                .border(
                    width = 1.dp,
                    color = Color(0xFFE0E0E0),
                    shape = RoundedCornerShape(8.dp)
                )
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    color = if (value.text.isEmpty()) Color.Gray else Color.Black
                ),
                decorationBox = { innerTextField ->
                    if (value.text.isEmpty()) {
                        Text(
                            text = placeholder,
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.Gray
                        )
                    }
                    innerTextField()
                }
            )
        }
    }
}

/**
 * 섹션 헤더
 */
@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        color = Color.Black
    )
}

/**
 * 피보호인 데이터 모델
 */
data class ProtectedPersonData(
    val name: String = "",
    val phoneNumber: String = "",
    val homePhone: String = "",
    val address: String = "",
    val emergencyContact: String = "",
    val relationship: String = "",
    val notes: String = ""
)

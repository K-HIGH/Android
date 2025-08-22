package com.khigh.seniormap.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.khigh.seniormap.viewmodel.AuthViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import com.khigh.seniormap.model.entity.UserEntity

/**
 * 로딩 화면 컴포넌트
 * 
 * 앱 내에서 데이터 로딩이나 비동기 작업 진행 중에 표시되는 화면입니다.
 * 사용자에게 작업이 진행 중임을 알려주는 역할을 합니다.
 * 
 * @param modifier 레이아웃 수정자
 * @param message 로딩 중 표시할 메시지 (기본값: "로딩 중...")
 */
@Composable
fun LoadingScreen(
    onNavigateToHome: () -> Unit = {},
    onNavigateToRoleSelection: () -> Unit = {},
    modifier: Modifier = Modifier,
    message: String = "로딩 중...",
    authViewModel: AuthViewModel = hiltViewModel()
) {

    val _tag = "com.khigh.seniormap.LoadingScreen"
    Log.d(_tag, "message: $message")

    val uiState by authViewModel.uiState.collectAsStateWithLifecycle()
    val isSigningIn by authViewModel.isSigningIn.collectAsStateWithLifecycle()
    val isUserRegistered by authViewModel.isUserRegistered.collectAsStateWithLifecycle()

    LaunchedEffect(isSigningIn, isUserRegistered) {
        Log.d(_tag, "[LaunchedEffect] isSigningIn: $isSigningIn, isUserRegistered: $isUserRegistered")
        
        // 로그인 진행 중이 아닐 때만 네비게이션 처리
        if (!isSigningIn) {
            when (isUserRegistered) {
                // 역할 선택이 필요한 경우 (신규 사용자)
                true -> {
                    Log.d(_tag, "[LaunchedEffect] No need role selection, navigating to home")
                    onNavigateToHome()
                }
                // 인증 완료된 기존 사용자
                else -> {
                    Log.d(_tag, "[LaunchedEffect] role selection, navigating to role selection")
                    onNavigateToRoleSelection()
                }
            }
        }
    }
    
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5E5)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // 로딩 인디케이터
            CircularProgressIndicator(
                modifier = Modifier.size(48.dp),
                color = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // 로딩 메시지
            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
} 
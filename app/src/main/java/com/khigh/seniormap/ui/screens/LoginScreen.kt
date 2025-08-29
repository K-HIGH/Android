package com.khigh.seniormap.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
// import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.khigh.seniormap.viewmodel.AuthViewModel
import io.github.jan.supabase.auth.providers.Google
import io.github.jan.supabase.auth.providers.Kakao
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.flow.distinctUntilChanged
import androidx.lifecycle.compose.collectAsStateWithLifecycle

/**
 * 로그인 화면 컴포넌트
 * 
 * SeniorMap 앱의 로그인 화면을 담당하며,
 * OAuth(카카오, 구글) 인증을 통한 로그인 기능을 제공합니다.
 * 
 * @param onNavigateToHome 홈 화면으로 이동하는 콜백
 * @param modifier 레이아웃 수정자
 * @param authViewModel 인증 관련 ViewModel (Hilt를 통해 주입)
 */
@Composable
fun LoginScreen(
    onNavigateToLoading: () -> Unit = {},
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel
) {
    val _tag = "com.khigh.seniormap.LoginScreen"
    Log.d(_tag, "onNavigateToLoading: $onNavigateToLoading")
    
    val context = LocalContext.current
    val uiState by authViewModel.uiState.collectAsStateWithLifecycle()
    val isSigningIn by authViewModel.isSigningIn.collectAsStateWithLifecycle()
    val isLoading by authViewModel.isLoading.collectAsStateWithLifecycle()
    val isUserRegistered by authViewModel.isUserRegistered.collectAsStateWithLifecycle()
    val errorMessage by authViewModel.errorMessage.collectAsStateWithLifecycle()
    val authState by authViewModel.authState.collectAsStateWithLifecycle()
    
    // 인증 성공 시 네비게이션 처리
    LaunchedEffect(authState, isSigningIn, isLoading, Unit) {
        Log.d(_tag, "[LaunchedEffect] isSigningIn: $isSigningIn, isLoading: $isLoading, authState: $authState")
        
        // 로그인 진행 중이 아닐 때만 네비게이션 처리
        if (isSigningIn && !isLoading) {
            when {
                // 인증 완료된 기존 사용자
                authState is SessionStatus.Authenticated -> {
                    Log.d(_tag, "[LaunchedEffect] Login complete, navigating to loading")
                    authViewModel.onAuthResultHandled()
                    onNavigateToLoading()
                }
                else -> {
                    Log.d(_tag, "[LaunchedEffect] Authentication state: $authState")
                    authViewModel.onAuthResultHandled()
                }
            }
        }
    }

    // 에러 메시지 처리
    errorMessage?.let { message ->
        LaunchedEffect(message) {
            Log.e(_tag, "[LaunchedEffect] Error: $message")
            // TODO: 스낵바나 다이얼로그로 에러 표시
        }
    }
    
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(32.dp)
        ) {
            // 앱 제목
            Text(
                text = "SeniorMap",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // 앱 설명
            Text(
                text = "시니어를 위한 안전한 지도 서비스",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(48.dp))
            
            // 로딩 상태 표시
            if (isSigningIn) {
                CircularProgressIndicator(
                    modifier = Modifier.size(32.dp),
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "로그인 중...",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            } else {
                // OAuth 로그인 버튼들
                LoginButtonsSection(
                    onKakaoLogin = {
                        Log.d(_tag, "[LoginScreen] Kakao login clicked")
                        authViewModel.loginWithOAuth(Kakao)
                    },
                    onGoogleLogin = {
                        Log.d(_tag, "[LoginScreen] Google login clicked")
                        authViewModel.loginWithOAuth(Google)
                    }
                )
            }
            
            // 에러 메시지 표시
            errorMessage?.let { message ->
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Text(
                        text = message,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.padding(16.dp),
                        textAlign = TextAlign.Center
                    )
                }
                
                // 에러 메시지 자동 클리어 (5초 후)
                LaunchedEffect(message) {
                    kotlinx.coroutines.delay(5000)
                    authViewModel.clearErrorMessage()
                }
            }
        }
    }
}

/**
 * OAuth 로그인 버튼들을 담은 섹션
 */
@Composable
private fun LoginButtonsSection(
    onKakaoLogin: () -> Unit,
    onGoogleLogin: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 카카오 로그인 버튼
        Button(
            onClick = onKakaoLogin,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.tertiary, // Figma 노란색
                contentColor = MaterialTheme.colorScheme.onTertiary
            ),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Done,
                contentDescription = "카카오 로그인",
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "카카오로 로그인",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Medium
            )
        }
        
        // 구글 로그인 버튼
        OutlinedButton(
            onClick = onGoogleLogin,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.onSurface
            ),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Done,
                contentDescription = "구글 로그인",
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Google로 로그인",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Medium
            )
        }
    }
} 
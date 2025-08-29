package com.khigh.seniormap.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
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
import io.github.jan.supabase.auth.status.SessionStatus

/**
 * 홈 화면 컴포넌트
 * 
 * SeniorMap 앱의 메인 홈 화면을 담당하며,
 * 사용자가 로그인한 후 표시되는 메인 대시보드입니다.
 * AuthViewModel을 통해 로그아웃 기능을 제공합니다.
 * 
 * @param onNavigateToLogin 로그인 화면으로 이동하는 콜백 (로그아웃)
 * @param modifier 레이아웃 수정자
 * @param authViewModel 인증 관련 ViewModel (Hilt를 통해 주입)
 */
@Composable
fun HomeScreen(
    onNavigateToLogin: () -> Unit = {},
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel
) {
    Log.d("com.khigh.seniormap", "[HomeScreen] onNavigateToLogin: $onNavigateToLogin")
    
    val context = LocalContext.current
    val uiState by authViewModel.uiState.collectAsState()
    val isSigningIn by authViewModel.isSigningIn.collectAsState()
    val isSigningOut by authViewModel.isSigningOut.collectAsState()
    val errorMessage by authViewModel.errorMessage.collectAsState()
    val authState by authViewModel.authState.collectAsState()
    val currentUser by authViewModel.currentUser.collectAsState()
    
    // 인증 상태가 false가 되면 로그인 화면으로 이동
    LaunchedEffect(authState) {
        if (authState is SessionStatus.NotAuthenticated) {
            Log.d("com.khigh.seniormap", "[HomeScreen] User logged out, navigating to login")
            onNavigateToLogin()
        }
    }
    
    // 에러 메시지 처리
    errorMessage?.let { message ->
        LaunchedEffect(message) {
            Log.e("com.khigh.seniormap", "[HomeScreen] Error: $message")
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
            modifier = Modifier.padding(32.dp)
        ) {
            // 홈 아이콘
            Icon(
                imageVector = Icons.Default.Home,
                contentDescription = "홈",
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // 화면 제목
            Text(
                text = "홈 화면",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // 환영 메시지
            Text(
                text = "SeniorMap에 오신 것을 환영합니다!",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
            
            // 사용자 정보 표시 (있는 경우)
            currentUser?.let { user ->
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "로그인된 사용자",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "ID: ${user.id}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // 로그아웃 버튼
            if (isSigningOut) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.primary
                )
            } else {
                OutlinedButton(
                    onClick = {
                        Log.d("com.khigh.seniormap", "[HomeScreen] Logout button clicked")
                        authViewModel.logout()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "로그아웃",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "로그아웃",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Medium
                    )
                }
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
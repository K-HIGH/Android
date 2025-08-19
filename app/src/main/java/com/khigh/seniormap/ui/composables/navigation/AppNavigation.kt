package com.khigh.seniormap.ui.composables.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

/**
 * 앱의 메인 네비게이션 컴포넌트
 * 
 * @param modifier 레이아웃 수정자
 * @param isAuthenticated 사용자 인증 상태
 * @param isLoading 로딩 상태
 * @param errorMessage 에러 메시지
 * @param onClearError 에러 메시지 클리어 콜백
 */
@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    isAuthenticated: Boolean = false,
    isLoading: Boolean = false,
    errorMessage: String? = null,
    onClearError: () -> Unit = {}
) {
    val navController = rememberNavController()
    
    // 에러 메시지 표시
    errorMessage?.let { message ->
        LaunchedEffect(message) {
            // TODO: 스낵바나 다이얼로그로 에러 표시
        }
    }
    
    Box(modifier = modifier.fillMaxSize()) {
        // 로딩 상태 표시
        if (isLoading) {
            LoadingScreen()
        } else {
            // 네비게이션 호스트
            NavHost(
                navController = navController,
                startDestination = if (isAuthenticated) "home" else "login"
            ) {
                // 로그인 화면
                composable("login") {
                    LoginScreen(
                        onNavigateToHome = {
                            navController.navigate("home") {
                                popUpTo("login") { inclusive = true }
                            }
                        }
                    )
                }
                
                // 홈 화면
                composable("home") {
                    HomeScreen(
                        onNavigateToLogin = {
                            navController.navigate("login") {
                                popUpTo("home") { inclusive = true }
                            }
                        }
                    )
                }
            }
        }
    }
}

/**
 * 로딩 화면 컴포넌트
 */
@Composable
private fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(48.dp),
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "로딩 중...",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

/**
 * 임시 로그인 화면 컴포넌트
 */
@Composable
private fun LoginScreen(
    onNavigateToHome: () -> Unit = {}
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(32.dp)
        ) {
            Text(
                text = "SeniorMap",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "시니어를 위한 안전한 지도 서비스",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = onNavigateToHome,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "로그인",
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}

/**
 * 임시 홈 화면 컴포넌트
 */
@Composable
private fun HomeScreen(
    onNavigateToLogin: () -> Unit = {}
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(32.dp)
        ) {
            Text(
                text = "홈 화면",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "SeniorMap에 오신 것을 환영합니다!",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(32.dp))
            OutlinedButton(
                onClick = onNavigateToLogin,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "로그아웃",
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
} 
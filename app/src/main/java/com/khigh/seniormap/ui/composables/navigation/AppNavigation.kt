package com.khigh.seniormap.ui.composables.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import android.util.Log
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.khigh.seniormap.ui.screens.HomeScreen
import com.khigh.seniormap.ui.screens.LoadingScreen
import com.khigh.seniormap.ui.screens.LoginScreen
import com.khigh.seniormap.ui.screens.guardian.GuardianHomeScreen
import com.khigh.seniormap.viewmodel.AuthViewModel
import androidx.hilt.navigation.compose.hiltViewModel

/**
 * 앱의 메인 네비게이션 컴포넌트
 * 
 * 앱 전체의 화면 전환과 네비게이션을 관리합니다.
 * 사용자의 인증 상태에 따라 적절한 화면을 표시하며,
 * 로딩 상태와 에러 처리도 담당합니다.
 * 
 * @param modifier 레이아웃 수정자
 * @param isAuthenticated 사용자 인증 상태
 * @param isSigningIn 로그인 상태
 * @param errorMessage 에러 메시지
 * @param onClearError 에러 메시지 클리어 콜백
 */
@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    isAuthenticated: Boolean = false,
    isSigningIn: Boolean = false,
    errorMessage: String? = null,
    onClearError: () -> Unit = {}
) {
    val navController = rememberNavController()

    val authViewModel: AuthViewModel = hiltViewModel()

    val authState by authViewModel.authState.collectAsState()
    
    // 에러 메시지 표시
    errorMessage?.let { message ->
        LaunchedEffect(message) {
            // TODO: 스낵바나 다이얼로그로 에러 표시
            Log.e("AppNavigation", "Error: $message")
        }
    }
    
    Box(modifier = modifier.fillMaxSize()) {
        // 네비게이션 호스트
        Log.d("com.khigh.seniormap", "[AppNavigation] isAuthenticated: $isAuthenticated, authState: $authState")
        NavHost(
            navController = navController,
            startDestination = if (isAuthenticated) "guardian_home" else "login"
        ) {
            // 로그인 화면
            composable("login") {
                LoginScreen(
                    onNavigateToHome = {
                        navController.navigate("guardian_home") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                )
            }
            
            // 보호인 홈 화면
            composable("guardian_home") {
                GuardianHomeScreen(
                    onNavigateToLogin = {
                        navController.navigate("login") {
                            popUpTo("guardian_home") { inclusive = true }
                        }
                    }
                )
            }
            
            // 기존 홈 화면 (필요시 유지)
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
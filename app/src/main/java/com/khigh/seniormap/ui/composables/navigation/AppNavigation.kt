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
import com.khigh.seniormap.ui.screens.RoleSelectionScreen
import com.khigh.seniormap.viewmodel.AuthViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import com.khigh.seniormap.viewmodel.UserViewModel


/**
 * 앱의 메인 네비게이션 컴포넌트
 * 
 * 앱 전체의 화면 전환과 네비게이션을 관리합니다.
 * 사용자의 인증 상태에 따라 적절한 화면을 표시하며,
 * 로딩 상태와 에러 처리도 담당합니다.
 * 
 * @param authViewModel 인증 관련 ViewModel (외부에서 전달)
 * @param userViewModel 사용자 관련 ViewModel (외부에서 전달)
 * @param modifier 레이아웃 수정자
 * @param isAuthenticated 사용자 인증 상태
 * @param isSigningIn 로그인 상태
 * @param errorMessage 에러 메시지
 * @param onClearError 에러 메시지 클리어 콜백
 */
@Composable
fun AppNavigation(
    authViewModel: AuthViewModel,
    userViewModel: UserViewModel,
    modifier: Modifier = Modifier,
    isAuthenticated: Boolean = false,
    isSigningIn: Boolean = false,
    errorMessage: String? = null,
    onClearError: () -> Unit = {}
) {
    val navController = rememberNavController()

    val authState by authViewModel.authState.collectAsState()
    val isUserRegistered by authViewModel.isUserRegistered.collectAsState()
    val isCaregiver by userViewModel.isCaregiver.collectAsState()
    val isHelper by userViewModel.isHelper.collectAsState()
    
    // 에러 메시지 표시
    errorMessage?.let { message ->
        LaunchedEffect(message) {
            // TODO: 스낵바나 다이얼로그로 에러 표시
            Log.e("AppNavigation", "Error: $message")
        }
    }
    
    // 네비게이션 상태 결정
    val startDestination = when {
        isUserRegistered -> "role_selection"
        !isAuthenticated -> "login"
        isAuthenticated && !isUserRegistered -> "role_selection"
        isAuthenticated && isUserRegistered -> "guardian_home"
        else -> "login"
    }
    
    Box(modifier = modifier.fillMaxSize()) {
        // 네비게이션 호스트
        Log.d("com.khigh.seniormap", "[AppNavigation] isAuthenticated: $isAuthenticated, authState: $authState, isUserRegistered: $isUserRegistered")
        NavHost(
            navController = navController,
            startDestination = startDestination
        ) {
            // 로그인 화면
            composable("login") {
                LoginScreen(
                    onNavigateToLoading = {
                        navController.navigate("loading") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                )
            }
            composable("loading") {
                LoadingScreen(
                    onNavigateToGuardianHome = {
                        navController.navigate("guardian_home") {
                            popUpTo("loading") { inclusive = true }
                        }
                    },
                    onNavigateToRoleSelection = {
                        navController.navigate("role_selection") {
                            popUpTo("loading") { inclusive = true }
                        }
                    }
                )
            }
            
            // 역할 선택 화면
            composable("role_selection") {
                RoleSelectionScreen(
                    onRoleSelected = { isCaregiver, isHelper ->
                        // 홈 화면으로 이동
                        Log.d("com.khigh.seniormap", "[RoleSelectionScreen] isCaregiver: $isCaregiver, isHelper: $isHelper")
                        if (isCaregiver || isHelper) {
                            navController.navigate("guardian_home") {
                                popUpTo("role_selection") { inclusive = true }
                            }
                        } else {
                            navController.navigate("home") {
                                popUpTo("role_selection") { inclusive = true }
                            }
                        }
                    },
                    userViewModel = userViewModel
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
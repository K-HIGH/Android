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
import com.khigh.seniormap.viewmodel.LocationViewModel
import com.khigh.seniormap.viewmodel.RouteViewModel
import com.khigh.seniormap.viewmodel.UserViewModel
import com.khigh.seniormap.viewmodel.CaregiverViewModel
import com.khigh.seniormap.ui.screens.senior.SeniorHomeScreen
import com.khigh.seniormap.ui.screens.senior.LocationSearchScreen
import com.khigh.seniormap.ui.screens.senior.FavoriteEditScreen
import com.khigh.seniormap.ui.screens.senior.RouteSearchScreen


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
    caregiverViewModel: CaregiverViewModel = hiltViewModel(),
    locationViewModel: LocationViewModel = hiltViewModel(),
    routeViewModel: RouteViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
    isAuthenticated: Boolean = false,
    isSigningIn: Boolean = false,
    isLoading: Boolean = false,
    isUserRegistered: Boolean = false,
    errorMessage: String? = null,
    onClearError: () -> Unit = {}
) {
    val navController = rememberNavController()

    val authState by authViewModel.authState.collectAsState()
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
        !isAuthenticated -> "login"
        isAuthenticated && !isLoading && !isUserRegistered -> "role_selection"
        isAuthenticated && !isLoading && isUserRegistered -> "guardian_home" // TODO: 보호자 홈 화면으로 변경
        else -> "loading"
    }
    
    Box(modifier = modifier.fillMaxSize()) {
        // 네비게이션 호스트
        Log.d("com.khigh.seniormap", "[AppNavigation] isAuthenticated: $isAuthenticated, authState: $authState, isUserRegistered: $isUserRegistered")
        NavHost(
            navController = navController,
            startDestination = "login"
            // startDestination = startDestination
        ) {
            // 로그인 화면
            composable("login") {
                LoginScreen(
                    onNavigateToLoading = {
                        navController.navigate("loading") {
                            // popUpTo("login") { inclusive = true }
                        }
                    },
                    authViewModel = authViewModel
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
                    },
                    authViewModel = authViewModel,
                    userViewModel = userViewModel
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
                            navController.navigate("senior_home") {
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
                            // popUpTo("guardian_home") { inclusive = true }
                        }
                    },
                    caregiverViewModel = caregiverViewModel
                )
            }
            
            composable("senior_home") {
                SeniorHomeScreen(
                    onNavigateToLogin = {
                        navController.navigate("login") {
                            // popUpTo("senior_home") { inclusive = true }
                        }
                    },
                    onNavigateToLocationSearch = {
                        navController.navigate("location_search") {
                            // popUpTo("senior_home") { inclusive = true }
                        }
                    },
                    onNavigateToFavoriteEdit = {
                        navController.navigate("favorite_edit") {
                            // popUpTo("senior_home") { inclusive = true }
                        }
                    }
                )
            }

            composable("location_search") {
                LocationSearchScreen(
                    onRouteSelected = {
                        // 경로 검색 화면으로 이동하면서 출발지와 도착지 정보 전달
                        navController.navigate("route_search") {
                            // popUpTo("location_search") { inclusive = true }
                        }
                    },
                    onBackClick = {
                        navController.navigateUp()
                    },
                    locationViewModel = locationViewModel,
                    routeViewModel = routeViewModel
                )
            }

            composable("route_search") {
                RouteSearchScreen(
                    onBackClick = {
                        navController.navigate("location_search") {
                            // popUpTo("location_search") { inclusive = true }
                        }
                    },
                    routeViewModel = routeViewModel
                )
            }

            composable("favorite_edit") {
                FavoriteEditScreen(
                    onBackClick = {
                        navController.navigateUp()
                    }
                )
            }
        }
        
    }
} 
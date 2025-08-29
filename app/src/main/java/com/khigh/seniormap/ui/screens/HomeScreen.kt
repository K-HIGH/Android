package com.khigh.seniormap.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.khigh.seniormap.ui.composables.*
import com.khigh.seniormap.ui.model.Person
import com.khigh.seniormap.ui.model.PersonStatus
import com.khigh.seniormap.viewmodel.AuthViewModel
import androidx.compose.ui.tooling.preview.Preview

/**
 * 홈 화면 컴포넌트
 * 
 * '나의 부모님' 목록을 표시하는 메인 홈 화면입니다.
 * 각 부모님을 클릭하면 프로필 화면으로 이동할 수 있습니다.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToLogin: () -> Unit = {},
    onNavigateToProfile: (Person) -> Unit = {},
    onNavigateToAddPerson: () -> Unit = {},
    onNavigate: (String) -> Unit = {},
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel
) {
    Log.d("com.khigh.seniormap", "[HomeScreen] HomeScreen composable started")
    
    val authState by authViewModel.authState.collectAsState()
    
    // 인증 상태가 false가 되면 로그인 화면으로 이동
    LaunchedEffect(authState) {
        if (authState is io.github.jan.supabase.auth.status.SessionStatus.NotAuthenticated) {
            Log.d("com.khigh.seniormap", "[HomeScreen] User logged out, navigating to login")
            onNavigateToLogin()
        }
    }
    
    // 샘플 데이터 (실제로는 ViewModel에서 가져와야 함)
    val sampleParents = remember {
        listOf(
            Person(
                id = "1",
                name = "김혜자",
                status = PersonStatus.HOME,
                age = 68,
                relationship = "어머니"
            ),
            Person(
                id = "2",
                name = "송강호",
                status = PersonStatus.OUTING,
                age = 72,
                relationship = "아버지"
            ),
            Person(
                id = "3",
                name = "나문희",
                status = PersonStatus.HOME,
                age = 65,
                relationship = "어머니"
            ),
            Person(
                id = "4",
                name = "최불암",
                status = PersonStatus.PENDING,
                age = 70,
                relationship = "아버지"
            )
        )
    }
    
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(bottom = 90.dp) // 하단 네비게이션 바 높이만큼 패딩
        ) {
            // 헤더
            HomeHeader(
                onAddPerson = onNavigateToAddPerson
            )
            
            // 부모님 목록
            ParentsList(
                parents = sampleParents,
                onPersonClick = onNavigateToProfile
            )
        }
        
        // 하단 네비게이션 바를 화면 하단에 고정
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        ) {
            BottomNavigationBar(
                currentRoute = "home",
                onNavigate = onNavigate
            )
        }
    }
}

/**
 * Preview 함수 - UI 확인용
 */
@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun HomeScreenPreview() {
    HomeScreen(
        onNavigateToLogin = {},
        onNavigateToProfile = {},
        onNavigateToAddPerson = {},
        onNavigate = {}
    )
} 
package com.khigh.seniormap.ui.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import com.khigh.seniormap.ui.composables.navigation.AppNavigation
import com.khigh.seniormap.ui.theme.SeniorMapTheme
import com.khigh.seniormap.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.status.SessionStatus
import io.github.jan.supabase.SupabaseClient
import javax.inject.Inject

/**
 * 메인 Activity
 * 
 * 앱의 진입점이며 네비게이션과 전체 테마를 관리합니다.
 * Hilt를 사용한 의존성 주입을 통해 AuthViewModel을 관리하고,
 * 사용자 인증 상태에 따른 화면 전환을 처리합니다.
 * 
 * OAuth 딥링크 콜백도 이 Activity에서 처리합니다.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    
    private var authViewModel: AuthViewModel? = null

    @Inject
    lateinit var supabaseClient: SupabaseClient
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        setContent {
            SeniorMapTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainContent()
                }
            }
        }
        
        // 초기 인텐트 처리
        handleIntent(intent)
    }
    
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        // 새로운 인텐트 처리 (앱이 이미 실행 중일 때)
        handleIntent(intent)
    }
    
    /**
     * 딥링크 인텐트 처리
     */
    private fun handleIntent(intent: Intent) {
        val action = intent.action
        
        Log.d("com.khigh.seniormap", "[MainActivity] handleIntent - action: $action")
        
        if (action == Intent.ACTION_VIEW) {
            handleDeepLink(intent)
        }
    }
    
    /**
     * OAuth 딥링크 처리
     */
    private fun handleDeepLink(intent: Intent) {
        Log.d("com.khigh.seniormap", "[MainActivity] handleDeepLink: $intent")
        val uri = intent.data

        when {
            uri?.scheme == "com.khigh.seniormap" && uri.host == "oauth" -> {
                Log.d("com.khigh.seniormap", "[MainActivity] OAuth callback detected")
                authViewModel?.handleCallback(intent)
            }
            else -> {
                Log.d("com.khigh.seniormap", "[MainActivity] Unknown deep link: $intent")
            }
        }
    }
    
    @Composable
    private fun MainContent() {
        val viewModel: AuthViewModel = hiltViewModel()
        
        // AuthViewModel 참조 저장
        LaunchedEffect(viewModel) {
            authViewModel = viewModel
        }
        
        // 인증 관련 상태 관찰
        val authState by viewModel.authState.collectAsState()
        val isSigningIn by viewModel.isSigningIn.collectAsState()
        val errorMessage by viewModel.errorMessage.collectAsState()
        
        Scaffold(
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            AppNavigation(
                modifier = Modifier.padding(innerPadding),
                isAuthenticated = authState != SessionStatus.NotAuthenticated(),
                isSigningIn = isSigningIn,
                errorMessage = errorMessage,
                onClearError = { viewModel.clearErrorMessage() }
            )
        }
    }
} 
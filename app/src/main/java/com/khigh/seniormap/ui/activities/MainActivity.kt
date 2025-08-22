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
    
    @Inject
    lateinit var supabaseClient: SupabaseClient
    
    // AuthViewModel 참조를 저장하기 위한 변수
    private var authViewModelRef: AuthViewModel? = null
    
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
        
        // 브라우저에서 돌아올 때 직접 AuthViewModel을 통해 처리
        if (intent.action == Intent.ACTION_VIEW) {
            val uri = intent.data
            if (uri?.scheme == "com.khigh.seniormap" && uri.host == "oauth") {
                Log.d("com.khigh.seniormap", "[MainActivity] onNewIntent: OAuth callback detected")
                authViewModelRef?.let { authViewModel ->
                    Log.d("com.khigh.seniormap", "[MainActivity] onNewIntent: Calling handleCallback directly")
                    authViewModel.handleCallback(intent)
                } ?: run {
                    Log.d("com.khigh.seniormap", "[MainActivity] onNewIntent: AuthViewModel not available, fallback to regular handling")
                    handleIntent(intent)
                }
                return
            }
        }
        
        // 일반적인 인텐트 처리
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
     * OAuth 딥링크 처리 (AuthViewModel이 없을 때)
     */
    private fun handleDeepLink(intent: Intent) {
        Log.d("com.khigh.seniormap", "[MainActivity] handleDeepLink: $intent")
        val uri = intent.data

        when {
            uri?.scheme == "com.khigh.seniormap" && uri.host == "oauth" -> {
                Log.d("com.khigh.seniormap", "[MainActivity] OAuth callback detected - but AuthViewModel not available yet")
                // AuthViewModel이 아직 사용 가능하지 않으므로, Compose에서 처리하도록 Intent 저장
                setIntent(intent)
            }
            else -> {
                Log.d("com.khigh.seniormap", "[MainActivity] Unknown deep link: $intent")
            }
        }
    }
    
    /**
     * AuthViewModel을 사용한 딥링크 처리
     */
    private fun handleDeepLinkWithViewModel(intent: Intent, authViewModel: AuthViewModel) {
        Log.d("com.khigh.seniormap", "[MainActivity] handleDeepLinkWithViewModel: $intent")
        val uri = intent.data

        when {
            uri?.scheme == "com.khigh.seniormap" && uri.host == "oauth" -> {
                Log.d("com.khigh.seniormap", "[MainActivity] OAuth callback detected - calling handleCallback")
                authViewModel.handleCallback(intent)
            }
            else -> {
                Log.d("com.khigh.seniormap", "[MainActivity] Unknown deep link: $intent")
            }
        }
    }
    
    @Composable
    private fun MainContent() {
        val authViewModel: AuthViewModel = hiltViewModel()
        
        // ViewModel 준비 상태 확인 및 참조 저장
        val isViewModelReady by authViewModel.isReady.collectAsState()
        
        // AuthViewModel 참조 저장 (onNewIntent에서 사용하기 위해)
        LaunchedEffect(authViewModel, isViewModelReady) {
            if (isViewModelReady) {
                authViewModelRef = authViewModel
                Log.d("com.khigh.seniormap", "[MainActivity] AuthViewModel ready and reference saved")
                
                // 초기 Intent 처리 (onCreate에서 들어온 딥링크)
                intent?.let { currentIntent ->
                    if (currentIntent.action == Intent.ACTION_VIEW) {
                        handleDeepLinkWithViewModel(currentIntent, authViewModel)
                    }
                }
            } else {
                Log.d("com.khigh.seniormap", "[MainActivity] AuthViewModel not ready yet")
            }
        }
        
        // 인증 관련 상태 관찰
        val authState by authViewModel.authState.collectAsState()
        val isSigningIn by authViewModel.isSigningIn.collectAsState()
        val errorMessage by authViewModel.errorMessage.collectAsState()
        
        Scaffold(
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            AppNavigation(
                authViewModel = authViewModel,
                modifier = Modifier.padding(innerPadding),
                isAuthenticated = authState != SessionStatus.NotAuthenticated(),
                isSigningIn = isSigningIn,
                errorMessage = errorMessage,
                onClearError = { authViewModel.clearErrorMessage() }
            )
        }
    }
} 
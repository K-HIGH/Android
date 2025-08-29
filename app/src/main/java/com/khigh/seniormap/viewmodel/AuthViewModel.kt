package com.khigh.seniormap.viewmodel

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khigh.seniormap.model.dto.supabaseauth.*
import com.khigh.seniormap.model.dto.auth.*
import com.khigh.seniormap.model.entity.UserEntity
import com.khigh.seniormap.repository.AuthRepository
import com.khigh.seniormap.repository.SupabaseAuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import io.github.jan.supabase.auth.providers.OAuthProvider
import io.github.jan.supabase.auth.status.SessionStatus
import io.github.jan.supabase.SupabaseClient

/**
 * Supabase 기반 인증 ViewModel
 */
@HiltViewModel
class AuthViewModel @Inject constructor(
    private val supabaseAuthRepository: SupabaseAuthRepository,
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _tag = "com.khigh.seniormap.viewmodel.AuthViewModel"
    
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()
    
    private val _signingIn = MutableStateFlow(false)
    val isSigningIn: StateFlow<Boolean> = _signingIn.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _signingOut = MutableStateFlow(false)
    val isSigningOut: StateFlow<Boolean> = _signingOut.asStateFlow()

    private val _isUserRegistered = MutableStateFlow(false)
    val isUserRegistered: StateFlow<Boolean> = _isUserRegistered.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()
    
    // ViewModel 초기화 완료 상태
    private val _isReady = MutableStateFlow(false)
    val isReady: StateFlow<Boolean> = _isReady.asStateFlow()
    
    // 인증 상태 관찰
    val authState = supabaseAuthRepository.observeAuthState()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = supabaseAuthRepository.getCurrentSessionStatus()
        )
    
    // 현재 사용자 정보 관찰
    val currentUser = supabaseAuthRepository.observeCurrentUser()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )
    
    init {
        checkAuthState()
        // 앱 시작 시 세션 복원 시도
        restoreSessionIfNeeded()
        
        // 초기화 완료 표시
        viewModelScope.launch {
            _isReady.value = true
            Log.d(_tag, "AuthViewModel initialization completed")
        }
    }
    
    /**
     * 앱 시작 시 세션 복원 시도
     */
    private fun restoreSessionIfNeeded() {
        viewModelScope.launch {
            try {
                Log.d(_tag, "Attempting session restore on app start")
                refreshSession()
                // val localUser = supabaseAuthRepository.getLocalUser()
                val accessToken = supabaseAuthRepository.getAccessToken()
                val refreshToken = supabaseAuthRepository.getRefreshToken()
                
                if (!accessToken.isNullOrEmpty()) {
                    Log.d(_tag, "Local session found, attempting to restore")
                    
                    // 현재 사용자 정보 확인
                    supabaseAuthRepository.getCurrentUser()
                        .onSuccess { user ->
                            if (user != null) {
                                Log.d(_tag, "Session restored successfully: ${user.id}")
                            } else {
                                Log.w(_tag, "Session restore failed - no current user")
                            }
                        }
                        .onFailure { error ->
                            Log.w(_tag, "Session restore failed", error)
                            // 세션 복원 실패 시 로컬 데이터 정리
                            supabaseAuthRepository.clearTokens()
                            supabaseAuthRepository.clearLocalUser()
                        }
                } else {
                    Log.d(_tag, "No local session found")
                }
            } catch (e: Exception) {
                Log.e(_tag, "Session restore error", e)
            }
        }
    }
    
    /**
     * Supabase OAuth 로그인 (카카오, 구글)
     * @param provider "google" 또는 "kakao"
     */
    fun loginWithOAuth(provider: OAuthProvider) {
        viewModelScope.launch {
            _isLoading.value = true
            _signingIn.value = true
            _errorMessage.value = null
            
            supabaseAuthRepository.loginWithOAuth(provider)
                .onSuccess { 
                    // pass
                }
                .onFailure { error ->
                    _errorMessage.value = error.message ?: "OAuth 로그인에 실패했습니다."
                    _signingIn.value = false
                }
            _isLoading.value = false
        }
    }

    fun handleCallback(intent: Intent) {
        viewModelScope.launch {
            Log.d(_tag, "handleCallback start: _signingIn.value: ${_signingIn.value}")
            _isLoading.value = true
            _signingIn.value = true
            supabaseAuthRepository.handleCallback(intent)
                .onSuccess {
                    Log.d(_tag, "handleCallback: ${intent.data}")
                    val uri = intent.data.toString().toUri()
                    val fragment = parseFragmentParameters(uri.fragment ?: "")
                    val accessToken = fragment.get("access_token")
                    val refreshToken = fragment.get("refresh_token")
                    Log.d(_tag, "handleCallback:accessToken: ${accessToken}")
                    Log.d(_tag, "handleCallback:refreshToken: ${refreshToken}")
                    if (accessToken != null && refreshToken != null) {
                        supabaseAuthRepository.saveAccessToken(accessToken)
                        supabaseAuthRepository.saveRefreshToken(refreshToken)
                    } else {
                        _errorMessage.value = "액세스 토큰 또는 리프레시 토큰을 찾을 수 없습니다."
                        _signingIn.value = false
                    }
                }
                .onFailure { error ->
                    _errorMessage.value = error.message ?: "OAuth 딥링크 처리에 실패했습니다."
                    _signingIn.value = false
                }
            
            val accessToken = supabaseAuthRepository.getAccessToken()
            Log.d(_tag, "handleCallback:login:accessToken: ${accessToken}")
            Log.d(_tag, "accessToken before login: _signingIn.value: ${_signingIn.value}")
            if (accessToken != null) {
                authRepository.login(UserLoginRequest(accessToken = accessToken))
                .onSuccess { response ->
                    Log.d(_tag, "handleCallback:login:response: ${response}, _signingIn.value: ${_signingIn.value}")
                    Log.d(_tag, "handleCallback:login: ${accessToken}, _signingIn.value: ${_signingIn.value}")

                    response.body()?.let { user ->
                        Log.d(_tag, "handleCallback:login:user.user.isRegistered: ${user.user.isRegistered}, _signingIn.value: ${_signingIn.value}")
                        _isUserRegistered.value = user.user.isRegistered
                    }

                    Log.d(_tag, "handleCallback:login: _signingIn: ${_signingIn.value}")
                    _isLoading.value = false
                }
                .onFailure { error ->
                    _errorMessage.value = error.message ?: "OAuth 딥링크 처리에 실패했습니다."
                    Log.e(_tag, "handleCallback:login: ${error.message}")
                    Log.e(_tag, "handleCallback:login: ${error.cause}")
                    _isLoading.value = false
                    _signingIn.value = false
                }
            } else {
                _errorMessage.value = "액세스 토큰을 찾을 수 없습니다."
                _signingIn.value = false
                _isLoading.value = false
            }
        }
    }

    fun onAuthResultHandled() {
        _signingIn.value = false
    }

    
    /**
     * 이메일/패스워드 로그인
     */
    // fun loginWithEmail(email: String, password: String) {
    //     viewModelScope.launch {
    //         _isLoading.value = true
    //         _errorMessage.value = null
            
    //         supabaseAuthRepository.loginWithEmail(email, password)
    //             .onSuccess { 
    //                 handleOAuthCallback()
                    
    //                 _uiState.value = _uiState.value.copy(
    //                     isLoggedIn = true,
    //                     isOAuthFlow = false,
    //                 )
    //             }
    //             .onFailure { error ->
    //                 _errorMessage.value = error.message ?: "이메일 로그인에 실패했습니다."
    //             }
            
    //         _isLoading.value = false
    //     }
    // }
    
    
    /**
     * 세션 갱신
     */
    fun refreshSession() {
        viewModelScope.launch {
            _isLoading.value = true
            supabaseAuthRepository.refreshSession()
                .onSuccess { user ->
                    Log.d(_tag, "refreshSession: user: ${user}")
                    val accessToken = supabaseAuthRepository.getAccessToken()
                    Log.d(_tag, "refreshSession: accessToken: ${accessToken}")
                    if (accessToken != null) {
                        authRepository.login(UserLoginRequest(accessToken = accessToken))
                            .onSuccess { response ->
                                Log.d(_tag, "refreshSession:login:response: ${response}")
                            }
                            .onFailure { error ->
                                Log.e(_tag, "refreshSession:login: ${error.message}")
                            }
                    } else {
                        Log.e(_tag, "refreshSession: accessToken is null")
                    }
                }
                .onFailure { error ->
                    // 세션 갱신 실패 시 로그아웃 처리
                    logout()
                }
            _isLoading.value = false
        }
    }
    
    /**
     * 로그아웃
     * 
     * AuthRepository를 통해 서버 로그아웃 및 로컬 데이터 정리를 수행합니다.
     * 성공/실패 여부와 관계없이 로컬 상태는 초기화하여 안전성을 보장합니다.
     */
    fun logout() {
        viewModelScope.launch {
            _signingOut.value = true
            supabaseAuthRepository.logout()
                .onSuccess {
                    authRepository.logout()
                        .onSuccess {
                        }
                        .onFailure { error ->
                            _errorMessage.value = error.message ?: "Server 로그아웃에 실패했습니다."
                        }
                }
                .onFailure { error ->
                    _errorMessage.value = error.message ?: "Supabase 로그아웃에 실패했습니다."
                    // 실패해도 로컬 상태는 리셋 (보안상 중요)
                    
                }
            _uiState.value = AuthUiState()
            // observeAuthState()가 자동으로 false를 반환하게 됨
            // (clearTokens()와 clearLocalUser()가 호출되었으므로)
            _signingOut.value = false
        }
    }
    
    /**
     * 프로필 업데이트
     */
    // fun updateProfile(request: UserProfileUpdateRequest) {
    //     viewModelScope.launch {
    //         _isLoading.value = true
    //         _errorMessage.value = null
            
    //         supabaseAuthRepository.updateProfile(request)
    //             .onSuccess { updatedUser ->
    //                 // val updatedEntity = updatedUser.toEntity()
    //                 // supabaseAuthRepository.saveUserLocally(updatedEntity)
                    
    //                 // _uiState.value = _uiState.value.copy(user = updatedEntity)
    //             }
    //             .onFailure { error ->
    //                 _errorMessage.value = error.message ?: "프로필 업데이트에 실패했습니다."
    //             }
            
    //         _isLoading.value = false
    //     }
    // }
    
    /**
     * 계정 삭제
     */
    fun deleteAccount() {
        viewModelScope.launch {
            _signingOut.value = true
            supabaseAuthRepository.deleteUser()
                .onSuccess {
                    _uiState.value = AuthUiState() // 초기 상태로 리셋
                }
                .onFailure { error ->
                    _errorMessage.value = error.message ?: "계정 삭제에 실패했습니다."
                }
            _signingOut.value = false
        }
    }
    
    /**
     * 현재 사용자 정보 새로고침
     */
    fun getCurrentUser() {
        viewModelScope.launch {
            supabaseAuthRepository.getCurrentUser()
                .onSuccess { user ->
                    if (user != null) {
                        // val userEntity = user.toEntity()
                        // supabaseAuthRepository.saveUserLocally(userEntity)
                        
                        // _uiState.value = _uiState.value.copy(
                        //     isLoggedIn = true,
                        //     user = userEntity
                        // )
                    }
                }
                .onFailure {
                    // 사용자 정보 조회 실패
                }
        }
    }
    
    /**
     * OAuth URL 클리어
     */
    fun clearOAuthUrl() {
        _uiState.value = _uiState.value.copy(
            oAuthUrl = null,
            isOAuthFlow = false
        )
    }
    
    /**
     * 에러 메시지 클리어
     */
    fun clearErrorMessage() {
        _errorMessage.value = null
    }
    
    /**
     * 인증 상태 확인
     */
    private fun checkAuthState() {
        viewModelScope.launch {
            val localUser = supabaseAuthRepository.getLocalUser()
            val accessToken = supabaseAuthRepository.getAccessToken()
            
            _uiState.value = _uiState.value.copy(
                isLoggedIn = localUser != null && accessToken != null,
                user = localUser
            )
            
            // 로그인 상태라면 현재 사용자 정보 갱신
            if (localUser != null && accessToken != null) {
                getCurrentUser()
            }
        }
    }
}

/**
 * 인증 UI 상태 (Supabase 방식)
 */
data class AuthUiState(
    val isLoggedIn: Boolean = false,
    val user: UserEntity? = null,
    val isOAuthFlow: Boolean = false,
    val oAuthUrl: String? = null
)

private fun parseFragmentParameters(fragment: String): Map<String, String> {
    return fragment.split('&').mapNotNull { part ->
        val keyValue = part.split('=', limit = 2)
        if (keyValue.size == 2) {
            val key = keyValue[0]
            val value = keyValue[1]
            key to value
        } else {
            null
        }
    }.toMap()
}
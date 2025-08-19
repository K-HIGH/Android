package com.khigh.seniormap.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khigh.seniormap.model.dto.ProfileUpdateRequest
import com.khigh.seniormap.model.entity.UserEntity
import com.khigh.seniormap.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 인증 관련 ViewModel
 */
@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()
    
    // 인증 상태 관찰
    val isAuthenticated = authRepository.observeAuthState()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )
    
    // 현재 사용자 정보 관찰
    val currentUser = authRepository.observeCurrentUser()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )
    
    init {
        checkAuthState()
    }
    
    /**
     * OAuth 로그인
     */
    fun loginWithOAuth(provider: String, accessToken: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            
            authRepository.loginWithOAuth(provider, accessToken)
                .onSuccess { response ->
                    // 토큰 저장
                    authRepository.saveAccessToken(response.accessToken)
                    authRepository.saveRefreshToken(response.refreshToken)
                    
                    // 사용자 정보 로컬 저장
                    val userEntity = response.user.toEntity()
                        .copy(
                            accessToken = response.accessToken,
                            refreshToken = response.refreshToken
                        )
                    authRepository.saveUserLocally(userEntity)
                    
                    _uiState.value = _uiState.value.copy(
                        isLoggedIn = true,
                        user = userEntity
                    )
                }
                .onFailure { error ->
                    _errorMessage.value = error.message ?: "로그인에 실패했습니다."
                }
            
            _isLoading.value = false
        }
    }
    
    /**
     * 로그아웃
     */
    fun logout() {
        viewModelScope.launch {
            _isLoading.value = true
            
            authRepository.logout()
                .onSuccess {
                    authRepository.clearTokens()
                    authRepository.clearLocalUser()
                    
                    _uiState.value = AuthUiState() // 초기 상태로 리셋
                }
                .onFailure { error ->
                    _errorMessage.value = error.message ?: "로그아웃에 실패했습니다."
                }
            
            _isLoading.value = false
        }
    }
    
    /**
     * 프로필 업데이트
     */
    fun updateProfile(request: ProfileUpdateRequest) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            
            authRepository.updateProfile(request)
                .onSuccess { updatedUser ->
                    val currentUserEntity = authRepository.getLocalUser()
                    currentUserEntity?.let { user ->
                        val updatedEntity = user.copy(
                            userName = updatedUser.userName,
                            phone = updatedUser.phone,
                            isCaregiver = updatedUser.isCaregiver,
                            isHelper = updatedUser.isHelper
                        )
                        authRepository.saveUserLocally(updatedEntity)
                        
                        _uiState.value = _uiState.value.copy(user = updatedEntity)
                    }
                }
                .onFailure { error ->
                    _errorMessage.value = error.message ?: "프로필 업데이트에 실패했습니다."
                }
            
            _isLoading.value = false
        }
    }
    
    /**
     * 계정 삭제
     */
    fun deleteAccount() {
        viewModelScope.launch {
            _isLoading.value = true
            
            authRepository.deleteUser()
                .onSuccess {
                    authRepository.clearTokens()
                    authRepository.clearLocalUser()
                    
                    _uiState.value = AuthUiState() // 초기 상태로 리셋
                }
                .onFailure { error ->
                    _errorMessage.value = error.message ?: "계정 삭제에 실패했습니다."
                }
            
            _isLoading.value = false
        }
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
            val localUser = authRepository.getLocalUser()
            val accessToken = authRepository.getAccessToken()
            
            _uiState.value = _uiState.value.copy(
                isLoggedIn = localUser != null && accessToken != null,
                user = localUser
            )
        }
    }
}

/**
 * 인증 UI 상태
 */
data class AuthUiState(
    val isLoggedIn: Boolean = false,
    val user: UserEntity? = null
)

/**
 * UserDto를 UserEntity로 변환하는 확장 함수
 */
private fun com.khigh.seniormap.model.dto.UserDto.toEntity(): UserEntity {
    return UserEntity(
        id = this.id,
        email = this.email,
        userName = this.userName,
        phone = this.phone,
        isCaregiver = this.isCaregiver,
        isHelper = this.isHelper,
        fcmToken = this.fcmToken,
        isAlert = this.isAlert,
        accessToken = null,
        refreshToken = null
    )
} 
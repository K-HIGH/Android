package com.khigh.seniormap.viewmodel

import javax.inject.Inject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import com.khigh.seniormap.repository.UserRepository
import com.khigh.seniormap.model.dto.auth.*
import com.khigh.seniormap.model.dto.user.*
import com.khigh.seniormap.model.dto.ApiMessage
import com.khigh.seniormap.model.entity.UserEntity
import dagger.hilt.android.lifecycle.HiltViewModel


@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _tag = "com.khigh.seniormap.viewmodel.UserViewModel"

    private val _uiState = MutableStateFlow(UserUiState())
    val uiState: StateFlow<UserUiState> = _uiState.asStateFlow()

    private val _isCaregiver = MutableStateFlow(false)
    val isCaregiver: StateFlow<Boolean> = _isCaregiver.asStateFlow()
    private val _isHelper = MutableStateFlow(false)
    val isHelper: StateFlow<Boolean> = _isHelper.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()
    
    init {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true
            )
            _isCaregiver.value = _uiState.value.userEntity?.isCaregiver ?: false
            _isHelper.value = _uiState.value.userEntity?.isHelper ?: false
        }
    }
    
    fun getUserInfo() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true
            )
            _errorMessage.value = null

            userRepository.getUserInfo()
                .onSuccess { response ->
                    if (response.isSuccessful) {
                        val userResponse = response.body()
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            userEntity = userResponse?.toEntity()
                        )
                    }
                }
                .onFailure {
                    _errorMessage.value = it.message
                    _uiState.value = _uiState.value.copy(
                        isLoading = false
                    )
                }
        }
    }

    fun updateUserProfile(request: UserProfileUpdateRequest) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true
            )
            _errorMessage.value = null

            userRepository.updateUserProfile(request)
                .onSuccess { response ->
                    if (response.isSuccessful) {
                        val userResponse = response.body()
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            userEntity = userResponse?.toEntity()
                        )
                    }
                }
                .onFailure {
                    _errorMessage.value = it.message
                    _uiState.value = _uiState.value.copy(
                        isLoading = false
                    )
                }
        }
    }

    fun updateFcmToken(request: FcmTokenRequest) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true
            )
            _errorMessage.value = null

            userRepository.updateFcmToken(request)
                .onSuccess { response ->
                    if (response.isSuccessful) {
                        val userResponse = response.body()
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            userEntity = userResponse?.toEntity()
                        )
                    }
                }
                .onFailure {
                    _errorMessage.value = it.message
                    _uiState.value = _uiState.value.copy(
                        isLoading = false
                    )
                }
        }
    }

    fun updateAlertFlag(request: AlertFlagRequest) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true
            )
            _errorMessage.value = null

            userRepository.updateAlertFlag(request)
                .onSuccess { response ->
                    if (response.isSuccessful) {
                        val userResponse = response.body()
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            userEntity = userResponse?.toEntity()
                        )
                    }
                }
                .onFailure {
                    _errorMessage.value = it.message
                    _uiState.value = _uiState.value.copy(
                        isLoading = false
                    )
                }
        }
    }
    
    fun deleteUser() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true
            )
            _errorMessage.value = null

            userRepository.deleteUser()
                .onSuccess { response ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        userEntity = null
                    )
                }
                .onFailure {
                    _errorMessage.value = it.message
                    _uiState.value = _uiState.value.copy(
                        isLoading = false
                    )
                }
        }
    }
}

data class UserUiState(
    val isLoading: Boolean = false,
    val userEntity: UserEntity? = null
)
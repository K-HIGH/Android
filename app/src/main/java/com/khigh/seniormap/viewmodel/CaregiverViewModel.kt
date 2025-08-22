package com.khigh.seniormap.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.khigh.seniormap.repository.AuthRepository
import com.khigh.seniormap.repository.CaregiverRepository
import com.khigh.seniormap.model.dto.caregiver.CaregiverResponse
import com.khigh.seniormap.model.dto.caregiver.CaregiverCreateRequest
import com.khigh.seniormap.model.dto.ApiMessage
import com.khigh.seniormap.repository.UserRepository


@HiltViewModel
class CaregiverViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val caregiverRepository: CaregiverRepository
) : ViewModel() {
    private val _tag = "com.khigh.seniormap.viewmodel.CaregiverViewModel"

    private val _uiState = MutableStateFlow(CaregiverUiState())
    val uiState: StateFlow<CaregiverUiState> = _uiState.asStateFlow()

    val _isAddingCaregiver = MutableStateFlow(false)
    val isAddingCaregiver: StateFlow<Boolean> = _isAddingCaregiver.asStateFlow()

    val _caregivers = MutableStateFlow<List<CaregiverResponse>>(emptyList())
    val caregivers: StateFlow<List<CaregiverResponse>> = _caregivers.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()
    
    init {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true
            )
        }
    }

    fun getCaregivers() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true
            )
            _errorMessage.value = null

            caregiverRepository.getCaregivers()
                .onSuccess { response ->
                    if (response.isSuccessful) {
                        val caregivers = response.body() ?: emptyList()
                        _caregivers.value = caregivers
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            caregivers = caregivers
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

    fun createCaregiver(targetEmail: String) {
        viewModelScope.launch {
            _isAddingCaregiver.value = true
            _uiState.value = _uiState.value.copy(
                isLoading = true
            )

            val request = CaregiverCreateRequest(targetEmail = targetEmail)
            val response = caregiverRepository.createCaregiver(request)
                .onSuccess { response ->
                    if (response.isSuccessful) {
                        val caregiverResponse = response.body() ?: return@onSuccess
                        _caregivers.value = _caregivers.value + caregiverResponse
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            caregivers = _caregivers.value + caregiverResponse
                        )
                        _isAddingCaregiver.value = false
                    } else {
                        _errorMessage.value = "보호자 관계 생성 실패: ${response.message()}"
                        _uiState.value = _uiState.value.copy(
                            isLoading = false
                        )
                        _isAddingCaregiver.value = false
                    }
                }
                .onFailure {
                    _errorMessage.value = it.message
                    _uiState.value = _uiState.value.copy(
                        isLoading = false
                    )
                    _isAddingCaregiver.value = false
                }
        }
    }

    fun deleteCaregiver(caregiverId: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true
            )
            _errorMessage.value = null

            val response = caregiverRepository.deleteCaregiver(caregiverId)
                .onSuccess { response ->
                    if (response.isSuccessful) {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false
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
}

data class CaregiverUiState(
    val isLoading: Boolean = false,
    val caregivers: List<CaregiverResponse> = emptyList(),
    val errorMessage: String? = null
)
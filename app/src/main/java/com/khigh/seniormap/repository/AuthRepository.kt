package com.khigh.seniormap.repository

import com.khigh.seniormap.model.dto.*
import com.khigh.seniormap.model.entity.UserEntity
import kotlinx.coroutines.flow.Flow

/**
 * 인증 관련 Repository 인터페이스
 */
interface AuthRepository {
    
    /**
     * OAuth 로그인
     */
    suspend fun loginWithOAuth(provider: String, accessToken: String): Result<LoginResponse>
    
    /**
     * 토큰 갱신
     */
    suspend fun refreshToken(refreshToken: String): Result<LoginResponse>
    
    /**
     * 로그아웃
     */
    suspend fun logout(): Result<Unit>
    
    /**
     * 사용자 프로필 조회
     */
    suspend fun getProfile(): Result<UserDto>
    
    /**
     * 사용자 프로필 업데이트
     */
    suspend fun updateProfile(request: ProfileUpdateRequest): Result<UserDto>
    
    /**
     * 계정 삭제
     */
    suspend fun deleteUser(): Result<Unit>
    
    /**
     * 로컬 사용자 정보 저장
     */
    suspend fun saveUserLocally(user: UserEntity)
    
    /**
     * 로컬 사용자 정보 조회
     */
    suspend fun getLocalUser(): UserEntity?
    
    /**
     * 로컬 사용자 정보 삭제
     */
    suspend fun clearLocalUser()
    
    /**
     * 액세스 토큰 저장
     */
    suspend fun saveAccessToken(token: String)
    
    /**
     * 액세스 토큰 조회
     */
    suspend fun getAccessToken(): String?
    
    /**
     * 리프레시 토큰 저장
     */
    suspend fun saveRefreshToken(token: String)
    
    /**
     * 리프레시 토큰 조회
     */
    suspend fun getRefreshToken(): String?
    
    /**
     * 모든 토큰 삭제
     */
    suspend fun clearTokens()
    
    /**
     * 로그인 상태 관찰
     */
    fun observeAuthState(): Flow<Boolean>
    
    /**
     * 현재 사용자 정보 관찰
     */
    fun observeCurrentUser(): Flow<UserEntity?>
} 
package com.khigh.seniormap.repository

import com.khigh.seniormap.model.dto.auth.*
import com.khigh.seniormap.model.entity.UserEntity
import com.khigh.seniormap.model.dto.ApiMessage

/**
 * K-HIGH 서버 API Repository 인터페이스
 * 
 * Supabase OAuth 토큰을 사용하여 K-HIGH 서버와 통신하는
 * 비즈니스 로직을 정의합니다.
 */
interface AuthRepository {
    
    // ==================== Auth API ====================
    
    /**
     * K-HIGH 서버에 로그인
     * Supabase access_token을 전달하여 서버 세션 동기화
     */
    suspend fun login(request: UserLoginRequest): Result<ApiMessage>
    
    /**
     * K-HIGH 서버에서 로그아웃
     */
    suspend fun logout(): Result<Unit>
    
    // ==================== User API ====================
    
    /**
     * K-HIGH 서버에서 현재 사용자 정보 조회
     */
    suspend fun getCurrentUser(): Result<UserLoginResponse>
    
    /**
     * K-HIGH 서버에 사용자 프로필 업데이트
     */
    suspend fun updateUserProfile(request: UserProfileUpdateRequest): Result<Unit>
    
    /**
     * K-HIGH 서버에 FCM 토큰 업데이트
     */
    suspend fun updateFcmToken(request: FcmTokenRequest): Result<Unit>
    
    /**
     * K-HIGH 서버에 알림 설정 업데이트
     */
    suspend fun updateAlertFlag(request: AlertFlagRequest): Result<Unit>
    
    /**
     * K-HIGH 서버에서 사용자 계정 삭제
     */
    suspend fun deleteUser(): Result<Unit>
    
    // ==================== 통합 기능 ====================
    
    /**
     * OAuth 로그인 후 K-HIGH 서버 동기화
     * 1. K-HIGH 서버에 로그인
     * 2. 서버에서 사용자 정보 조회
     * 3. 로컬 UserEntity 업데이트
     */
    suspend fun syncWithServer(supabaseAccessToken: String, userId: String, email: String): Result<UserEntity>
} 
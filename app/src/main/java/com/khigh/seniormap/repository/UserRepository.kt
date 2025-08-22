package com.khigh.seniormap.repository

import com.khigh.seniormap.model.dto.user.*
import com.khigh.seniormap.model.dto.user.UserResponse
import com.khigh.seniormap.model.dto.ApiMessage
import retrofit2.Response

/**
 * K-HIGH 서버 API Repository 인터페이스
 * 
 * Supabase OAuth 토큰을 사용하여 K-HIGH 서버와 통신하는
 * 비즈니스 로직을 정의합니다.
 */
interface UserRepository {
    // ==================== User API ====================
    
    /**
     * K-HIGH 서버에서 현재 사용자 정보 조회
     */
    suspend fun getUserInfo(): Result<Response<UserResponse>>
    
    /**
     * K-HIGH 서버에서 사용자 프로필 업데이트
     */
    suspend fun updateUserProfile(request: UserProfileUpdateRequest): Result<Response<UserResponse>>
    
    
    /**
     * K-HIGH 서버에 FCM 토큰 업데이트
     */
    suspend fun updateFcmToken(request: FcmTokenRequest): Result<Response<UserResponse>>
    
    
    /**
     * K-HIGH 서버에 알림 설정 업데이트
     */
    suspend fun updateAlertFlag(request: AlertFlagRequest): Result<Response<UserResponse>>
    
    
    /**
     * K-HIGH 서버에서 사용자 계정 삭제
     */
    suspend fun deleteUser(): Result<Response<ApiMessage>>
}
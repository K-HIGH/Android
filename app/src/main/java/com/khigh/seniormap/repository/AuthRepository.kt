package com.khigh.seniormap.repository

import com.khigh.seniormap.model.dto.auth.*
import com.khigh.seniormap.model.dto.user.UserResponse
import com.khigh.seniormap.model.entity.UserEntity
import retrofit2.Response

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
    suspend fun login(request: UserLoginRequest): Result<Response<UserResponse>>
    
    /**
     * K-HIGH 서버에서 로그아웃
     */
    suspend fun logout(): Result<Unit>
    
    
    // ==================== 통합 기능 ====================
    
    /**
     * OAuth 로그인 후 K-HIGH 서버 동기화
     * 1. K-HIGH 서버에 로그인
     * 2. 서버에서 사용자 정보 조회
     * 3. 로컬 UserEntity 업데이트
     */
    suspend fun syncWithServer(supabaseAccessToken: String): Result<Unit>

    /**
     * 로컬 UserEntity 조회
     */
    suspend fun getUserEntity(): UserEntity?

    /**
     * 로컴 UserEntity 저장
     */
    suspend fun saveUserEntity(userEntity: UserEntity)
} 
package com.khigh.seniormap.network.api

import com.khigh.seniormap.model.dto.auth.*
import com.khigh.seniormap.model.dto.user.UserResponse
import com.khigh.seniormap.model.dto.ApiMessage
import retrofit2.Response
import retrofit2.http.*

/**
 * Server API 인터페이스
 * 
 * Supabase OAuth에서 받은 access_token을 사용하여
 * Server와 통신하는 API들을 정의합니다.
 */
interface AuthApi {
    
    // ==================== Auth API ====================
    
    /**
     * Server에 로그인
     * Supabase access_token을 전달하여 서버 세션 동기화
     */
    @POST("api/v1/auth/login")
    suspend fun login(
        @Body request: UserLoginRequest
    ): Response<UserResponse>
    
    /**
     * Server에서 로그아웃
     */
    @DELETE("api/v1/auth/logout")
    suspend fun logout(
        @Header("Authorization") token: String
    ): Response<Unit>
    
    // ==================== User API ====================
    /**
     * 사용자 계정 삭제
     */
    @DELETE("api/v1/users/me")
    suspend fun deleteUser(
        @Header("Authorization") token: String
    ): Response<Unit>
} 
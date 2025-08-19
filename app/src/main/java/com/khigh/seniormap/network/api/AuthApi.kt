package com.khigh.seniormap.network.api

import com.khigh.seniormap.model.dto.*
import retrofit2.Response
import retrofit2.http.*

/**
 * 인증 관련 API 인터페이스
 */
interface AuthApi {
    
    /**
     * OAuth 로그인
     */
    @POST("auth/oauth")
    suspend fun loginWithOAuth(
        @Body request: OAuthLoginRequest
    ): Response<LoginResponse>
    
    /**
     * 토큰 갱신
     */
    @POST("auth/refresh")
    suspend fun refreshToken(
        @Body request: RefreshTokenRequest
    ): Response<LoginResponse>
    
    /**
     * 로그아웃
     */
    @POST("auth/logout")
    suspend fun logout(
        @Header("Authorization") token: String
    ): Response<Unit>
    
    /**
     * 사용자 프로필 조회
     */
    @GET("users/profile")
    suspend fun getProfile(
        @Header("Authorization") token: String
    ): Response<UserDto>
    
    /**
     * 사용자 프로필 업데이트
     */
    @PUT("users/profile")
    suspend fun updateProfile(
        @Header("Authorization") token: String,
        @Body request: ProfileUpdateRequest
    ): Response<UserDto>
    
    /**
     * 계정 삭제
     */
    @DELETE("users/profile")
    suspend fun deleteUser(
        @Header("Authorization") token: String
    ): Response<Unit>
} 
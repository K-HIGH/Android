package com.khigh.seniormap.network.api

import com.khigh.seniormap.model.dto.ApiMessage
import com.khigh.seniormap.model.dto.user.*
import retrofit2.Response
import retrofit2.http.*

/**
 * K-HIGH 백엔드 서버의 User API 인터페이스
 * 
 * 사용자 정보 관리, 프로필 업데이트, 알림 설정 등의 기능을 제공합니다.
 * 모든 엔드포인트는 인증이 필요하며, Bearer 토큰을 사용합니다.
 * 
 * 주요 기능:
 * - 사용자 정보 조회
 * - 사용자 계정 삭제
 * - 사용자 프로필 업데이트
 * - FCM 토큰 관리
 * - 알림 설정 관리
 * 
 * @see UserResponse 사용자 정보 응답 모델
 * @see UserProfileUpdateRequest 프로필 업데이트 요청 모델
 */
interface UserApi {
    
    @GET("api/v1/users/me")
    suspend fun getUser(
        @Header("Authorization") token: String
    ): Response<UserResponse>
    
    @PUT("api/v1/users/me/profile")
    suspend fun updateUserProfile(
        @Header("Authorization") token: String,
        @Body request: UserProfileUpdateRequest
    ): Response<UserResponse>
    
    @PUT("api/v1/users/me/alert/fcm-token")
    suspend fun updateFcmToken(
        @Header("Authorization") token: String,
        @Body request: FcmTokenRequest
    ): Response<UserResponse>
    
    @PUT("api/v1/users/me/alert/flag")
    suspend fun updateAlertFlag(
        @Header("Authorization") token: String,
        @Body request: AlertFlagRequest
    ): Response<UserResponse>

    @DELETE("api/v1/users/me")
    suspend fun deleteUser(
        @Header("Authorization") token: String
    ): Response<ApiMessage>

}


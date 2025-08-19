package com.khigh.seniormap.model.dto

import com.google.gson.annotations.SerializedName

/**
 * OAuth 로그인 요청 DTO
 */
data class OAuthLoginRequest(
    @SerializedName("provider")
    val provider: String, // "kakao" | "google"
    @SerializedName("access_token")
    val accessToken: String
)

/**
 * 로그인 응답 DTO
 */
data class LoginResponse(
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("refresh_token")
    val refreshToken: String,
    @SerializedName("user")
    val user: UserDto
)

/**
 * 사용자 정보 DTO
 */
data class UserDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("user_name")
    val userName: String,
    @SerializedName("phone")
    val phone: String?,
    @SerializedName("is_caregiver")
    val isCaregiver: Boolean,
    @SerializedName("is_helper")
    val isHelper: Boolean,
    @SerializedName("fcm_token")
    val fcmToken: String?,
    @SerializedName("is_alert")
    val isAlert: Boolean
)

/**
 * 토큰 갱신 요청 DTO
 */
data class RefreshTokenRequest(
    @SerializedName("refresh_token")
    val refreshToken: String
)

/**
 * 프로필 업데이트 요청 DTO
 */
data class ProfileUpdateRequest(
    @SerializedName("user_name")
    val userName: String,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("is_caregiver")
    val isCaregiver: Boolean,
    @SerializedName("is_helper")
    val isHelper: Boolean
) 
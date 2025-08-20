package com.khigh.seniormap.model.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * Supabase OAuth 로그인 요청 DTO
 */
@Serializable
data class SupabaseOAuthLoginRequest(
    @SerialName("provider")
    val provider: String, // "kakao" | "google"
    @SerialName("redirect_to")
    val redirectTo: String? = null
)

/**
 * Supabase 로그인 응답 DTO
 */
@Serializable
data class SupabaseLoginResponse(
    @SerialName("access_token")
    val accessToken: String,
    @SerialName("refresh_token")
    val refreshToken: String,
    @SerialName("expires_in")
    val expiresIn: Int,
    @SerialName("token_type")
    val tokenType: String,
    @SerialName("user")
    val user: UserDto
)

/**
 * Supabase 사용자 정보 DTO
 */
@Serializable
data class SupabaseUserDto(
    @SerialName("id")
    val id: String,
    @SerialName("email")
    val email: String? = null,
    @SerialName("user_metadata")
    val userMetadata: UserMetadata? = null,
    @SerialName("app_metadata")
    val appMetadata: AppMetadata? = null,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("updated_at")
    val updatedAt: String? = null
)

/**
 * 사용자 메타데이터 (카카오, 구글에서 제공하는 정보)
 */
@Serializable
data class UserMetadata(
    @SerialName("name")
    val name: String? = null,
    @SerialName("picture")
    val picture: String? = null,
    @SerialName("provider_id")
    val providerId: String? = null,
    @SerialName("sub")
    val sub: String? = null
)

/**
 * 앱 메타데이터 (앱에서 관리하는 추가 정보)
 */
@Serializable
data class AppMetadata(
    @SerialName("provider")
    val provider: String? = null,
    @SerialName("providers")
    val providers: List<String>? = null
)

/**
 * 앱 전용 사용자 정보 DTO
 */
@Serializable
data class UserDto(
    @SerialName("id")
    val userId: String,
    @SerialName("user_ulid")
    val userUlid: String,
    @SerialName("oauth_platform")
    val oauthPlatform: String,
    @SerialName("openid")
    val openid: String,
    @SerialName("oauth_id")
    val oauthId: String?,
    @SerialName("oauth_token")
    val oauthToken: String?,
    @SerialName("created_at")
    val createdAt: String? = null,
    @SerialName("updated_at")
    val updatedAt: String? = null
)

/**
 * 유저 로그인 요청 DTO
 */
@Serializable
data class SupabaseUserLoginRequest(
    @SerialName("access_token")
    val accessToken: String,
)

/**
 * 토큰 갱신 요청 DTO
 */
@Serializable
data class SupabaseRefreshTokenRequest(
    @SerialName("refresh_token")
    val refreshToken: String
) {
    companion object {
        fun from(refreshToken: String): SupabaseRefreshTokenRequest {
            return SupabaseRefreshTokenRequest(refreshToken)
        }
    }
}

/**
 * 토큰 갱신 응답 DTO
 */
@Serializable
data class SupabaseRefreshTokenResponse(
    @SerialName("access_token")
    val accessToken: String,
)
/**
 * 프로필 업데이트 요청 DTO (앱 방식)
 */
@Serializable
data class SupabaseUserProfileUpdateRequest(
    @SerialName("user_name")
    val userName: String,
    @SerialName("phone")
    val phone: String? = null,
    @SerialName("is_helper")
    val isHelper: Boolean = false,
)

package com.khigh.seniormap.model.dto.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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
    @SerialName("is_registered")
    val isRegistered: Boolean = false,
    @SerialName("created_at")
    val createdAt: String? = null,
    @SerialName("updated_at")
    val updatedAt: String? = null
)
package com.khigh.seniormap.model.dto.supabaseauth

import com.khigh.seniormap.model.dto.auth.UserDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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
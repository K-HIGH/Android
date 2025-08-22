package com.khigh.seniormap.model.dto.supabaseauth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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
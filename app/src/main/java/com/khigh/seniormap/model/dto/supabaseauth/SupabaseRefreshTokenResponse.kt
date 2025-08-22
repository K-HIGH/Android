package com.khigh.seniormap.model.dto.supabaseauth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 토큰 갱신 응답 DTO
 */
@Serializable
data class SupabaseRefreshTokenResponse(
    @SerialName("access_token")
    val accessToken: String,
) 
package com.khigh.seniormap.model.dto.supabaseauth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 유저 로그인 요청 DTO
 */
@Serializable
data class SupabaseUserLoginRequest(
    @SerialName("access_token")
    val accessToken: String,
) 
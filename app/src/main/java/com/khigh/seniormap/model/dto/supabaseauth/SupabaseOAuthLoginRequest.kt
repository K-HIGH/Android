package com.khigh.seniormap.model.dto.supabaseauth

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
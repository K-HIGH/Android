package com.khigh.seniormap.model.dto.supabaseauth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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
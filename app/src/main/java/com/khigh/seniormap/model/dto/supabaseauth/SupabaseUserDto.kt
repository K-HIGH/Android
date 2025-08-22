package com.khigh.seniormap.model.dto.supabaseauth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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
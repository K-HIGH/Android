package com.khigh.seniormap.model.dto.supabaseauth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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
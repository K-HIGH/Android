package com.khigh.seniormap.model.dto.supabaseauth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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
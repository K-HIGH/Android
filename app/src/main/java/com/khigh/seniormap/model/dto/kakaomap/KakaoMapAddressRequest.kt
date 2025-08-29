package com.khigh.seniormap.model.dto.kakaomap

import kotlinx.serialization.Serializable

@Serializable
data class KakaoMapAddressRequest(
    val query: String,
    val analyze_type: String? = null,
    val page: Int? = null,
    val size: Int? = null
) 
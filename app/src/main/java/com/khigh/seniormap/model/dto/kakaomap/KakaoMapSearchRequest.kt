package com.khigh.seniormap.model.dto.kakaomap

import kotlinx.serialization.Serializable

@Serializable
data class KakaoMapSearchRequest(
    val query: String,
    val category_group_code: String? = null,
    val x: String? = null,
    val y: String? = null,
    val radius: Int? = null,
    val rect: String? = null,
    val page: Int? = null,
    val size: Int? = null,
    val sort: String? = null
) 
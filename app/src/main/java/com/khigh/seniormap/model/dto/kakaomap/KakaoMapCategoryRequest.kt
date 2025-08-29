package com.khigh.seniormap.model.dto.kakaomap

import kotlinx.serialization.Serializable

@Serializable
data class KakaoMapCategoryRequest(
    val category_group_code: String,
    val x: String,
    val y: String,
    val radius: Int? = null,
    val rect: String? = null,
    val page: Int? = null,
    val size: Int? = null,
    val sort: String? = null
) 
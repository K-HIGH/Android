package com.khigh.seniormap.model.dto.kakaomap

import kotlinx.serialization.Serializable

@Serializable
data class KakaoMapAddressResponse(
    val documents: List<KakaoMapAddressDocument>,
    val meta: KakaoMapAddressMeta
)

@Serializable
data class KakaoMapAddressDocument(
    val address: KakaoMapAddress?,
    val road_address: KakaoMapRoadAddress?,
    val place_name: String?,
    val distance: String?,
    val x: String,
    val y: String
)

@Serializable
data class KakaoMapAddress(
    val address_name: String,
    val region_1depth_name: String,
    val region_2depth_name: String,
    val region_3depth_name: String,
    val region_3depth_h_name: String,
    val h_code: String,
    val b_code: String,
    val mountain_yn: String,
    val main_address_no: String,
    val sub_address_no: String,
    val x: String,
    val y: String
)

@Serializable
data class KakaoMapRoadAddress(
    val address_name: String,
    val region_1depth_name: String,
    val region_2depth_name: String,
    val region_3depth_name: String,
    val road_name: String,
    val underground_yn: String,
    val main_building_no: String,
    val sub_building_no: String,
    val building_name: String,
    val zone_no: String,
    val x: String,
    val y: String
)

@Serializable
data class KakaoMapAddressMeta(
    val total_count: Int,
    val pageable_count: Int,
    val is_end: Boolean
) 
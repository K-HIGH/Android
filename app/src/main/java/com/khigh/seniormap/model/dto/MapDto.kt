package com.khigh.seniormap.model.dto

import com.google.gson.annotations.SerializedName

/**
 * 검색 상태 enum
 */
enum class SearchStatus {
    @SerializedName("idle")
    IDLE,
    @SerializedName("searching")
    SEARCHING,
    @SerializedName("success")
    SUCCESS,
    @SerializedName("error")
    ERROR
}

/**
 * 검색 결과 DTO
 */
data class SearchResultDto(
    @SerializedName("location")
    val location: String,
    @SerializedName("lat")
    val lat: Double,
    @SerializedName("lng")
    val lng: Double,
    @SerializedName("address")
    val address: String?,
    @SerializedName("road_address")
    val roadAddress: String?,
    @SerializedName("place_type")
    val placeType: String?
)

/**
 * 검색 제안 DTO
 */
data class SearchSuggestionDto(
    @SerializedName("text")
    val text: String,
    @SerializedName("type")
    val type: String // "place" | "keyword" | "category"
)

/**
 * 장소 정보 DTO
 */
data class PlaceInfoDto(
    @SerializedName("name")
    val name: String,
    @SerializedName("category")
    val category: String,
    @SerializedName("rating")
    val rating: Float,
    @SerializedName("address")
    val address: String,
    @SerializedName("road_address")
    val roadAddress: String?,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("hours")
    val hours: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("coordinates")
    val coordinates: CoordinatesDto,
    @SerializedName("website")
    val website: String?,
    @SerializedName("images")
    val images: List<String>?,
    @SerializedName("additional_info")
    val additionalInfo: Map<String, Any>?
)

/**
 * 좌표 DTO
 */
data class CoordinatesDto(
    @SerializedName("lat")
    val lat: Double,
    @SerializedName("lng")
    val lng: Double
)

/**
 * 장소 액션 DTO
 */
data class PlaceActionDto(
    @SerializedName("type")
    val type: String, // "call" | "navigate" | "share" | "bookmark" | "review"
    @SerializedName("label")
    val label: String,
    @SerializedName("icon")
    val icon: String?,
    @SerializedName("enabled")
    val enabled: Boolean
)

/**
 * 즐겨찾기 아이템 DTO
 */
data class FavoriteItemDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("from")
    val from: String,
    @SerializedName("to")
    val to: String,
    @SerializedName("created_at")
    val createdAt: Long,
    @SerializedName("last_used_at")
    val lastUsedAt: Long?,
    @SerializedName("usage_count")
    val usageCount: Int,
    @SerializedName("alias")
    val alias: String?,
    @SerializedName("metadata")
    val metadata: FavoriteMetadataDto?
)

/**
 * 즐겨찾기 메타데이터 DTO
 */
data class FavoriteMetadataDto(
    @SerializedName("from_coords")
    val fromCoords: CoordinatesDto?,
    @SerializedName("to_coords")
    val toCoords: CoordinatesDto?,
    @SerializedName("estimated_time")
    val estimatedTime: Int?,
    @SerializedName("distance")
    val distance: Int?,
    @SerializedName("preferred_transport")
    val preferredTransport: String? // "walk" | "car" | "public"
) 
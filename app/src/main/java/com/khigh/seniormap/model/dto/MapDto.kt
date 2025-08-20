package com.khigh.seniormap.model.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 검색 상태 enum
 */
@Serializable
enum class SearchStatus {
    @SerialName("idle")
    IDLE,
    @SerialName("searching")
    SEARCHING,
    @SerialName("success")
    SUCCESS,
    @SerialName("error")
    ERROR
}

/**
 * 검색 결과 DTO
 */
@Serializable
data class SearchResultDto(
    @SerialName("location")
    val location: String,
    @SerialName("lat")
    val lat: Double,
    @SerialName("lng")
    val lng: Double,
    @SerialName("address")
    val address: String?,
    @SerialName("road_address")
    val roadAddress: String?,
    @SerialName("place_type")
    val placeType: String?
)

/**
 * 검색 제안 DTO
 */
@Serializable
data class SearchSuggestionDto(
    @SerialName("text")
    val text: String,
    @SerialName("type")
    val type: String // "place" | "keyword" | "category"
)

/**
 * 장소 정보 DTO
 */
@Serializable
data class PlaceInfoDto(
    @SerialName("name")
    val name: String,
    @SerialName("category")
    val category: String,
    @SerialName("rating")
    val rating: Float,
    @SerialName("address")
    val address: String,
    @SerialName("road_address")
    val roadAddress: String?,
    @SerialName("phone")
    val phone: String,
    @SerialName("hours")
    val hours: String,
    @SerialName("description")
    val description: String,
    @SerialName("coordinates")
    val coordinates: CoordinatesDto,
    @SerialName("website")
    val website: String?,
    @SerialName("images")
    val images: List<String>?,
    @SerialName("additional_info")
    val additionalInfo: Map<String, kotlinx.serialization.json.JsonElement>?
)

/**
 * 좌표 DTO
 */
@Serializable
data class CoordinatesDto(
    @SerialName("lat")
    val lat: Double,
    @SerialName("lng")
    val lng: Double
)

/**
 * 장소 액션 DTO
 */
@Serializable
data class PlaceActionDto(
    @SerialName("type")
    val type: String, // "call" | "navigate" | "share" | "bookmark" | "review"
    @SerialName("label")
    val label: String,
    @SerialName("icon")
    val icon: String?,
    @SerialName("enabled")
    val enabled: Boolean
)

/**
 * 즐겨찾기 아이템 DTO
 */
@Serializable
data class FavoriteItemDto(
    @SerialName("id")
    val id: String,
    @SerialName("from")
    val from: String,
    @SerialName("to")
    val to: String,
    @SerialName("created_at")
    val createdAt: Long,
    @SerialName("last_used_at")
    val lastUsedAt: Long?,
    @SerialName("usage_count")
    val usageCount: Int,
    @SerialName("alias")
    val alias: String?,
    @SerialName("metadata")
    val metadata: FavoriteMetadataDto?
)

/**
 * 즐겨찾기 메타데이터 DTO
 */
@Serializable
data class FavoriteMetadataDto(
    @SerialName("from_coords")
    val fromCoords: CoordinatesDto?,
    @SerialName("to_coords")
    val toCoords: CoordinatesDto?,
    @SerialName("estimated_time")
    val estimatedTime: Int?,
    @SerialName("distance")
    val distance: Int?,
    @SerialName("preferred_transport")
    val preferredTransport: String? // "walk" | "car" | "public"
) 
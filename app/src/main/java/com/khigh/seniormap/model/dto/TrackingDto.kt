package com.khigh.seniormap.model.dto

import com.google.gson.annotations.SerializedName

/**
 * 위치 추적 업데이트 요청 DTO
 */
data class TrackUpdateRequest(
    @SerializedName("latitude")
    val latitude: Double,
    @SerializedName("longitude")
    val longitude: Double,
    @SerializedName("altitude")
    val altitude: Double,
    @SerializedName("speed")
    val speed: Float,
    @SerializedName("direction")
    val direction: Float
)

/**
 * 위치 추적 업데이트 응답 DTO
 */
data class TrackUpdateResponse(
    @SerializedName("detail")
    val detail: String
)

/**
 * 위치 데이터 DTO
 */
data class LocationDataDto(
    @SerializedName("latitude")
    val latitude: Double,
    @SerializedName("longitude")
    val longitude: Double,
    @SerializedName("altitude")
    val altitude: Double,
    @SerializedName("speed")
    val speed: Float,
    @SerializedName("direction")
    val direction: Float,
    @SerializedName("timestamp")
    val timestamp: Long,
    @SerializedName("accuracy")
    val accuracy: Float? = null
)

/**
 * 사용자 위치 조회 응답 DTO
 */
data class UserLocationResponse(
    @SerializedName("user_id")
    val userId: String,
    @SerializedName("location")
    val location: LocationDataDto,
    @SerializedName("last_updated")
    val lastUpdated: Long,
    @SerializedName("is_online")
    val isOnline: Boolean
)

/**
 * 위치 변경 이벤트 DTO
 */
data class LocationChangeEvent(
    @SerializedName("user_id")
    val userId: String,
    @SerializedName("location")
    val location: LocationDataDto,
    @SerializedName("previous_location")
    val previousLocation: LocationDataDto?,
    @SerializedName("distance_moved")
    val distanceMoved: Float?
) 
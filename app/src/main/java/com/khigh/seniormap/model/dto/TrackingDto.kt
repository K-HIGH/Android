package com.khigh.seniormap.model.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 위치 추적 업데이트 요청 DTO
 */
@Serializable
data class TrackUpdateRequest(
    @SerialName("latitude")
    val latitude: Double,
    @SerialName("longitude")
    val longitude: Double,
    @SerialName("altitude")
    val altitude: Double,
    @SerialName("speed")
    val speed: Float,
    @SerialName("direction")
    val direction: Float
)

/**
 * 위치 추적 업데이트 응답 DTO
 */
@Serializable
data class TrackUpdateResponse(
    @SerialName("detail")
    val detail: String
)

/**
 * 위치 데이터 DTO
 */
@Serializable
data class LocationDataDto(
    @SerialName("latitude")
    val latitude: Double,
    @SerialName("longitude")
    val longitude: Double,
    @SerialName("altitude")
    val altitude: Double,
    @SerialName("speed")
    val speed: Float,
    @SerialName("direction")
    val direction: Float,
    @SerialName("timestamp")
    val timestamp: Long,
    @SerialName("accuracy")
    val accuracy: Float? = null
)

/**
 * 사용자 위치 조회 응답 DTO
 */
@Serializable
data class UserLocationResponse(
    @SerialName("user_id")
    val userId: String,
    @SerialName("location")
    val location: LocationDataDto,
    @SerialName("last_updated")
    val lastUpdated: Long,
    @SerialName("is_online")
    val isOnline: Boolean
)

/**
 * 위치 변경 이벤트 DTO
 */
@Serializable
data class LocationChangeEvent(
    @SerialName("user_id")
    val userId: String,
    @SerialName("location")
    val location: LocationDataDto,
    @SerialName("previous_location")
    val previousLocation: LocationDataDto?,
    @SerialName("distance_moved")
    val distanceMoved: Float?
) 
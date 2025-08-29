package com.khigh.seniormap.model.dto.track

import kotlinx.serialization.Serializable

@Serializable
data class TrackUpdateReq(
    val latitude: Double,
    val longitude: Double,
    val altitude: Double,
    val speed: Double,
    val direction: Double
) 
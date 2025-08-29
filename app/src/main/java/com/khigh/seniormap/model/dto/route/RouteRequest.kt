package com.khigh.seniormap.model.dto.route

import kotlinx.serialization.Serializable

@Serializable
data class RouteRequest(
    val src: TMapCoordinate,
    val dst: TMapCoordinate,
    val count: Int? = null
)

@Serializable
data class TMapCoordinate(
    val lon: Double,
    val lat: Double
) 
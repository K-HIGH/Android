package com.khigh.seniormap.model.dto.route

import kotlinx.serialization.Serializable

@Serializable
data class RouteResponse(
    val route_id: Int,
    val user_id: Int,
    val src: RouteLocation,
    val dst: RouteLocation,
    val routes: List<RouteDetail>,
    val created_at: String
)

@Serializable
data class RouteLocation(
    val name: String? = null,
    val address: String? = null,
    val lon: Double? = null,
    val lat: Double? = null
)

@Serializable
data class RouteDetail(
    val name: String,
    val distance: Int,
    val duration: Int,
    val polyline: String
) 
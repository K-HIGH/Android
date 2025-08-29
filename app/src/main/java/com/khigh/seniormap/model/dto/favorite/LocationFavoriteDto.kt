package com.khigh.seniormap.model.dto.favorite

import kotlinx.serialization.Serializable

@Serializable
data class LocationFavoriteDto(
    val location_id: Int,
    val loc_name: String,
    val loc_coor: String,
    val loc_desc: String,
    val loc_info: Map<String, String>
) 
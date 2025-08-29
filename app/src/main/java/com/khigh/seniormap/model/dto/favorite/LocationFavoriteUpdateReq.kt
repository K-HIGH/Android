package com.khigh.seniormap.model.dto.favorite

import kotlinx.serialization.Serializable

@Serializable
data class LocationFavoriteUpdateReq(
    val loc_name: String? = null,
    val loc_coor: String? = null,
    val loc_desc: String? = null,
    val loc_info: Map<String, String>? = null
) 
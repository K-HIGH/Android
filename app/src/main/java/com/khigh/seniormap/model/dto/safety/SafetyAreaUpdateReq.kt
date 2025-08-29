package com.khigh.seniormap.model.dto.safety

import kotlinx.serialization.Serializable

@Serializable
data class SafetyAreaUpdateReq(
    val area_name: String? = null,
    val area_coor: String? = null,
    val area_desc: String? = null,
    val area_info: Map<String, String>? = null
) 
package com.khigh.seniormap.model.dto.safety

import kotlinx.serialization.Serializable

@Serializable
data class SafetyAreaCreateReq(
    val area_name: String,
    val area_coor: String,
    val area_desc: String,
    val area_info: Map<String, String>
) 
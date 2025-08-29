package com.khigh.seniormap.model.dto.safety

import kotlinx.serialization.Serializable

@Serializable
data class SafetyAreaDto(
    val safety_area_id: Int,
    val user_id: Int,
    val area_name: String,
    val area_coor: String,
    val area_desc: String,
    val area_info: Map<String, String>,
    val created_at: String,
    val updated_at: String
) 
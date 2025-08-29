package com.khigh.seniormap.model.dto.caretaker

import kotlinx.serialization.Serializable

@Serializable
data class CaretakerDto(
    val caretaker_id: Int,
    val user_id: Int,
    val caretaker_name: String,
    val caretaker_phone: String,
    val caretaker_email: String,
    val caretaker_relation: String,
    val created_at: String,
    val updated_at: String
) 
package com.khigh.seniormap.model.dto.caretaker

import kotlinx.serialization.Serializable

@Serializable
data class CaretakerCreateReq(
    val caretaker_name: String,
    val caretaker_phone: String,
    val caretaker_email: String,
    val caretaker_relation: String
) 
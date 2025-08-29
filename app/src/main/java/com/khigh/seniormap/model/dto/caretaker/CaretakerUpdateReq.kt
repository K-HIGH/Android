package com.khigh.seniormap.model.dto.caretaker

import kotlinx.serialization.Serializable

@Serializable
data class CaretakerUpdateReq(
    val caretaker_name: String? = null,
    val caretaker_phone: String? = null,
    val caretaker_email: String? = null,
    val caretaker_relation: String? = null
) 
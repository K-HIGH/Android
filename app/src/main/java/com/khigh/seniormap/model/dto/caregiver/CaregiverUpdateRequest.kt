package com.khigh.seniormap.model.dto.caregiver

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CaregiverUpdateRequest(
    @SerialName("relationship_type")
    val relationshipType: String? = null,
    @SerialName("description")
    val description: String? = null
)
package com.khigh.seniormap.model.dto.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserProfileDto(
    @SerialName("user_name")
    val userName: String,
    @SerialName("phone")
    val phone: String?,
    @SerialName("is_caregiver")
    val isCaregiver: Boolean,
    @SerialName("is_helper")
    val isHelper: Boolean,
)

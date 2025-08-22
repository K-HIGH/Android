package com.khigh.seniormap.model.dto.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserViewResponse(
    @SerialName("user_ulid")
    val userUlid: String,
    @SerialName("user_name")
    val userName: String,
    @SerialName("email")
    val email: String,
    @SerialName("phone")
    val phone: String? = null,
    @SerialName("address")
    val address: String? = null,
    @SerialName("emergency_contact")
    val emergencyContact: String? = null
)
package com.khigh.seniormap.model.dto.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class UserAlertDto(
    @SerialName("is_alert")
    val isAlert: Boolean,
    @SerialName("fcm_token")
    val fcmToken: String?
)
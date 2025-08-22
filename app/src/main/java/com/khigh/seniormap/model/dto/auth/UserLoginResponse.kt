package com.khigh.seniormap.model.dto.auth

import com.khigh.seniormap.model.entity.UserEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * K-HIGH 서버 사용자 정보 응답 DTO
 */
@Serializable
data class UserLoginResponse(
    @SerialName("user_name")
    val userName: String,
    @SerialName("phone")
    val phone: String?,
    @SerialName("is_caregiver")
    val isCaregiver: Boolean,
    @SerialName("is_helper")
    val isHelper: Boolean,
    @SerialName("fcm_token")
    val fcmToken: String?,
    @SerialName("is_alert")
    val isAlert: Boolean
)

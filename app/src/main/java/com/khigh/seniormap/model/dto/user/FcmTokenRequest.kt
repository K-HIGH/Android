package com.khigh.seniormap.model.dto.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * K-HIGH 서버 FCM 토큰 업데이트 요청 DTO
 */
@Serializable
data class FcmTokenRequest(
    @SerialName("fcm_token")
    val fcmToken: String
)
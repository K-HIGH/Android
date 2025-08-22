package com.khigh.seniormap.model.dto.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * K-HIGH 서버 알림 설정 업데이트 요청 DTO
 */
@Serializable
data class AlertFlagRequest(
    @SerialName("is_alert")
    val isAlert: Boolean
) 
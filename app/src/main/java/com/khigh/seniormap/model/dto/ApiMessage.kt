package com.khigh.seniormap.model.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * API 응답 메시지
 */
@Serializable
data class ApiMessage(
    @SerialName("message")
    val message: String
)
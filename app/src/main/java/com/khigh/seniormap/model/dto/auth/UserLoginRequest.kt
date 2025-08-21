package com.khigh.seniormap.model.dto.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * K-HIGH 서버 로그인 요청 DTO
 */
@Serializable
data class UserLoginRequest(
    @SerialName("access_token")
    val accessToken: String
) 
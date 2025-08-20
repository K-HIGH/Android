package com.khigh.seniormap.model.dto

import com.khigh.seniormap.model.entity.UserEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * K-HIGH 서버 API 관련 DTO 클래스들
 * 
 * Supabase OAuth 토큰을 사용하여 K-HIGH 서버와 통신할 때 사용하는
 * 요청/응답 데이터 클래스들을 정의합니다.
 */

// ==================== Auth API DTOs ====================

/**
 * K-HIGH 서버 로그인 요청 DTO
 */
@Serializable
data class KHighLoginRequest(
    @SerialName("access_token")
    val accessToken: String
)

// ==================== User API DTOs ====================

/**
 * K-HIGH 서버 사용자 정보 응답 DTO
 */
@Serializable
data class KHighUserResponse(
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

/**
 * K-HIGH 서버 사용자 프로필 업데이트 요청 DTO
 */
@Serializable
data class KHighUserProfileUpdateRequest(
    @SerialName("user_name")
    val userName: String,
    @SerialName("phone")
    val phone: String?,
    @SerialName("is_caregiver")
    val isCaregiver: Boolean,
    @SerialName("is_helper")
    val isHelper: Boolean
)

/**
 * K-HIGH 서버 FCM 토큰 업데이트 요청 DTO
 */
@Serializable
data class KHighFcmTokenRequest(
    @SerialName("fcm_token")
    val fcmToken: String
)

/**
 * K-HIGH 서버 알림 설정 업데이트 요청 DTO
 */
@Serializable
data class KHighAlertFlagRequest(
    @SerialName("is_alert")
    val isAlert: Boolean
)

// ==================== 확장 함수들 ====================

/**
 * KHighUserResponse를 UserEntity로 변환
 */
fun KHighUserResponse.toUserEntity(id: String, email: String): UserEntity {
    return UserEntity(
        id = id,
        email = email,
        userName = this.userName,
        phone = this.phone,
        isCaregiver = this.isCaregiver,
        isHelper = this.isHelper,
        fcmToken = this.fcmToken,
        isAlert = this.isAlert,
        accessToken = null,
        refreshToken = null
    )
}

/**
 * UserEntity를 KHighUserProfileUpdateRequest로 변환
 */
fun UserEntity.toKHighProfileUpdateRequest(): KHighUserProfileUpdateRequest {
    return KHighUserProfileUpdateRequest(
        userName = this.userName,
        phone = this.phone,
        isCaregiver = this.isCaregiver,
        isHelper = this.isHelper
    )
} 
package com.khigh.seniormap.model.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 알림 타입 enum
 */
@Serializable
enum class NotificationType {
    @SerialName("location_alert")
    LOCATION_ALERT,
    @SerialName("emergency")
    EMERGENCY,
    @SerialName("reminder")
    REMINDER,
    @SerialName("general")
    GENERAL
}

/**
 * FCM 토큰 등록 요청 DTO
 */
@Serializable
data class FCMTokenRequest(
    @SerialName("fcm_token")
    val fcmToken: String,
    @SerialName("device_type")
    val deviceType: String = "android"
)

/**
 * 알림 설정 DTO
 */
@Serializable
data class NotificationSettingsDto(
    @SerialName("enabled")
    val enabled: Boolean,
    @SerialName("location_alerts")
    val locationAlerts: Boolean,
    @SerialName("emergency_alerts")
    val emergencyAlerts: Boolean,
    @SerialName("reminders")
    val reminders: Boolean,
    @SerialName("sound_enabled")
    val soundEnabled: Boolean,
    @SerialName("vibration_enabled")
    val vibrationEnabled: Boolean
)

/**
 * 푸시 알림 데이터 DTO
 */
@Serializable
data class PushNotificationData(
    @SerialName("title")
    val title: String?,
    @SerialName("body")
    val body: String?,
    @SerialName("type")
    val type: NotificationType?,
    @SerialName("data")
    val data: Map<String, kotlinx.serialization.json.JsonElement>?,
    @SerialName("sound")
    val sound: Boolean = true,
    @SerialName("badge")
    val badge: Int? = null
)

/**
 * 위치 알림 데이터 DTO
 */
@Serializable
data class LocationAlertData(
    @SerialName("user_id")
    val userId: String,
    @SerialName("latitude")
    val latitude: Double,
    @SerialName("longitude")
    val longitude: Double,
    @SerialName("address")
    val address: String?,
    @SerialName("message")
    val message: String,
    @SerialName("timestamp")
    val timestamp: Long
)

/**
 * 응급 알림 데이터 DTO
 */
@Serializable
data class EmergencyAlertData(
    @SerialName("user_id")
    val userId: String,
    @SerialName("urgency")
    val urgency: String, // "low" | "medium" | "high"
    @SerialName("message")
    val message: String,
    @SerialName("location")
    val location: LocationAlertData?,
    @SerialName("timestamp")
    val timestamp: Long
) 
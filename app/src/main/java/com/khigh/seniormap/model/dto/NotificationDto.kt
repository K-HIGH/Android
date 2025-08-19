package com.khigh.seniormap.model.dto

import com.google.gson.annotations.SerializedName

/**
 * 알림 타입 enum
 */
enum class NotificationType {
    @SerializedName("location_alert")
    LOCATION_ALERT,
    @SerializedName("emergency")
    EMERGENCY,
    @SerializedName("reminder")
    REMINDER,
    @SerializedName("general")
    GENERAL
}

/**
 * FCM 토큰 등록 요청 DTO
 */
data class FCMTokenRequest(
    @SerializedName("fcm_token")
    val fcmToken: String,
    @SerializedName("device_type")
    val deviceType: String = "android"
)

/**
 * 알림 설정 DTO
 */
data class NotificationSettingsDto(
    @SerializedName("enabled")
    val enabled: Boolean,
    @SerializedName("location_alerts")
    val locationAlerts: Boolean,
    @SerializedName("emergency_alerts")
    val emergencyAlerts: Boolean,
    @SerializedName("reminders")
    val reminders: Boolean,
    @SerializedName("sound_enabled")
    val soundEnabled: Boolean,
    @SerializedName("vibration_enabled")
    val vibrationEnabled: Boolean
)

/**
 * 푸시 알림 데이터 DTO
 */
data class PushNotificationData(
    @SerializedName("title")
    val title: String?,
    @SerializedName("body")
    val body: String?,
    @SerializedName("type")
    val type: NotificationType?,
    @SerializedName("data")
    val data: Map<String, Any>?,
    @SerializedName("sound")
    val sound: Boolean = true,
    @SerializedName("badge")
    val badge: Int? = null
)

/**
 * 위치 알림 데이터 DTO
 */
data class LocationAlertData(
    @SerializedName("user_id")
    val userId: String,
    @SerializedName("latitude")
    val latitude: Double,
    @SerializedName("longitude")
    val longitude: Double,
    @SerializedName("address")
    val address: String?,
    @SerializedName("message")
    val message: String,
    @SerializedName("timestamp")
    val timestamp: Long
)

/**
 * 응급 알림 데이터 DTO
 */
data class EmergencyAlertData(
    @SerializedName("user_id")
    val userId: String,
    @SerializedName("urgency")
    val urgency: String, // "low" | "medium" | "high"
    @SerializedName("message")
    val message: String,
    @SerializedName("location")
    val location: LocationAlertData?,
    @SerializedName("timestamp")
    val timestamp: Long
) 
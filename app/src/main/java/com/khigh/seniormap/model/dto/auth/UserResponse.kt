package com.khigh.seniormap.model.dto.auth

import com.khigh.seniormap.model.entity.UserEntity
import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    val user: UserDto,
    val profile: UserProfileDto,
    val alert: UserAlertDto
)

fun UserResponse.toUserEntity(id: String, email: String, accessToken: String, refreshToken: String): UserEntity {
    return UserEntity(
        id = id,
        email = email,
        isRegistered = user.isRegistered,
        userName = profile.userName,
        phone = profile.phone,
        isCaregiver = profile.isCaregiver,
        isHelper = profile.isHelper,
        fcmToken = alert.fcmToken,
        isAlert = alert.isAlert,
        accessToken = accessToken,
        refreshToken = refreshToken,
        lastSyncTime = System.currentTimeMillis()
    )
}
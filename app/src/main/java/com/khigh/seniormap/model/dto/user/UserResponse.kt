package com.khigh.seniormap.model.dto.user

import com.khigh.seniormap.model.entity.UserEntity
import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    val user: UserDto,
    val profile: UserProfileDto,
    val alert: UserAlertDto
)

fun UserResponse.toEntity(): UserEntity {
    return UserEntity(
        id = user.userId,
        isRegistered = user.isRegistered,
        userName = profile.userName,
        phone = profile.phone,
        isCaregiver = profile.isCaregiver,
        isHelper = profile.isHelper,
        fcmToken = alert.fcmToken,
        isAlert = alert.isAlert,
        lastSyncTime = System.currentTimeMillis()
    )
}
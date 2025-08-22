package com.khigh.seniormap.model.dto.user

import com.khigh.seniormap.model.entity.UserEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    val user: UserDto,
    @SerialName("user_profile")
    val userProfile: UserProfileDto,
    @SerialName("user_alert")
    val userAlert: UserAlertDto
)

fun UserResponse.toEntity(): UserEntity {
    return UserEntity(
        id = user.userId,
        isRegistered = user.isRegistered,
        userName = userProfile.userName,
        phone = userProfile.phone,
        isCaregiver = userProfile.isCaregiver,
        isHelper = userProfile.isHelper,
        fcmToken = userAlert.fcmToken,
        isAlert = userAlert.isAlert,
        lastSyncTime = System.currentTimeMillis()
    )
}
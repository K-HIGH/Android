package com.khigh.seniormap.model.dto.auth

import com.khigh.seniormap.model.entity.UserEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * K-HIGH 서버 사용자 프로필 업데이트 요청 DTO
 */
@Serializable
data class UserProfileUpdateRequest(
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
 * UserEntity를 UserProfileUpdateRequest로 변환
 */
fun UserEntity.toUserProfileUpdateRequest(): UserProfileUpdateRequest {
    return UserProfileUpdateRequest(
        userName = this.userName,
        phone = this.phone,
        isCaregiver = this.isCaregiver,
        isHelper = this.isHelper
    )
} 
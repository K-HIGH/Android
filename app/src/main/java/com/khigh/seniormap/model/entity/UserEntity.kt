package com.khigh.seniormap.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

/**
 * 사용자 정보 Entity (Room Database)
 */
@Serializable
@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    val id: String,
    val isRegistered: Boolean,
    val userName: String,
    val phone: String?,
    val isCaregiver: Boolean,
    val isHelper: Boolean,
    val fcmToken: String?,
    val isAlert: Boolean,
    val lastSyncTime: Long = System.currentTimeMillis()
) 
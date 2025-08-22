package com.khigh.seniormap.ui.screens.guardian.components

import com.khigh.seniormap.model.dto.caregiver.CaregiverResponse

/**
 * 보호인 목록 데이터 클래스
 */
data class GuardianData(
    val userId: String,
    val userName: String,
    val location: String,
    val profileImageRes: Int? = null,
    val isAtHome: Boolean = true
)



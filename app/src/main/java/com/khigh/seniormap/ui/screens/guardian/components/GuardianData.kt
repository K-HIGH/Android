package com.khigh.seniormap.ui.screens.guardian.components

/**
 * 보호인 목록 데이터 클래스
 */
data class GuardianData(
    val id: String,
    val name: String,
    val location: String,
    val profileImageRes: Int? = null,
    val isAtHome: Boolean = true,
    // 위치 관련 필드 추가
    val homeAddress: String = "",
    val homeLatitude: Double = 0.0,
    val homeLongitude: Double = 0.0,
    val currentLatitude: Double? = null,
    val currentLongitude: Double? = null
)

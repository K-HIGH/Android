package com.khigh.seniormap.ui.screens.guardian.components

/**
 * 보호인 목록 데이터 클래스
 */
data class GuardianData(
    val id: String,
    val name: String,
    val location: String,
    val profileImageRes: Int? = null,
    val isAtHome: Boolean = true
)

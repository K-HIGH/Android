package com.khigh.seniormap.ui.model

import androidx.compose.ui.graphics.Color

/**
 * 부모님의 현재 상태를 나타내는 enum
 */
enum class PersonStatus(val displayName: String, val color: Color) {
    HOME("집", Color(0xFF4CAF50)),      // 초록색
    OUTING("외출", Color(0xFF2196F3)),  // 파란색
    PENDING("미승인", Color(0xFFF44336)), // 빨간색
    WORK("근무", Color(0xFFFF9800)),     // 주황색
    HOSPITAL("병원", Color(0xFF9C27B0))  // 보라색
}

/**
 * 부모님 정보를 담는 데이터 모델
 */
data class Person(
    val id: String,
    val name: String,
    val status: PersonStatus,
    val profileImageUrl: String? = null,
    val age: Int? = null,
    val relationship: String? = null
) 
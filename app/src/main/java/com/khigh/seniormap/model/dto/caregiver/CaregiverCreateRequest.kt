package com.khigh.seniormap.model.dto.caregiver

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 보호자 관계 생성 요청 모델
 */
@Serializable
data class CaregiverCreateRequest(
    @SerialName("target_email")
    val targetEmail: String
) {
    fun validate(): Boolean {
        require(targetEmail.isNotEmpty()) { "타겟 사용자 이메일은 비어있을 수 없습니다: $targetEmail" }
        return true
    }
} 
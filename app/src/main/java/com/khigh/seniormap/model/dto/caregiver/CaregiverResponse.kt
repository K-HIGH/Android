package com.khigh.seniormap.model.dto.caregiver

import com.khigh.seniormap.model.dto.user.UserViewResponse
import com.khigh.seniormap.ui.screens.guardian.components.GuardianData
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 보호자 관계 응답 모델
 * 
 * K-HIGH 백엔드 서버에서 반환하는 보호자 관계 정보를 담는 데이터 클래스입니다.
 * 보호자(케어기버)와 피보호자(케어 대상) 간의 관계를 나타냅니다.
 * 
 * @property caregiverId 보호자 관계 고유 식별자
 * @property userId 보호자(케어기버) 사용자 ID
 * @property targetId 피보호자(케어 대상) 사용자 ID  
 * @property createdAt 관계 생성 시간 (ISO 8601 형식)
 * @property updatedAt 관계 수정 시간 (ISO 8601 형식)
 * 
 * 사용 예시:
 * ```kotlin
 * val response = caregiverApi.getCaregivers()
 * if (response.isSuccessful) {
 *     val caregivers = response.body()
 *     caregivers?.forEach { caregiver ->
 *         println("보호자 관계 ID: ${caregiver.caregiverId}")
 *         println("보호자 ID: ${caregiver.userId}")
 *         println("피보호자 ID: ${caregiver.targetId}")
 *         println("생성일: ${caregiver.createdAt}")
 *     }
 * }
 * ```
 * 
 * JSON 응답 예시:
 * ```json
 * {
 *   "caregiver_id": 1,
 *   "user_id": 123,
 *   "target_id": 456,
 *   "created_at": "2024-01-01T10:00:00",
 *   "updated_at": "2024-01-01T10:00:00"
 * }
 * ```
 */
@Serializable
data class CaregiverResponse(
    @SerialName("caregiver_id")
    val caregiverId: Int,

    @SerialName("user_ulid")
    val userUlid: String,
    
    @SerialName("target_ulid")
    val targetUlid: String,
    
    /**
     * 관계 생성 시간
     * 
     * 보호자 관계가 최초로 생성된 시간을 나타냅니다.
     * ISO 8601 형식의 문자열로 제공됩니다.
     * 
     * 예시: "2024-01-01T10:00:00" 또는 "2024-01-01T10:00:00Z"
     */
    @SerialName("created_at")
    val createdAt: String,
    
    /**
     * 관계 수정 시간
     * 
     * 보호자 관계 정보가 마지막으로 수정된 시간을 나타냅니다.
     * ISO 8601 형식의 문자열로 제공됩니다.
     * 
     * 참고: 현재 API에서는 보호자 관계 수정 기능이 없으므로
     * 일반적으로 created_at과 동일한 값을 가집니다.
     */
    @SerialName("updated_at")
    val updatedAt: String,

    @SerialName("target")
    val target: UserViewResponse,
) {
    /**
     * 관계 정보를 읽기 쉬운 문자열로 변환
     * 
     * 디버깅이나 로깅 목적으로 사용할 수 있는 문자열 표현을 제공합니다.
     * 
     * @return 보호자 관계 정보가 포함된 문자열
     */
    override fun toString(): String {
        return "CaregiverResponse(id=$caregiverId, caregiver=$userUlid, target=$targetUlid, created=$createdAt)"
    }
    
    /**
     * 보호자 관계가 유효한지 검증
     * 
     * 기본적인 데이터 유효성을 검사합니다.
     * - ID 값들이 양수인지 확인
     * - 보호자와 피보호자가 동일하지 않은지 확인
     */
    fun isValid(): Boolean {
        return caregiverId > 0 && 
               userUlid.isNotEmpty() && 
               targetUlid.isNotEmpty() && 
               userUlid != targetUlid &&
               createdAt.isNotBlank() &&
               updatedAt.isNotBlank()
    }
}

fun CaregiverResponse.toGuardianData(): GuardianData {
    return GuardianData(
        userId = target.userUlid,
        userName = target.userName,
        location = "",
        profileImageRes = null,
        isAtHome = true
    )
}
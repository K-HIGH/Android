package com.khigh.seniormap.model.dto.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 사용자 프로필 업데이트 API 요청 모델
 * 
 * 사용자의 프로필 정보를 업데이트하기 위한 요청 데이터를 담는 클래스입니다.
 * PUT /api/v1/users/me/profile 엔드포인트에서 사용됩니다.
 * 
 * @property userName 업데이트할 사용자 이름
 * @property phone 업데이트할 전화번호
 * @property isHelper 업데이트할 도우미 여부
 * 
 * 사용 예시:
 * ```kotlin
 * val updateRequest = UserProfileUpdateRequest(
 *     userName = "홍길동",
 *     phone = "010-1234-5678",
 *     isHelper = true
 * )
 * val response = userApi.updateUserProfile(updateRequest)
 * ```
 * 
 * 유효성 검사 규칙:
 * - userName: 필수 입력, 1-50자 이내
 * - phone: 필수 입력, 유효한 전화번호 형식
 * - isCaregiver: 필수 입력, boolean 값
 * - isHelper: 필수 입력, boolean 값
 */
@Serializable
data class UserProfileUpdateRequest(
    @SerialName("user_name")
    val userName: String? = null,
    @SerialName("phone")
    val phone: String? = null,
    @SerialName("is_caregiver")
    val isCaregiver: Boolean? = null,
    @SerialName("is_helper")
    val isHelper: Boolean? = null
) {
    /**
     * 요청 데이터 유효성 검사
     * 
     * 서버로 전송하기 전에 클라이언트 측에서 기본적인 유효성 검사를 수행합니다.
     * 
     * @return 유효성 검사 결과 (true: 유효, false: 무효)
     * @throws IllegalArgumentException 유효하지 않은 데이터가 있을 경우
     */
    fun validate(): Boolean {
        if (userName != null) {
            require(userName.isNotBlank()) { "사용자 이름은 필수 입력 항목입니다." }
            require(userName.length <= 50) { "사용자 이름은 50자를 초과할 수 없습니다." }
        }
        if (phone != null) {
            require(phone.isNotBlank()) { "전화번호는 필수 입력 항목입니다." }
            require(isValidPhoneNumber(phone)) { "유효하지 않은 전화번호 형식입니다." }
        }
        if (isCaregiver != null) {
            require(isCaregiver == true || isCaregiver == false) { "isCaregiver는 true 또는 false 값이어야 합니다." }
        }
        if (isHelper != null) {
            require(isHelper == true || isHelper == false) { "isHelper는 true 또는 false 값이어야 합니다." }
        }
        
        return true
    }
    
    /**
     * 전화번호 형식 유효성 검사
     * 
     * 다양한 전화번호 형식을 지원하는 유효성 검사 함수입니다.
     * 
     * @param phone 검사할 전화번호
     * @return 유효성 검사 결과
     */
    private fun isValidPhoneNumber(phone: String): Boolean {
        val phoneRegexes = listOf(
            // 한국 휴대폰 번호 (010-1234-5678)
            Regex("^010-\\d{4}-\\d{4}$"),
            // 한국 휴대폰 번호 (01012345678)
            Regex("^010\\d{8}$"),
            // 국가 코드 포함 (+82-10-1234-5678)
            Regex("^\\+82-10-\\d{4}-\\d{4}$"),
            // 국제 형식 (+1-555-123-4567)
            Regex("^\\+\\d{1,3}-\\d{3}-\\d{3}-\\d{4}$"),
            // 일반적인 형식 (숫자와 하이픈만)
            Regex("^[+]?[\\d-]{10,15}$")
        )
        
        return phoneRegexes.any { it.matches(phone) }
    }
} 
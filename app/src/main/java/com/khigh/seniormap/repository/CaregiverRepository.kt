package com.khigh.seniormap.repository

import com.khigh.seniormap.model.dto.ApiMessage
import com.khigh.seniormap.model.dto.caregiver.CaregiverResponse
import com.khigh.seniormap.model.dto.caregiver.CaregiverCreateRequest
import com.khigh.seniormap.model.dto.caregiver.CaregiverUpdateRequest
import retrofit2.Response

/**
 * 보호자 관계 관리를 위한 Repository 인터페이스
 * 
 * @see CaregiverRepositoryImpl 실제 구현체
 * @see CaregiverResponse 보호자 관계 응답 모델
 * @see CreateCaregiverRequest 보호자 관계 생성 요청 모델
 */
interface CaregiverRepository {
    
    /**
     * 보호자 관계 목록 조회
     */
    suspend fun getCaregivers(): Result<Response<List<CaregiverResponse>>>
    
    /**
     * 보호자 관계 생성
     */
    suspend fun createCaregiver(request: CaregiverCreateRequest): Result<Response<CaregiverResponse>>
    
    /**
     * 보호자 관계 수정
     */
    suspend fun updateCaregiver(
        caregiverId: Int,
        request: CaregiverUpdateRequest
    ): Result<Response<CaregiverResponse>>

    /**
     * 보호자 관계 삭제
     */
    suspend fun deleteCaregiver(caregiverId: Int): Result<Response<ApiMessage>>
}

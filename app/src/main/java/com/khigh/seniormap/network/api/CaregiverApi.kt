package com.khigh.seniormap.network.api

import com.khigh.seniormap.model.dto.ApiMessage
import com.khigh.seniormap.model.dto.caregiver.*
import retrofit2.Response
import retrofit2.http.*

/**
 * K-HIGH 백엔드 서버의 Caregiver API 인터페이스
 * 
 * 보호자 관계 관리 기능을 제공합니다.
 * 모든 엔드포인트는 인증이 필요하며, Bearer 토큰을 사용합니다.
 * 
 * 주요 기능:
 * - 보호자 관계 목록 조회
 * - 보호자 관계 생성 (타겟 사용자와의 관계 설정)
 * - 보호자 관계 삭제
 * 
 * 보호자 관계는 다음과 같은 구조를 가집니다:
 * - caregiver_id: 보호자 관계 고유 ID
 * - user_id: 보호자(케어기버) 사용자 ID
 * - target_id: 피보호자(케어 대상) 사용자 ID
 * - created_at: 관계 생성 시간
 * - updated_at: 관계 수정 시간
 * 
 * @see CaregiverResponse 보호자 관계 응답 모델
 * @see CreateCaregiverRequest 보호자 관계 생성 요청 모델
 */
interface CaregiverApi {
    
    /**
     * 보호자 관계 목록 조회
     */
    @GET("api/v1/users/caregivers/")
    suspend fun getCaregivers(
        @Header("Authorization") token: String
    ): Response<List<CaregiverResponse>>
    
    /**
     * 보호자 관계 생성
     */
    @POST("api/v1/users/caregivers/")
    suspend fun createCaregiver(
        @Header("Authorization") token: String,
        @Body request: CaregiverCreateRequest
    ): Response<CaregiverResponse>
    
    /**
     * 보호자 관계 수정
     */
    @PUT("api/v1/users/caregivers/{caregiver_id}")
    suspend fun updateCaregiver(
        @Header("Authorization") token: String,
        @Path("caregiver_id") caregiverId: Int,
        @Body request: CaregiverUpdateRequest
    ): Response<CaregiverResponse>

    /**
     * 보호자 관계 삭제
    */
    @DELETE("api/v1/users/caregivers/{caregiver_id}")
    suspend fun deleteCaregiver(
        @Header("Authorization") token: String,
        @Path("caregiver_id") caregiverId: Int
    ): Response<ApiMessage>
}

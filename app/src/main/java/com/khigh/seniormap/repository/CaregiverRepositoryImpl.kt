package com.khigh.seniormap.repository

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.khigh.seniormap.model.dto.ApiMessage
import com.khigh.seniormap.model.dto.caregiver.CaregiverResponse
import com.khigh.seniormap.model.dto.caregiver.CaregiverCreateRequest
import com.khigh.seniormap.model.dto.caregiver.CaregiverUpdateRequest
import com.khigh.seniormap.network.api.CaregiverApi
import kotlinx.coroutines.flow.first
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

/**
 * CaregiverRepository의 실제 구현체
 * 
 * @param caregiverApi K-HIGH 백엔드의 Caregiver API 인터페이스
 * @param dataStore 토큰 저장을 위한 DataStore
 * 
 * @see CaregiverRepository 인터페이스
 * @see CaregiverApi API 인터페이스
 */
@Singleton
class CaregiverRepositoryImpl @Inject constructor(
    private val caregiverApi: CaregiverApi,
    private val dataStore: DataStore<Preferences>
) : CaregiverRepository {
    
    companion object {
        private const val TAG = "CaregiverRepository"
        private val SUPABASE_ACCESS_TOKEN = stringPreferencesKey("supabase_access_token")
    }
    
    /**
     * DataStore에서 Supabase 액세스 토큰 조회
     * 
     * @return 저장된 액세스 토큰 또는 null
     */
    private suspend fun getSupabaseAccessToken(): String? {
        return try {
            val preferences = dataStore.data.first()
            preferences[SUPABASE_ACCESS_TOKEN]
        } catch (e: Exception) {
            Log.e(TAG, "토큰 조회 실패", e)
            null
        }
    }
    
    /**
     * Bearer 토큰 헤더 생성
     * 
     * @param token 액세스 토큰
     * @return Bearer 형식의 인증 헤더
     */
    private fun createAuthHeader(token: String): String = "Bearer $token"
    
    /**
     * 보호자 관계 목록 조회
     */
    override suspend fun getCaregivers(): Result<Response<List<CaregiverResponse>>> {
        return try {
            Log.d(TAG, "getCaregivers: 보호자 관계 목록 조회 시작")
            
            val token = getSupabaseAccessToken()
            if (token == null) {
                Log.e(TAG, "getCaregivers: 액세스 토큰이 없습니다")
                return Result.failure(Exception("인증 토큰이 없습니다. 다시 로그인해주세요."))
            }
            
            val authHeader = createAuthHeader(token)
            Log.d(TAG, "getCaregivers: API 호출 시작")
            
            val response = caregiverApi.getCaregivers(authHeader)
            
            if (response.isSuccessful) {
                val caregivers = response.body()
                Log.d(TAG, "getCaregivers: 성공 - 보호자 관계 수: ${caregivers?.size}")
                caregivers?.forEach { caregiver ->
                    Log.d(TAG, "getCaregivers: 관계 ID ${caregiver.caregiverId} - 보호자: ${caregiver.userUlid}, 피보호자: ${caregiver.targetUlid}")
                }
            } else {
                Log.e(TAG, "getCaregivers: API 에러 - ${response.code()}: ${response.message()}")
            }
            
            Result.success(response)
            
        } catch (e: Exception) {
            Log.e(TAG, "getCaregivers: 예외 발생", e)
            Result.failure(e)
        }
    }
    
    /**
     * 보호자 관계 생성
     */
    override suspend fun createCaregiver(request: CaregiverCreateRequest): Result<Response<CaregiverResponse>> {
        return try {
            Log.d(TAG, "createCaregiver: 보호자 관계 생성 시작 - 타겟 이메일: ${request.targetEmail}")
            
            // 요청 데이터 유효성 검사
            if (!request.validate()) {
                Log.e(TAG, "createCaregiver: 유효하지 않은 요청 데이터")
                return Result.failure(IllegalArgumentException("유효하지 않은 요청 데이터입니다"))
            }
            
            val token = getSupabaseAccessToken()
            if (token == null) {
                Log.e(TAG, "createCaregiver: 액세스 토큰이 없습니다")
                return Result.failure(Exception("인증 토큰이 없습니다. 다시 로그인해주세요."))
            }
            
            val authHeader = createAuthHeader(token)
            Log.d(TAG, "createCaregiver: API 호출 시작")
            
            val response = caregiverApi.createCaregiver(authHeader, request)
            
            when (response.code()) {
                200 -> {
                    val caregiver = response.body()
                    Log.d(TAG, "createCaregiver: 성공 - 관계 ID: ${caregiver?.caregiverId}")
                    Result.success(response)
                }
                401 -> {
                    Log.w(TAG, "createCaregiver: 인증 실패 - ${response.message()}")
                    Result.failure(Exception("인증 실패: ${response.message()}"))
                }
                404 -> {
                    Log.w(TAG, "createCaregiver: 타겟 사용자를 찾을 수 없음 - 이메일: ${request.targetEmail}")
                    Result.failure(Exception("타겟 사용자를 찾을 수 없습니다"))
                }
                409 -> {
                    Log.w(TAG, "createCaregiver: 보호자 관계가 이미 존재함 - 타겟 이메일: ${request.targetEmail}")
                    Result.failure(Exception("보호자 관계가 이미 존재합니다"))
                }
                else -> {
                    Log.e(TAG, "createCaregiver: API 에러 - ${response.code()}: ${response.message()}")
                    Result.failure(Exception("API 에러: ${response.code()}: ${response.message()}"))
                }
            }
                        
        } catch (e: Exception) {
            Log.e(TAG, "createCaregiver: 예외 발생", e)
            Result.failure(e)
        }
    }
    
    /**
     * 보호자 관계 수정
     */
    override suspend fun updateCaregiver(caregiverId: Int, request: CaregiverUpdateRequest): Result<Response<CaregiverResponse>> {
        return try {
            Log.d(TAG, "updateCaregiver: 보호자 관계 수정 시작 - 관계 ID: $caregiverId")

            val token = getSupabaseAccessToken()
            if (token == null) {
                Log.e(TAG, "updateCaregiver: 액세스 토큰이 없습니다")
                return Result.failure(Exception("인증 토큰이 없습니다. 다시 로그인해주세요."))
            }
            
            val authHeader = createAuthHeader(token)
            Log.d(TAG, "updateCaregiver: API 호출 시작")
            
            val response = caregiverApi.updateCaregiver(authHeader, caregiverId, request)
            when (response.code()) {
                200 -> {
                    val caregiver = response.body()
                    Log.d(TAG, "updateCaregiver: 성공 - 관계 ID: ${caregiver?.caregiverId}")
                    Result.success(response)
                }
                401 -> {
                    Log.w(TAG, "updateCaregiver: 인증 실패 - ${response.message()}")
                    Result.failure(Exception("인증 실패: ${response.message()}"))
                }
                404 -> {
                    Log.w(TAG, "updateCaregiver: 보호자 관계를 찾을 수 없음 - ID: $caregiverId")
                    Result.failure(Exception("보호자 관계를 찾을 수 없습니다"))
                }
                else -> {
                    Log.e(TAG, "updateCaregiver: API 에러 - ${response.code()}: ${response.message()}")
                    Result.failure(Exception("API 에러: ${response.code()}: ${response.message()}"))
                }
            }

        } catch (e: Exception) {
            Log.e(TAG, "updateCaregiver: 예외 발생", e)
            Result.failure(e)
        }
    }

    /**
     * 보호자 관계 삭제
     */
    override suspend fun deleteCaregiver(caregiverId: Int): Result<Response<ApiMessage>> {
        return try {
            Log.d(TAG, "deleteCaregiver: 보호자 관계 삭제 시작 - 관계 ID: $caregiverId")
            
            // ID 유효성 검사
            if (caregiverId <= 0) {
                Log.e(TAG, "deleteCaregiver: 유효하지 않은 관계 ID: $caregiverId")
                return Result.failure(IllegalArgumentException("유효하지 않은 보호자 관계 ID입니다"))
            }
            
            val token = getSupabaseAccessToken()
            if (token == null) {
                Log.e(TAG, "deleteCaregiver: 액세스 토큰이 없습니다")
                return Result.failure(Exception("인증 토큰이 없습니다. 다시 로그인해주세요."))
            }
            
            val authHeader = createAuthHeader(token)
            Log.d(TAG, "deleteCaregiver: API 호출 시작")
            
            val response = caregiverApi.deleteCaregiver(authHeader, caregiverId)
            
            when (response.code()) {
                204 -> {
                    Log.d(TAG, "deleteCaregiver: 성공 (응답 본문 없음) - 관계 ID: $caregiverId 삭제됨")
                    Result.success(response)
                }
                401 -> {
                    Log.w(TAG, "deleteCaregiver: 인증 실패 - ${response.message()}")
                    Result.failure(Exception("인증 실패: ${response.message()}"))
                }
                404 -> {
                    Log.w(TAG, "deleteCaregiver: 보호자 관계를 찾을 수 없음 - ID: $caregiverId")
                    Result.failure(Exception("보호자 관계를 찾을 수 없습니다"))
                }
                else -> {
                    Log.e(TAG, "deleteCaregiver: API 에러 - ${response.code()}: ${response.message()}")
                    Result.failure(Exception("API 에러: ${response.code()}: ${response.message()}"))
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "deleteCaregiver: 예외 발생", e)
            Result.failure(e)
        }
    }
} 
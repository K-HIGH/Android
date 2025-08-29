package com.khigh.seniormap.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.khigh.seniormap.model.dto.safety.SafetyAreaCreateReq
import com.khigh.seniormap.model.dto.safety.SafetyAreaDto
import com.khigh.seniormap.model.dto.safety.SafetyAreaUpdateReq
import com.khigh.seniormap.network.api.SafetyAreaApi
import kotlinx.coroutines.flow.first
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

private val Context.safetyAreaDataStore: DataStore<Preferences> by preferencesDataStore(name = "safety_area_data")

@Singleton
class SafetyAreaRepositoryImpl @Inject constructor(
    private val safetyAreaApi: SafetyAreaApi,
    private val context: Context,
    private val dataStore: DataStore<Preferences>
) : SafetyAreaRepository {
    
    private val json = Json { ignoreUnknownKeys = true }
    
    companion object {
        private val SUPABASE_ACCESS_TOKEN_KEY = stringPreferencesKey("supabase_access_token")
        private val SAFETY_AREAS_DATA_KEY = stringPreferencesKey("safety_areas_data")
    }
    
    override suspend fun getSafetyAreas(): Result<List<SafetyAreaDto>> {
        return try {
            val token = getAuthToken()
            if (token.isEmpty()) {
                return Result.failure(Exception("인증 토큰이 없습니다."))
            }
            
            val response = safetyAreaApi.getSafetyAreas(token)
            if (response.isSuccessful) {
                response.body()?.let { safetyAreas ->
                    saveSafetyAreasToLocal(safetyAreas)
                    Result.success(safetyAreas)
                } ?: Result.failure(Exception("응답 데이터가 없습니다."))
            } else {
                Result.failure(Exception("API 호출 실패: ${response.code()}"))
            }
        } catch (e: Exception) {
            // 오프라인 데이터 반환
            val localSafetyAreas = getSafetyAreasFromLocal()
            if (localSafetyAreas.isNotEmpty()) {
                Result.success(localSafetyAreas)
            } else {
                Result.failure(e)
            }
        }
    }
    
    override suspend fun createSafetyArea(request: SafetyAreaCreateReq): Result<SafetyAreaDto> {
        return try {
            val token = getAuthToken()
            if (token.isEmpty()) {
                return Result.failure(Exception("인증 토큰이 없습니다."))
            }
            
            val response = safetyAreaApi.createSafetyArea(token, request)
            if (response.isSuccessful) {
                response.body()?.let { safetyArea ->
                    saveSafetyAreaToLocal(safetyArea)
                    Result.success(safetyArea)
                } ?: Result.failure(Exception("응답 데이터가 없습니다."))
            } else {
                Result.failure(Exception("API 호출 실패: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun updateSafetyArea(safetyAreaId: Int, request: SafetyAreaUpdateReq): Result<SafetyAreaDto> {
        return try {
            val token = getAuthToken()
            if (token.isEmpty()) {
                return Result.failure(Exception("인증 토큰이 없습니다."))
            }
            
            val response = safetyAreaApi.updateSafetyArea(token, safetyAreaId, request)
            if (response.isSuccessful) {
                response.body()?.let { safetyArea ->
                    saveSafetyAreaToLocal(safetyArea)
                    Result.success(safetyArea)
                } ?: Result.failure(Exception("응답 데이터가 없습니다."))
            } else {
                Result.failure(Exception("API 호출 실패: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun deleteSafetyArea(safetyAreaId: Int): Result<Unit> {
        return try {
            val token = getAuthToken()
            if (token.isEmpty()) {
                return Result.failure(Exception("인증 토큰이 없습니다."))
            }
            
            val response = safetyAreaApi.deleteSafetyArea(token, safetyAreaId)
            if (response.isSuccessful) {
                deleteSafetyAreaFromLocal(safetyAreaId)
                Result.success(Unit)
            } else {
                Result.failure(Exception("API 호출 실패: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun saveSafetyAreasToLocal(safetyAreas: List<SafetyAreaDto>) {
        try {
            val dataStore = context.safetyAreaDataStore
            val safetyAreasJson = json.encodeToString(safetyAreas)
            dataStore.edit { preferences ->
                preferences[SAFETY_AREAS_DATA_KEY] = safetyAreasJson
            }
        } catch (e: Exception) {
            // 로컬 저장 실패 로그
        }
    }
    
    override suspend fun getSafetyAreasFromLocal(): List<SafetyAreaDto> {
        return try {
            val dataStore = context.safetyAreaDataStore
            val preferences = dataStore.data.first()
            val safetyAreasJson = preferences[SAFETY_AREAS_DATA_KEY] ?: return emptyList()
            json.decodeFromString<List<SafetyAreaDto>>(safetyAreasJson)
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    override suspend fun saveSafetyAreaToLocal(safetyArea: SafetyAreaDto) {
        try {
            val currentSafetyAreas = getSafetyAreasFromLocal().toMutableList()
            val existingIndex = currentSafetyAreas.indexOfFirst { it.safety_area_id == safetyArea.safety_area_id }
            if (existingIndex != -1) {
                currentSafetyAreas[existingIndex] = safetyArea
            } else {
                currentSafetyAreas.add(safetyArea)
            }
            saveSafetyAreasToLocal(currentSafetyAreas)
        } catch (e: Exception) {
            // 로컬 저장 실패 로그
        }
    }
    
    override suspend fun deleteSafetyAreaFromLocal(safetyAreaId: Int) {
        try {
            val currentSafetyAreas = getSafetyAreasFromLocal().toMutableList()
            currentSafetyAreas.removeAll { it.safety_area_id == safetyAreaId }
            saveSafetyAreasToLocal(currentSafetyAreas)
        } catch (e: Exception) {
            // 로컬 삭제 실패 로그
        }
    }
    
    override suspend fun clearLocalSafetyAreas() {
        try {
            val dataStore = context.safetyAreaDataStore
            dataStore.edit { preferences ->
                preferences.remove(SAFETY_AREAS_DATA_KEY)
            }
        } catch (e: Exception) {
            // 로컬 삭제 실패 로그
        }
    }
    
    override suspend fun syncLocalSafetyAreasWithServer() {
        // 로컬 데이터를 서버와 동기화하는 로직
        // 구현 필요시 추가
    }
    
    override suspend fun getOfflineSafetyAreas(): List<SafetyAreaDto> {
        return getSafetyAreasFromLocal()
    }
    
    private suspend fun getAuthToken(): String {
        val preferences = dataStore.data.first()
        return preferences[SUPABASE_ACCESS_TOKEN_KEY] ?: ""
    }
} 
package com.khigh.seniormap.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.khigh.seniormap.model.dto.caretaker.CaretakerCreateReq
import com.khigh.seniormap.model.dto.caretaker.CaretakerDto
import com.khigh.seniormap.model.dto.caretaker.CaretakerUpdateReq
import com.khigh.seniormap.network.api.CaretakerApi
import kotlinx.coroutines.flow.first
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

private val Context.caretakerDataStore: DataStore<Preferences> by preferencesDataStore(name = "caretaker_data")

@Singleton
class CaretakerRepositoryImpl @Inject constructor(
    private val caretakerApi: CaretakerApi,
    private val context: Context,
    private val dataStore: DataStore<Preferences>
) : CaretakerRepository {
    
    private val json = Json { ignoreUnknownKeys = true }
    
    companion object {
        private val SUPABASE_ACCESS_TOKEN_KEY = stringPreferencesKey("supabase_access_token")
        private val CARETAKERS_DATA_KEY = stringPreferencesKey("caretakers_data")
    }
    
    override suspend fun getCaretakers(): Result<List<CaretakerDto>> {
        return try {
            val token = getAuthToken()
            if (token.isEmpty()) {
                return Result.failure(Exception("인증 토큰이 없습니다."))
            }
            
            val response = caretakerApi.getCaretakers(token)
            if (response.isSuccessful) {
                response.body()?.let { caretakers ->
                    saveCaretakersToLocal(caretakers)
                    Result.success(caretakers)
                } ?: Result.failure(Exception("응답 데이터가 없습니다."))
            } else {
                Result.failure(Exception("API 호출 실패: ${response.code()}"))
            }
        } catch (e: Exception) {
            // 오프라인 데이터 반환
            val localCaretakers = getCaretakersFromLocal()
            if (localCaretakers.isNotEmpty()) {
                Result.success(localCaretakers)
            } else {
                Result.failure(e)
            }
        }
    }
    
    override suspend fun createCaretaker(request: CaretakerCreateReq): Result<CaretakerDto> {
        return try {
            val token = getAuthToken()
            if (token.isEmpty()) {
                return Result.failure(Exception("인증 토큰이 없습니다."))
            }
            
            val response = caretakerApi.createCaretaker(token, request)
            if (response.isSuccessful) {
                response.body()?.let { caretaker ->
                    saveCaretakerToLocal(caretaker)
                    Result.success(caretaker)
                } ?: Result.failure(Exception("응답 데이터가 없습니다."))
            } else {
                Result.failure(Exception("API 호출 실패: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun updateCaretaker(caretakerId: Int, request: CaretakerUpdateReq): Result<CaretakerDto> {
        return try {
            val token = getAuthToken()
            if (token.isEmpty()) {
                return Result.failure(Exception("인증 토큰이 없습니다."))
            }
            
            val response = caretakerApi.updateCaretaker(token, caretakerId, request)
            if (response.isSuccessful) {
                response.body()?.let { caretaker ->
                    saveCaretakerToLocal(caretaker)
                    Result.success(caretaker)
                } ?: Result.failure(Exception("응답 데이터가 없습니다."))
            } else {
                Result.failure(Exception("API 호출 실패: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun deleteCaretaker(caretakerId: Int): Result<Unit> {
        return try {
            val token = getAuthToken()
            if (token.isEmpty()) {
                return Result.failure(Exception("인증 토큰이 없습니다."))
            }
            
            val response = caretakerApi.deleteCaretaker(token, caretakerId)
            if (response.isSuccessful) {
                deleteCaretakerFromLocal(caretakerId)
                Result.success(Unit)
            } else {
                Result.failure(Exception("API 호출 실패: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun saveCaretakersToLocal(caretakers: List<CaretakerDto>) {
        try {
            val dataStore = context.caretakerDataStore
            val caretakersJson = json.encodeToString(caretakers)
            dataStore.edit { preferences ->
                preferences[CARETAKERS_DATA_KEY] = caretakersJson
            }
        } catch (e: Exception) {
            // 로컬 저장 실패 로그
        }
    }
    
    override suspend fun getCaretakersFromLocal(): List<CaretakerDto> {
        return try {
            val dataStore = context.caretakerDataStore
            val preferences = dataStore.data.first()
            val caretakersJson = preferences[CARETAKERS_DATA_KEY] ?: return emptyList()
            json.decodeFromString<List<CaretakerDto>>(caretakersJson)
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    override suspend fun saveCaretakerToLocal(caretaker: CaretakerDto) {
        try {
            val currentCaretakers = getCaretakersFromLocal().toMutableList()
            val existingIndex = currentCaretakers.indexOfFirst { it.caretaker_id == caretaker.caretaker_id }
            if (existingIndex != -1) {
                currentCaretakers[existingIndex] = caretaker
            } else {
                currentCaretakers.add(caretaker)
            }
            saveCaretakersToLocal(currentCaretakers)
        } catch (e: Exception) {
            // 로컬 저장 실패 로그
        }
    }
    
    override suspend fun deleteCaretakerFromLocal(caretakerId: Int) {
        try {
            val currentCaretakers = getCaretakersFromLocal().toMutableList()
            currentCaretakers.removeAll { it.caretaker_id == caretakerId }
            saveCaretakersToLocal(currentCaretakers)
        } catch (e: Exception) {
            // 로컬 삭제 실패 로그
        }
    }
    
    override suspend fun clearLocalCaretakers() {
        try {
            val dataStore = context.caretakerDataStore
            dataStore.edit { preferences ->
                preferences.remove(CARETAKERS_DATA_KEY)
            }
        } catch (e: Exception) {
            // 로컬 삭제 실패 로그
        }
    }
    
    override suspend fun syncLocalCaretakersWithServer() {
        // 로컬 데이터를 서버와 동기화하는 로직
        // 구현 필요시 추가
    }
    
    override suspend fun getOfflineCaretakers(): List<CaretakerDto> {
        return getCaretakersFromLocal()
    }
    
    private suspend fun getAuthToken(): String {
        val preferences = dataStore.data.first()
        return preferences[SUPABASE_ACCESS_TOKEN_KEY] ?: ""
    }
} 
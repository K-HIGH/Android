package com.khigh.seniormap.repository

import com.khigh.seniormap.model.dto.route.RouteRequest
import com.khigh.seniormap.model.dto.route.RouteResponse
import com.khigh.seniormap.network.api.RouteApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import android.content.Context

/**
 * RouteRepository의 구현체
 * K-HIGH 백엔드 API를 통해 경로 탐색 기능을 제공
 * 
 * @see <a href="http://121.157.24.40:63001/docs">K-HIGH Backend API 문서</a>
 */
private val Context.routeDataStore: DataStore<Preferences> by preferencesDataStore(name = "route_data")

@Singleton
class RouteRepositoryImpl @Inject constructor(
    private val routeApi: RouteApi,
    private val dataStore: DataStore<Preferences>
) : RouteRepository {

    companion object {
        private val SUPABASE_ACCESS_TOKEN_KEY = stringPreferencesKey("supabase_access_token")
        private val ROUTE_DATA_KEY = stringPreferencesKey("route_data")
    }
    
    override suspend fun findRoute(request: RouteRequest): Result<RouteResponse> = 
        withContext(Dispatchers.IO) {
            val token = getAuthToken()
            if (token.isEmpty()) {
                return@withContext Result.failure(Exception("인증 토큰이 없습니다."))
            }
            try {
                val response = routeApi.findRoute(token, request)
                if (response.isSuccessful && response.body() != null) {
                    Result.success(response.body()!!)
                } else {
                    Result.failure(Exception("API 응답 오류: ${response.code()}"))
                }
            } catch (e: Exception) {
                Result.failure(Exception("경로 탐색 중 오류가 발생했습니다: ${e.message}"))
            }
        }
    
    override suspend fun getRouteHistory(userId: String): Result<List<RouteResponse>> = 
        withContext(Dispatchers.IO) {
            val token = getAuthToken()
            if (token.isEmpty()) {
                return@withContext Result.failure(Exception("인증 토큰이 없습니다."))
            }
            try {
                val response = routeApi.getRouteHistory(token)
                if (response.isSuccessful && response.body() != null) {
                    Result.success(response.body()!!)
                } else {
                    Result.failure(Exception("API 응답 오류: ${response.code()}"))
                }
            } catch (e: Exception) {
                Result.failure(Exception("경로 히스토리 조회 중 오류가 발생했습니다: ${e.message}"))
            }
        }
    
    override suspend fun getRouteHistoryById(routeId: String): Result<RouteResponse> = 
        withContext(Dispatchers.IO) {
            val token = getAuthToken()
            if (token.isEmpty()) {
                return@withContext Result.failure(Exception("인증 토큰이 없습니다."))
            }
            try {
                val response = routeApi.getRouteHistoryById(token, routeId)
                if (response.isSuccessful && response.body() != null) {
                    Result.success(response.body()!!)
                } else {
                    Result.failure(Exception("API 응답 오류: ${response.code()}"))
                }
            } catch (e: Exception) {
                Result.failure(Exception("경로 상세 정보 조회 중 오류가 발생했습니다: ${e.message}"))
            }
        }
    
    override suspend fun deleteRouteHistory(routeId: String): Result<Boolean> = 
        withContext(Dispatchers.IO) {
            val token = getAuthToken()
            if (token.isEmpty()) {
                return@withContext Result.failure(Exception("인증 토큰이 없습니다."))
            }
            try {
                val response = routeApi.deleteRouteHistory(token, routeId)
                if (response.isSuccessful) {
                    Result.success(true)
                } else {
                    Result.failure(Exception("API 응답 오류: ${response.code()}"))
                }
            } catch (e: Exception) {
                Result.failure(Exception("경로 히스토리 삭제 중 오류가 발생했습니다: ${e.message}"))
            }
        }
    
    private suspend fun getRouteData(): String {
        val preferences = dataStore.data.first()
        return preferences[ROUTE_DATA_KEY] ?: ""
    }

    private suspend fun saveRouteData(routeData: String) {
        dataStore.edit { preferences ->
            preferences[ROUTE_DATA_KEY] = routeData
        }
    }

    private suspend fun deleteRouteData() {
        dataStore.edit { preferences ->
            preferences.remove(ROUTE_DATA_KEY)
        }
    }

    private suspend fun getAuthToken(): String {
        val preferences = dataStore.data.first()
        return preferences[SUPABASE_ACCESS_TOKEN_KEY] ?: ""
    }
} 
package com.khigh.seniormap.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.khigh.seniormap.model.dto.kakaomap.KakaoMapAddressResponse
import com.khigh.seniormap.model.dto.kakaomap.KakaoMapSearchResponse
import com.khigh.seniormap.network.api.KakaoMapApi
import kotlinx.coroutines.flow.first
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

private val Context.kakaoMapDataStore: DataStore<Preferences> by preferencesDataStore(name = "kakao_map_data")

@Singleton
class KakaoMapRepositoryImpl @Inject constructor(
    private val kakaoMapApi: KakaoMapApi,
    private val context: Context
) : KakaoMapRepository {
    
    private val json = Json { ignoreUnknownKeys = true }
    
    companion object {
        private val KAKAO_MAP_API_KEY = stringPreferencesKey("kakao_map_api_key")
        private val SEARCH_RESULTS_PREFIX = "search_results_"
        private val CACHE_TIMESTAMP_PREFIX = "cache_timestamp_"
        private const val CACHE_EXPIRY_HOURS = 24L
    }
    
    override suspend fun searchPlaces(
        query: String,
        categoryGroupCode: String?,
        x: String?,
        y: String?,
        radius: Int?,
        rect: String?,
        page: Int?,
        size: Int?,
        sort: String?
    ): Result<KakaoMapSearchResponse> {
        return try {
            val apiKey = getApiKey()
            if (apiKey.isEmpty()) {
                return Result.failure(Exception("카카오맵 API 키가 설정되지 않았습니다."))
            }
            
            val response = kakaoMapApi.searchPlaces(
                apiKey = apiKey,
                query = query,
                categoryGroupCode = categoryGroupCode,
                x = x,
                y = y,
                radius = radius,
                rect = rect,
                page = page,
                size = size,
                sort = sort
            )
            
            if (response.isSuccessful) {
                response.body()?.let { searchResponse ->
                    saveSearchResultsToLocal(query, searchResponse)
                    Result.success(searchResponse)
                } ?: Result.failure(Exception("응답 데이터가 없습니다."))
            } else {
                // API 호출 실패 시 로컬 캐시 확인
                getSearchResultsFromLocal(query)?.let { cachedResults ->
                    Result.success(cachedResults)
                } ?: Result.failure(Exception("API 호출 실패: ${response.code()}"))
            }
        } catch (e: Exception) {
            // 네트워크 오류 시 로컬 캐시 확인
            getSearchResultsFromLocal(query)?.let { cachedResults ->
                Result.success(cachedResults)
            } ?: Result.failure(e)
        }
    }
    
    override suspend fun searchAddresses(
        query: String,
        analyzeType: String?,
        page: Int?,
        size: Int?
    ): Result<KakaoMapAddressResponse> {
        return try {
            val apiKey = getApiKey()
            if (apiKey.isEmpty()) {
                return Result.failure(Exception("카카오맵 API 키가 설정되지 않았습니다."))
            }
            
            val response = kakaoMapApi.searchAddresses(
                apiKey = apiKey,
                query = query,
                analyzeType = analyzeType,
                page = page,
                size = size
            )
            
            if (response.isSuccessful) {
                response.body()?.let { addressResponse ->
                    Result.success(addressResponse)
                } ?: Result.failure(Exception("응답 데이터가 없습니다."))
            } else {
                Result.failure(Exception("API 호출 실패: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun searchByCategory(
        categoryGroupCode: String,
        x: String,
        y: String,
        radius: Int?,
        rect: String?,
        page: Int?,
        size: Int?,
        sort: String?
    ): Result<KakaoMapSearchResponse> {
        return try {
            val apiKey = getApiKey()
            if (apiKey.isEmpty()) {
                return Result.failure(Exception("카카오맵 API 키가 설정되지 않았습니다."))
            }
            
            val response = kakaoMapApi.searchByCategory(
                apiKey = apiKey,
                categoryGroupCode = categoryGroupCode,
                x = x,
                y = y,
                radius = radius,
                rect = rect,
                page = page,
                size = size,
                sort = sort
            )
            
            if (response.isSuccessful) {
                response.body()?.let { searchResponse ->
                    val cacheKey = "category_${categoryGroupCode}_${x}_${y}"
                    saveSearchResultsToLocal(cacheKey, searchResponse)
                    Result.success(searchResponse)
                } ?: Result.failure(Exception("응답 데이터가 없습니다."))
            } else {
                Result.failure(Exception("API 호출 실패: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getAddressFromCoordinates(
        x: String,
        y: String,
        inputCoord: String
    ): Result<KakaoMapAddressResponse> {
        return try {
            val apiKey = getApiKey()
            if (apiKey.isEmpty()) {
                return Result.failure(Exception("카카오맵 API 키가 설정되지 않았습니다."))
            }
            
            val response = kakaoMapApi.getAddressFromCoordinates(
                apiKey = apiKey,
                x = x,
                y = y,
                inputCoord = inputCoord
            )
            
            if (response.isSuccessful) {
                response.body()?.let { addressResponse ->
                    Result.success(addressResponse)
                } ?: Result.failure(Exception("응답 데이터가 없습니다."))
            } else {
                Result.failure(Exception("API 호출 실패: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getCoordinatesFromAddress(
        x: String,
        y: String,
        inputCoord: String
    ): Result<KakaoMapAddressResponse> {
        return try {
            val apiKey = getApiKey()
            if (apiKey.isEmpty()) {
                return Result.failure(Exception("카카오맵 API 키가 설정되지 않았습니다."))
            }
            
            val response = kakaoMapApi.getCoordinatesFromAddress(
                apiKey = apiKey,
                x = x,
                y = y,
                inputCoord = inputCoord
            )
            
            if (response.isSuccessful) {
                response.body()?.let { addressResponse ->
                    Result.success(addressResponse)
                } ?: Result.failure(Exception("응답 데이터가 없습니다."))
            } else {
                Result.failure(Exception("API 호출 실패: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun saveSearchResultsToLocal(query: String, results: KakaoMapSearchResponse) {
        try {
            val dataStore = context.kakaoMapDataStore
            val resultsJson = json.encodeToString(results)
            val timestamp = System.currentTimeMillis()
            
            dataStore.edit { preferences ->
                preferences[stringPreferencesKey("${SEARCH_RESULTS_PREFIX}${query}")] = resultsJson
                preferences[longPreferencesKey("${CACHE_TIMESTAMP_PREFIX}${query}")] = timestamp
            }
        } catch (e: Exception) {
            // 로컬 저장 실패 로그
        }
    }
    
    override suspend fun getSearchResultsFromLocal(query: String): KakaoMapSearchResponse? {
        return try {
            val dataStore = context.kakaoMapDataStore
            val preferences = dataStore.data.first()
            
            val resultsJson = preferences[stringPreferencesKey("${SEARCH_RESULTS_PREFIX}${query}")]
            val timestamp = preferences[longPreferencesKey("${CACHE_TIMESTAMP_PREFIX}${query}")]
            
            if (resultsJson != null && timestamp != null) {
                // 캐시 만료 확인
                val currentTime = System.currentTimeMillis()
                val cacheAge = currentTime - timestamp
                val expiryTime = TimeUnit.HOURS.toMillis(CACHE_EXPIRY_HOURS)
                
                if (cacheAge < expiryTime) {
                    json.decodeFromString<KakaoMapSearchResponse>(resultsJson)
                } else {
                    // 만료된 캐시 삭제
                    dataStore.edit { prefs ->
                        prefs.remove(stringPreferencesKey("${SEARCH_RESULTS_PREFIX}${query}"))
                        prefs.remove(longPreferencesKey("${CACHE_TIMESTAMP_PREFIX}${query}"))
                    }
                    null
                }
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
    
    override suspend fun clearLocalSearchResults() {
        try {
            val dataStore = context.kakaoMapDataStore
            dataStore.edit { preferences ->
                preferences.clear()
            }
        } catch (e: Exception) {
            // 로컬 삭제 실패 로그
        }
    }
    
    override suspend fun clearExpiredCache() {
        try {
            val dataStore = context.kakaoMapDataStore
            val preferences = dataStore.data.first()
            val currentTime = System.currentTimeMillis()
            val expiryTime = TimeUnit.HOURS.toMillis(CACHE_EXPIRY_HOURS)
            
            val expiredKeys = mutableListOf<String>()
            
            preferences.asMap().forEach { (key, value) ->
                if (key.name.startsWith(CACHE_TIMESTAMP_PREFIX)) {
                    val timestamp = value as Long
                    if (currentTime - timestamp > expiryTime) {
                        val query = key.name.removePrefix(CACHE_TIMESTAMP_PREFIX)
                        expiredKeys.add(query)
                    }
                }
            }
            
            // 만료된 캐시 삭제
            dataStore.edit { prefs ->
                expiredKeys.forEach { query ->
                    prefs.remove(stringPreferencesKey("${SEARCH_RESULTS_PREFIX}${query}"))
                    prefs.remove(longPreferencesKey("${CACHE_TIMESTAMP_PREFIX}${query}"))
                }
            }
        } catch (e: Exception) {
            // 캐시 정리 실패 로그
        }
    }
    
    override suspend fun getCacheSize(): Long {
        return try {
            val dataStore = context.kakaoMapDataStore
            val preferences = dataStore.data.first()
            preferences.asMap().size.toLong()
        } catch (e: Exception) {
            0L
        }
    }
    
    private suspend fun getApiKey(): String {
        val dataStore = context.kakaoMapDataStore
        val preferences = dataStore.data.first()
        return preferences[KAKAO_MAP_API_KEY] ?: ""
    }
    
    /**
     * API 키 설정 (앱 초기화 시 호출)
     */
    suspend fun setApiKey(apiKey: String) {
        try {
            val dataStore = context.kakaoMapDataStore
            dataStore.edit { preferences ->
                preferences[KAKAO_MAP_API_KEY] = apiKey
            }
        } catch (e: Exception) {
            // API 키 설정 실패 로그
        }
    }
} 
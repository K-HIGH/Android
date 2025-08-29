package com.khigh.seniormap.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.khigh.seniormap.model.dto.favorite.LocationFavoriteCreateReq
import com.khigh.seniormap.model.dto.favorite.LocationFavoriteDto
import com.khigh.seniormap.model.dto.favorite.LocationFavoriteUpdateReq
import com.khigh.seniormap.network.api.FavoriteApi
import kotlinx.coroutines.flow.first
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

private val Context.favoriteDataStore: DataStore<Preferences> by preferencesDataStore(name = "favorite_data")

@Singleton
class FavoriteRepositoryImpl @Inject constructor(
    private val favoriteApi: FavoriteApi,
    private val context: Context,
    private val dataStore: DataStore<Preferences>
) : FavoriteRepository {
    
    private val json = Json { ignoreUnknownKeys = true }
    
    companion object {
        private val SUPABASE_ACCESS_TOKEN_KEY = stringPreferencesKey("supabase_access_token")
        private val FAVORITES_DATA_KEY = stringPreferencesKey("favorites_data")
    }
    
    override suspend fun getLocationFavorites(): Result<List<LocationFavoriteDto>> {
        return try {
            val token = getAuthToken()
            if (token.isEmpty()) {
                return Result.failure(Exception("인증 토큰이 없습니다."))
            }
            
            val response = favoriteApi.getLocationFavorites(token)
            if (response.isSuccessful) {
                response.body()?.let { favorites ->
                    saveFavoritesToLocal(favorites)
                    Result.success(favorites)
                } ?: Result.failure(Exception("응답 데이터가 없습니다."))
            } else {
                Result.failure(Exception("API 호출 실패: ${response.code()}"))
            }
        } catch (e: Exception) {
            // 오프라인 데이터 반환
            val localFavorites = getFavoritesFromLocal()
            if (localFavorites.isNotEmpty()) {
                Result.success(localFavorites)
            } else {
                Result.failure(e)
            }
        }
    }
    
    override suspend fun createLocationFavorite(request: LocationFavoriteCreateReq): Result<LocationFavoriteDto> {
        return try {
            val token = getAuthToken()
            if (token.isEmpty()) {
                return Result.failure(Exception("인증 토큰이 없습니다."))
            }
            
            val response = favoriteApi.createLocationFavorite(token, request)
            if (response.isSuccessful) {
                response.body()?.let { favorite ->
                    saveFavoriteToLocal(favorite)
                    Result.success(favorite)
                } ?: Result.failure(Exception("응답 데이터가 없습니다."))
            } else {
                Result.failure(Exception("API 호출 실패: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun updateLocationFavorite(locationId: Int, request: LocationFavoriteUpdateReq): Result<LocationFavoriteDto> {
        return try {
            val token = getAuthToken()
            if (token.isEmpty()) {
                return Result.failure(Exception("인증 토큰이 없습니다."))
            }
            
            val response = favoriteApi.updateLocationFavorite(token, locationId, request)
            if (response.isSuccessful) {
                response.body()?.let { favorite ->
                    saveFavoriteToLocal(favorite)
                    Result.success(favorite)
                } ?: Result.failure(Exception("응답 데이터가 없습니다."))
            } else {
                Result.failure(Exception("API 호출 실패: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun deleteLocationFavorite(locationId: Int): Result<Unit> {
        return try {
            val token = getAuthToken()
            if (token.isEmpty()) {
                return Result.failure(Exception("인증 토큰이 없습니다."))
            }
            
            val response = favoriteApi.deleteLocationFavorite(token, locationId)
            if (response.isSuccessful) {
                deleteFavoriteFromLocal(locationId)
                Result.success(Unit)
            } else {
                Result.failure(Exception("API 호출 실패: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun saveFavoritesToLocal(favorites: List<LocationFavoriteDto>) {
        try {
            val dataStore = context.favoriteDataStore
            val favoritesJson = json.encodeToString(favorites)
            dataStore.edit { preferences ->
                preferences[FAVORITES_DATA_KEY] = favoritesJson
            }
        } catch (e: Exception) {
            // 로컬 저장 실패 로그
        }
    }
    
    override suspend fun getFavoritesFromLocal(): List<LocationFavoriteDto> {
        return try {
            val dataStore = context.favoriteDataStore
            val preferences = dataStore.data.first()
            val favoritesJson = preferences[FAVORITES_DATA_KEY] ?: return emptyList()
            json.decodeFromString<List<LocationFavoriteDto>>(favoritesJson)
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    override suspend fun saveFavoriteToLocal(favorite: LocationFavoriteDto) {
        try {
            val currentFavorites = getFavoritesFromLocal().toMutableList()
            val existingIndex = currentFavorites.indexOfFirst { it.location_id == favorite.location_id }
            if (existingIndex != -1) {
                currentFavorites[existingIndex] = favorite
            } else {
                currentFavorites.add(favorite)
            }
            saveFavoritesToLocal(currentFavorites)
        } catch (e: Exception) {
            // 로컬 저장 실패 로그
        }
    }
    
    override suspend fun deleteFavoriteFromLocal(locationId: Int) {
        try {
            val currentFavorites = getFavoritesFromLocal().toMutableList()
            currentFavorites.removeAll { it.location_id == locationId }
            saveFavoritesToLocal(currentFavorites)
        } catch (e: Exception) {
            // 로컬 삭제 실패 로그
        }
    }
    
    override suspend fun clearLocalFavorites() {
        try {
            val dataStore = context.favoriteDataStore
            dataStore.edit { preferences ->
                preferences.remove(FAVORITES_DATA_KEY)
            }
        } catch (e: Exception) {
            // 로컬 삭제 실패 로그
        }
    }
    
    override suspend fun syncLocalFavoritesWithServer() {
        // 로컬 데이터를 서버와 동기화하는 로직
        // 구현 필요시 추가
    }
    
    override suspend fun getOfflineFavorites(): List<LocationFavoriteDto> {
        return getFavoritesFromLocal()
    }
    
    private suspend fun getAuthToken(): String {
        val preferences = dataStore.data.first()
        return preferences[SUPABASE_ACCESS_TOKEN_KEY] ?: ""
    }
} 
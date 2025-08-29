package com.khigh.seniormap.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.khigh.seniormap.model.dto.track.TrackDto
import com.khigh.seniormap.model.dto.track.TrackUpdateReq
import com.khigh.seniormap.network.api.TrackApi
import kotlinx.coroutines.flow.first
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

private val Context.trackDataStore: DataStore<Preferences> by preferencesDataStore(name = "track_data")

@Singleton
class TrackRepositoryImpl @Inject constructor(
    private val trackApi: TrackApi,
    private val context: Context,
    private val dataStore: DataStore<Preferences>
) : TrackRepository {
    
    private val json = Json { ignoreUnknownKeys = true }
    
    companion object {
        private val SUPABASE_ACCESS_TOKEN_KEY = stringPreferencesKey("supabase_access_token")
        private val TRACK_DATA_KEY = stringPreferencesKey("track_data")
    }
    
    override suspend fun getTrack(userUlid: String): Result<TrackDto> {
        return try {
            val token = getAuthToken()
            if (token.isEmpty()) {
                return Result.failure(Exception("인증 토큰이 없습니다."))
            }
            
            val response = trackApi.getTrack(token, userUlid)
            if (response.isSuccessful) {
                response.body()?.let { track ->
                    saveTrackToLocal(track)
                    Result.success(track)
                } ?: Result.failure(Exception("응답 데이터가 없습니다."))
            } else {
                Result.failure(Exception("API 호출 실패: ${response.code()}"))
            }
        } catch (e: Exception) {
            // 오프라인 데이터 반환
            getTrackFromLocal()?.let { track ->
                Result.success(track)
            } ?: Result.failure(e)
        }
    }
    
    override suspend fun updateTrack(request: TrackUpdateReq): Result<TrackDto> {
        return try {
            val token = getAuthToken()
            if (token.isEmpty()) {
                return Result.failure(Exception("인증 토큰이 없습니다."))
            }
            
            val response = trackApi.updateTrack(token, request)
            if (response.isSuccessful) {
                response.body()?.let { track ->
                    saveTrackToLocal(track)
                    Result.success(track)
                } ?: Result.failure(Exception("응답 데이터가 없습니다."))
            } else {
                Result.failure(Exception("API 호출 실패: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun saveTrackToLocal(track: TrackDto) {
        try {
            val dataStore = context.trackDataStore
            val trackJson = json.encodeToString(track)
            dataStore.edit { preferences ->
                preferences[TRACK_DATA_KEY] = trackJson
            }
        } catch (e: Exception) {
            // 로컬 저장 실패 로그
        }
    }
    
    override suspend fun getTrackFromLocal(): TrackDto? {
        return try {
            val dataStore = context.trackDataStore
            val preferences = dataStore.data.first()
            val trackJson = preferences[TRACK_DATA_KEY] ?: return null
            json.decodeFromString<TrackDto>(trackJson)
        } catch (e: Exception) {
            null
        }
    }
    
    override suspend fun clearLocalTrack() {
        try {
            val dataStore = context.trackDataStore
            dataStore.edit { preferences ->
                preferences.remove(TRACK_DATA_KEY)
            }
        } catch (e: Exception) {
            // 로컬 삭제 실패 로그
        }
    }
    
    override suspend fun syncLocalTrackWithServer() {
        // 로컬 데이터를 서버와 동기화하는 로직
        // 구현 필요시 추가
    }
    
    override suspend fun getOfflineTrack(): TrackDto? {
        return getTrackFromLocal()
    }
    
    private suspend fun getAuthToken(): String {
        val preferences = dataStore.data.first()
        return preferences[SUPABASE_ACCESS_TOKEN_KEY] ?: ""
    }
} 
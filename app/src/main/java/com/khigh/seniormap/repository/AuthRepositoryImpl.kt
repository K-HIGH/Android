package com.khigh.seniormap.repository

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.khigh.seniormap.model.dto.auth.*
import com.khigh.seniormap.model.dto.user.UserResponse
import com.khigh.seniormap.repository.SupabaseAuthRepository
import com.khigh.seniormap.model.entity.UserEntity
import com.khigh.seniormap.network.api.AuthApi
import kotlinx.serialization.json.Json
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton
import retrofit2.Response

/**
 * K-HIGH 서버 API Repository 구현체
 * 
 * K-HIGH 서버와의 통신을 담당하며, Supabase 토큰을 사용하여
 * 서버와 통신하고 사용자 정보를 관리합니다.
 */
@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val dataStore: DataStore<Preferences>,
    private val supabaseAuthRepository: SupabaseAuthRepository,
) : AuthRepository {
    
    companion object {
        private val SUPABASE_ACCESS_TOKEN_KEY = stringPreferencesKey("supabase_access_token")
        private const val TAG = "com.khigh.seniormap.repository.AuthRepositoryImpl"
        private val USER_ENTITY_KEY = stringPreferencesKey("user_entity")
    }
    
    // ==================== Auth API ====================
    
    override suspend fun login(request: UserLoginRequest): Result<Response<UserResponse>> {
        return try {
            Log.d(TAG, "login: Attempting to login with Supabase token")
            
            val response = authApi.login(request)
            
            if (response.isSuccessful) {
                // Supabase access_token을 캐싱
                saveSupabaseAccessToken(request.accessToken)
                Log.d(TAG, "login: Login successful, Supabase token cached")
                Result.success(response)
            } else {
                Log.e(TAG, "login: Login failed - ${response.code()}: ${response.message()}")
                Result.failure(Exception("K-HIGH 서버 로그인 실패: ${response.message()}"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "login: Exception occurred", e)
            Result.failure(e)
        }
    }
    
    override suspend fun logout(): Result<Unit> {
        return try {
            val token = getSupabaseAccessToken()
            if (token.isNullOrEmpty()) {
                Log.w(TAG, "logout: No token found, skipping server logout")
                clearSupabaseAccessToken()
                return Result.success(Unit)
            }
            
            Log.d(TAG, "logout: Attempting to logout from K-HIGH server")
            
            val response = authApi.logout("Bearer $token")
            
            if (response.isSuccessful) {
                clearSupabaseAccessToken()
                Log.d(TAG, "logout: Logout successful")
                Result.success(Unit)
            } else {
                Log.e(TAG, "logout: Logout failed - ${response.code()}: ${response.message()}")
                // 실패해도 로컬 토큰은 삭제
                clearSupabaseAccessToken()
                Result.failure(Exception("K-HIGH 서버 로그아웃 실패: ${response.message()}"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "logout: Exception occurred", e)
            // 예외 발생해도 로컬 토큰은 삭제
            clearSupabaseAccessToken()
            Result.failure(e)
        }
    }
    
    // ==================== 통합 기능 ====================
    
    override suspend fun syncWithServer(
        supabaseAccessToken: String,
    ): Result<Unit> {
        return try {
            Log.d(TAG, "syncWithServer: Starting sync process")
            
            // K-HIGH 서버에 로그인 (Supabase 토큰 캐싱)
            val loginResult = login(UserLoginRequest(accessToken = supabaseAccessToken))
            if (loginResult.isFailure) {
                Log.e(TAG, "syncWithServer: K-HIGH login failed")
                return Result.failure(loginResult.exceptionOrNull() ?: Exception("K-HIGH 로그인 실패"))
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "syncWithServer: Exception occurred", e)
            Result.failure(e)
        }
    }
    
    override suspend fun getUserEntity(): UserEntity? {
        val preferences = dataStore.data.first()
        return preferences[USER_ENTITY_KEY]?.let { Json.decodeFromString<UserEntity>(it) }
    }

    override suspend fun saveUserEntity(userEntity: UserEntity) {
        dataStore.updateData { preferences ->
            preferences.toMutablePreferences().apply {
                this[USER_ENTITY_KEY] = Json.encodeToString(userEntity)
            }
        }
    }

    // ==================== Private Helper Methods ====================
    private suspend fun saveSupabaseAccessToken(token: String) {
        dataStore.updateData { preferences ->
            preferences.toMutablePreferences().apply {
                this[SUPABASE_ACCESS_TOKEN_KEY] = token
            }
        }
    }
    
    private suspend fun getSupabaseAccessToken(): String? {
        val preferences = dataStore.data.first()
        return preferences[SUPABASE_ACCESS_TOKEN_KEY]
    }
    
    private suspend fun clearSupabaseAccessToken() {
        dataStore.updateData { preferences ->
            preferences.toMutablePreferences().apply {
                remove(SUPABASE_ACCESS_TOKEN_KEY)
            }
        }
    }
} 
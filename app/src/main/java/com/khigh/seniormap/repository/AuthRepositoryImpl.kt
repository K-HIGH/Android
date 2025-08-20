package com.khigh.seniormap.repository

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.khigh.seniormap.model.dto.*
import com.khigh.seniormap.model.entity.UserEntity
import com.khigh.seniormap.network.api.KHighApi
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

/**
 * K-HIGH 서버 API Repository 구현체
 * 
 * K-HIGH 서버와의 통신을 담당하며, Supabase 토큰을 사용하여
 * 서버 세션을 동기화하고 사용자 정보를 관리합니다.
 */
@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val kHighApi: KHighApi,
    private val dataStore: DataStore<Preferences>
) : AuthRepository {
    
    companion object {
        private val KHIGH_TOKEN_KEY = stringPreferencesKey("khigh_server_token")
        private const val TAG = "AuthRepositoryImpl"
    }
    
    // ==================== Auth API ====================
    
    override suspend fun login(accessToken: String): Result<String> {
        return try {
            Log.d(TAG, "login: Attempting to login with Supabase token")
            
            val request = KHighLoginRequest(accessToken = accessToken)
            val response = kHighApi.login(request)
            
            if (response.isSuccessful) {
                val serverToken = response.body()
                if (!serverToken.isNullOrEmpty()) {
                    // K-HIGH 서버 토큰 저장
                    saveKHighToken(serverToken)
                    Log.d(TAG, "login: Login successful, token saved")
                    Result.success(serverToken)
                } else {
                    Log.e(TAG, "login: Empty token received")
                    Result.failure(Exception("서버에서 유효하지 않은 토큰을 반환했습니다"))
                }
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
            val token = getKHighToken()
            if (token.isNullOrEmpty()) {
                Log.w(TAG, "logout: No token found, skipping server logout")
                clearKHighToken()
                return Result.success(Unit)
            }
            
            Log.d(TAG, "logout: Attempting to logout from K-HIGH server")
            
            val response = kHighApi.logout("Bearer $token")
            
            if (response.isSuccessful) {
                clearKHighToken()
                Log.d(TAG, "logout: Logout successful")
                Result.success(Unit)
            } else {
                Log.e(TAG, "logout: Logout failed - ${response.code()}: ${response.message()}")
                // 실패해도 로컬 토큰은 삭제
                clearKHighToken()
                Result.failure(Exception("K-HIGH 서버 로그아웃 실패: ${response.message()}"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "logout: Exception occurred", e)
            // 예외 발생해도 로컬 토큰은 삭제
            clearKHighToken()
            Result.failure(e)
        }
    }
    
    // ==================== User API ====================
    
    override suspend fun getCurrentUser(): Result<KHighUserResponse> {
        return try {
            val token = getKHighToken()
            if (token.isNullOrEmpty()) {
                Log.e(TAG, "getCurrentUser: No K-HIGH token found")
                return Result.failure(Exception("K-HIGH 서버 토큰이 없습니다"))
            }
            
            Log.d(TAG, "getCurrentUser: Fetching user info from K-HIGH server")
            
            val response = kHighApi.getCurrentUser("Bearer $token")
            
            if (response.isSuccessful) {
                val userResponse = response.body()
                if (userResponse != null) {
                    Log.d(TAG, "getCurrentUser: User info fetched successfully")
                    Result.success(userResponse)
                } else {
                    Log.e(TAG, "getCurrentUser: Empty response body")
                    Result.failure(Exception("서버에서 사용자 정보를 받지 못했습니다"))
                }
            } else {
                Log.e(TAG, "getCurrentUserFromServer: Failed - ${response.code()}: ${response.message()}")
                Result.failure(Exception("사용자 정보 조회 실패: ${response.message()}"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "getCurrentUserFromServer: Exception occurred", e)
            Result.failure(e)
        }
    }
    
    override suspend fun updateUserProfile(request: KHighUserProfileUpdateRequest): Result<Unit> {
        return try {
            val token = getKHighToken()
            if (token.isNullOrEmpty()) {
                Log.e(TAG, "updateUserProfile: No K-HIGH token found")
                return Result.failure(Exception("K-HIGH 서버 토큰이 없습니다"))
            }
            
            Log.d(TAG, "updateUserProfile: Updating user profile")
            
            val response = kHighApi.updateUserProfile("Bearer $token", request)
            
            if (response.isSuccessful) {
                Log.d(TAG, "updateUserProfile: Profile updated successfully")
                Result.success(Unit)
            } else {
                Log.e(TAG, "updateUserProfile: Failed - ${response.code()}: ${response.message()}")
                Result.failure(Exception("프로필 업데이트 실패: ${response.message()}"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "updateUserProfile: Exception occurred", e)
            Result.failure(e)
        }
    }
    
    override suspend fun updateFcmToken(fcmToken: String): Result<Unit> {
        return try {
            val token = getKHighToken()
            if (token.isNullOrEmpty()) {
                Log.e(TAG, "updateFcmToken: No K-HIGH token found")
                return Result.failure(Exception("K-HIGH 서버 토큰이 없습니다"))
            }
            
            Log.d(TAG, "updateFcmToken: Updating FCM token")
            
            val request = KHighFcmTokenRequest(fcmToken = fcmToken)
            val response = kHighApi.updateFcmToken("Bearer $token", request)
            
            if (response.isSuccessful) {
                Log.d(TAG, "updateFcmToken: FCM token updated successfully")
                Result.success(Unit)
            } else {
                Log.e(TAG, "updateFcmToken: Failed - ${response.code()}: ${response.message()}")
                Result.failure(Exception("FCM 토큰 업데이트 실패: ${response.message()}"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "updateFcmToken: Exception occurred", e)
            Result.failure(e)
        }
    }
    
    override suspend fun updateAlertFlag(isAlert: Boolean): Result<Unit> {
        return try {
            val token = getKHighToken()
            if (token.isNullOrEmpty()) {
                Log.e(TAG, "updateAlertFlag: No K-HIGH token found")
                return Result.failure(Exception("K-HIGH 서버 토큰이 없습니다"))
            }
            
            Log.d(TAG, "updateAlertFlag: Updating alert flag to $isAlert")
            
            val request = KHighAlertFlagRequest(isAlert = isAlert)
            val response = kHighApi.updateAlertFlag("Bearer $token", request)
            
            if (response.isSuccessful) {
                Log.d(TAG, "updateAlertFlag: Alert flag updated successfully")
                Result.success(Unit)
            } else {
                Log.e(TAG, "updateAlertFlag: Failed - ${response.code()}: ${response.message()}")
                Result.failure(Exception("알림 설정 업데이트 실패: ${response.message()}"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "updateAlertFlag: Exception occurred", e)
            Result.failure(e)
        }
    }
    
    override suspend fun deleteUser(): Result<Unit> {
        return try {
            val token = getKHighToken()
            if (token.isNullOrEmpty()) {
                Log.e(TAG, "deleteUser: No K-HIGH token found")
                return Result.failure(Exception("K-HIGH 서버 토큰이 없습니다"))
            }
            
            Log.d(TAG, "deleteUser: Deleting user account")
            
            val response = kHighApi.deleteUser("Bearer $token")
            
            if (response.isSuccessful) {
                clearKHighToken()
                Log.d(TAG, "deleteUser: User account deleted successfully")
                Result.success(Unit)
            } else {
                Log.e(TAG, "deleteUser: Failed - ${response.code()}: ${response.message()}")
                Result.failure(Exception("계정 삭제 실패: ${response.message()}"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "deleteUser: Exception occurred", e)
            Result.failure(e)
        }
    }
    
    // ==================== 통합 기능 ====================
    
    override suspend fun syncWithServer(
        supabaseAccessToken: String,
        userId: String,
        email: String
    ): Result<UserEntity> {
        return try {
            Log.d(TAG, "syncWithServer: Starting sync process")
            
            // 1. K-HIGH 서버에 로그인
            val loginResult = login(supabaseAccessToken)
            if (loginResult.isFailure) {
                Log.e(TAG, "syncWithServer: K-HIGH login failed")
                return Result.failure(loginResult.exceptionOrNull() ?: Exception("K-HIGH 로그인 실패"))
            }
            
            // 2. 서버에서 사용자 정보 조회
            val userInfoResult = getCurrentUser()
            if (userInfoResult.isFailure) {
                Log.e(TAG, "syncWithServer: Failed to get user info from K-HIGH")
                return Result.failure(userInfoResult.exceptionOrNull() ?: Exception("사용자 정보 조회 실패"))
            }
            
            // 3. 로컬 UserEntity 생성
            val kHighUser = userInfoResult.getOrNull()!!
            val userEntity = kHighUser.toUserEntity(userId, email)
            
            Log.d(TAG, "syncWithServer: Sync completed successfully")
            Result.success(userEntity)
            
        } catch (e: Exception) {
            Log.e(TAG, "syncWithServer: Exception occurred", e)
            Result.failure(e)
        }
    }
    
    // ==================== Private Helper Methods ====================
    
    private suspend fun saveKHighToken(token: String) {
        dataStore.updateData { preferences ->
            preferences.toMutablePreferences().apply {
                this[KHIGH_TOKEN_KEY] = token
            }
        }
    }
    
    private suspend fun getKHighToken(): String? {
        val preferences = dataStore.data.first()
        return preferences[KHIGH_TOKEN_KEY]
    }
    
    private suspend fun clearKHighToken() {
        dataStore.updateData { preferences ->
            preferences.toMutablePreferences().apply {
                remove(KHIGH_TOKEN_KEY)
            }
        }
    }
} 
package com.khigh.seniormap.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.khigh.seniormap.model.dto.*
import com.khigh.seniormap.model.entity.UserEntity
import com.khigh.seniormap.network.api.AuthApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 인증 관련 Repository 구현체
 */
@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val dataStore: DataStore<Preferences>
) : AuthRepository {
    
    companion object {
        private val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
        private val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")
        private val USER_ID_KEY = stringPreferencesKey("user_id")
        private val USER_EMAIL_KEY = stringPreferencesKey("user_email")
        private val USER_NAME_KEY = stringPreferencesKey("user_name")
    }
    
    override suspend fun loginWithOAuth(provider: String, accessToken: String): Result<LoginResponse> {
        return try {
            val request = OAuthLoginRequest(provider = provider, accessToken = accessToken)
            val response = authApi.loginWithOAuth(request)
            
            if (response.isSuccessful) {
                response.body()?.let { loginResponse ->
                    Result.success(loginResponse)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                Result.failure(Exception("Login failed: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun refreshToken(refreshToken: String): Result<LoginResponse> {
        return try {
            val request = RefreshTokenRequest(refreshToken = refreshToken)
            val response = authApi.refreshToken(request)
            
            if (response.isSuccessful) {
                response.body()?.let { loginResponse ->
                    Result.success(loginResponse)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                Result.failure(Exception("Token refresh failed: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun logout(): Result<Unit> {
        return try {
            val token = getAccessToken()
            if (token != null) {
                val response = authApi.logout("Bearer $token")
                if (response.isSuccessful) {
                    Result.success(Unit)
                } else {
                    Result.failure(Exception("Logout failed: ${response.code()}"))
                }
            } else {
                Result.success(Unit) // 토큰이 없으면 이미 로그아웃 상태
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getProfile(): Result<UserDto> {
        return try {
            val token = getAccessToken()
            if (token != null) {
                val response = authApi.getProfile("Bearer $token")
                if (response.isSuccessful) {
                    response.body()?.let { userDto ->
                        Result.success(userDto)
                    } ?: Result.failure(Exception("Empty response body"))
                } else {
                    Result.failure(Exception("Get profile failed: ${response.code()}"))
                }
            } else {
                Result.failure(Exception("No access token"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun updateProfile(request: ProfileUpdateRequest): Result<UserDto> {
        return try {
            val token = getAccessToken()
            if (token != null) {
                val response = authApi.updateProfile("Bearer $token", request)
                if (response.isSuccessful) {
                    response.body()?.let { userDto ->
                        Result.success(userDto)
                    } ?: Result.failure(Exception("Empty response body"))
                } else {
                    Result.failure(Exception("Update profile failed: ${response.code()}"))
                }
            } else {
                Result.failure(Exception("No access token"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun deleteUser(): Result<Unit> {
        return try {
            val token = getAccessToken()
            if (token != null) {
                val response = authApi.deleteUser("Bearer $token")
                if (response.isSuccessful) {
                    Result.success(Unit)
                } else {
                    Result.failure(Exception("Delete user failed: ${response.code()}"))
                }
            } else {
                Result.failure(Exception("No access token"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun saveUserLocally(user: UserEntity) {
        dataStore.edit { preferences ->
            preferences[USER_ID_KEY] = user.id
            preferences[USER_EMAIL_KEY] = user.email
            preferences[USER_NAME_KEY] = user.userName
        }
    }
    
    override suspend fun getLocalUser(): UserEntity? {
        val preferences = dataStore.data.first()
        val userId = preferences[USER_ID_KEY]
        val userEmail = preferences[USER_EMAIL_KEY]
        val userName = preferences[USER_NAME_KEY]
        
        return if (userId != null && userEmail != null && userName != null) {
            UserEntity(
                id = userId,
                email = userEmail,
                userName = userName,
                phone = null,
                isCaregiver = false,
                isHelper = false,
                fcmToken = null,
                isAlert = false,
                accessToken = getAccessToken(),
                refreshToken = getRefreshToken()
            )
        } else {
            null
        }
    }
    
    override suspend fun clearLocalUser() {
        dataStore.edit { preferences ->
            preferences.remove(USER_ID_KEY)
            preferences.remove(USER_EMAIL_KEY)
            preferences.remove(USER_NAME_KEY)
        }
    }
    
    override suspend fun saveAccessToken(token: String) {
        dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN_KEY] = token
        }
    }
    
    override suspend fun getAccessToken(): String? {
        return dataStore.data.first()[ACCESS_TOKEN_KEY]
    }
    
    override suspend fun saveRefreshToken(token: String) {
        dataStore.edit { preferences ->
            preferences[REFRESH_TOKEN_KEY] = token
        }
    }
    
    override suspend fun getRefreshToken(): String? {
        return dataStore.data.first()[REFRESH_TOKEN_KEY]
    }
    
    override suspend fun clearTokens() {
        dataStore.edit { preferences ->
            preferences.remove(ACCESS_TOKEN_KEY)
            preferences.remove(REFRESH_TOKEN_KEY)
        }
    }
    
    override fun observeAuthState(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[ACCESS_TOKEN_KEY] != null
        }
    }
    
    override fun observeCurrentUser(): Flow<UserEntity?> {
        return dataStore.data.map { preferences ->
            val userId = preferences[USER_ID_KEY]
            val userEmail = preferences[USER_EMAIL_KEY]
            val userName = preferences[USER_NAME_KEY]
            
            if (userId != null && userEmail != null && userName != null) {
                UserEntity(
                    id = userId,
                    email = userEmail,
                    userName = userName,
                    phone = null,
                    isCaregiver = false,
                    isHelper = false,
                    fcmToken = null,
                    isAlert = false,
                    accessToken = preferences[ACCESS_TOKEN_KEY],
                    refreshToken = preferences[REFRESH_TOKEN_KEY]
                )
            } else {
                null
            }
        }
    }
} 
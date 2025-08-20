package com.khigh.seniormap.repository

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.khigh.seniormap.model.dto.*
import com.khigh.seniormap.model.entity.UserEntity
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.status.SessionStatus
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.OAuthProvider
import io.github.jan.supabase.auth.providers.Google
import io.github.jan.supabase.auth.providers.Kakao
import io.github.jan.supabase.auth.user.UserInfo
import com.khigh.seniormap.network.SupabaseModule
import io.github.jan.supabase.auth.handleDeeplinks
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Supabase 기반 인증 Repository 구현체
 */
@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val supabaseClient: SupabaseClient,
    private val dataStore: DataStore<Preferences>
) : AuthRepository {
    
    companion object {
        private val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
        private val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")
        private val USER_ID_KEY = stringPreferencesKey("user_id")
        private val USER_EMAIL_KEY = stringPreferencesKey("user_email")
        private val USER_NAME_KEY = stringPreferencesKey("user_name")
    }
    
    override suspend fun loginWithOAuth(provider: OAuthProvider): Result<Unit> = runCatching {
        Log.d("com.khigh.seniormap", "[AuthRepositoryImpl] loginWithOAuth: $provider")
        supabaseClient.auth.signInWith(provider)
    }

    override suspend fun handleCallback(intent: Intent) {
        supabaseClient.handleDeeplinks(intent)
    }
    
    override suspend fun getCurrentUser(): Result<UserInfo?> {
        return try {
            val user = supabaseClient.auth.currentUserOrNull()
            // val userDto = user?.toUserDto()
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun refreshSession(): Result<UserInfo> {
        return try {
            val session = supabaseClient.auth.refreshCurrentSession()
            val user = supabaseClient.auth.currentUserOrNull() 
            
            // val userDto = user?.toUserDto()
            
            if (user != null) {
                saveUserSessionLocally(user)
                Result.success(user)
            } else {
                Result.failure(Exception("세션 갱신 실패"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun logout(): Result<Unit> {
        return try {
            supabaseClient.auth.signOut()
            clearTokens()
            clearLocalUser()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
//    override suspend fun updateProfile(request: UserProfileUpdateRequest): Result<UserInfo> {
//        return try {
//            val updatedUser = supabaseClient.auth.updateUser {
//                data {
//                    put("user_name", request.userName)
//                    put("phone", request.phone)
//                    put("is_helper", request.isHelper)
//                }
//
//            }
//
//            // val userDto = updatedUser.toUserDto()
//            saveUserSessionLocally(updatedUser)
//
//            Result.success(updatedUser)
//        } catch (e: Exception) {
//            Result.failure(e)
//        }
//    }
    
    override suspend fun deleteUser(): Result<Unit> {
        return try {
            // Supabase에서는 관리자 권한이 필요하므로 클라이언트에서 직접 삭제 불가
            // 대신 서버 API를 통해 삭제 요청을 보내야 함
            logout()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Supabase 사용자 세션을 로컬에 저장
     */
    private suspend fun saveUserSessionLocally(user: UserInfo) {
        val session = supabaseClient.auth.currentSessionOrNull()
        session?.let {
            saveAccessToken(it.accessToken)
            saveRefreshToken(it.refreshToken ?: "")
        }
        
        dataStore.edit { preferences ->
            preferences[USER_ID_KEY] = user.id
            preferences[USER_EMAIL_KEY] = user.email ?: ""
            preferences[USER_NAME_KEY] = user.userMetadata?.get("name")?.toString() ?: "사용자"
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
    
    override fun observeAuthState(): Flow<SessionStatus> {
        Log.d("com.khigh.seniormap", "[AuthRepositoryImpl] observeAuthState")
        return supabaseClient.auth.sessionStatus
    }

    override fun getCurrentSessionStatus(): SessionStatus {
        return supabaseClient.auth.sessionStatus.value
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
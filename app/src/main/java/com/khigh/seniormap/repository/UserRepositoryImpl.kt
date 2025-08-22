package com.khigh.seniormap.repository

import javax.inject.Inject
import javax.inject.Singleton
import com.khigh.seniormap.network.api.UserApi
import com.khigh.seniormap.repository.UserRepository
import com.khigh.seniormap.model.dto.user.*
import com.khigh.seniormap.model.dto.user.UserResponse
import com.khigh.seniormap.model.entity.UserEntity
import com.khigh.seniormap.model.dto.ApiMessage
import retrofit2.Response
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.flow.first
import android.util.Log
import com.khigh.seniormap.repository.SupabaseAuthRepository
import kotlinx.serialization.json.Json


@Singleton
class UserRepositoryImpl @Inject constructor(
    private val userApi: UserApi,
    private val dataStore: DataStore<Preferences>,
    private val supabaseAuthRepository: SupabaseAuthRepository
) : UserRepository {

    companion object {
        private const val TAG = "com.khigh.seniormap.repository.UserRepositoryImpl"
        private val USER_ENTITY_KEY = stringPreferencesKey("user_entity")
    }

    override suspend fun getUserInfo(): Result<Response<UserResponse>> {
        return try {
            val token = supabaseAuthRepository.getAccessToken()
            if (token.isNullOrEmpty()) {
                Log.e(TAG, "getUserInfo: No Supabase access token found")
                return Result.failure(Exception("Supabase 액세스 토큰이 없습니다"))
            }
            
            Log.d(TAG, "getUserInfo: Fetching user info from K-HIGH server")

            val response = userApi.getUser("Bearer $token")

            if (!response.isSuccessful) {
                Log.e(TAG, "getUserInfo: Failed - ${response.code()}: ${response.message()}")
                return Result.failure(Exception("사용자 정보 조회 실패: ${response.message()}"))
            }

            val userResponse = response.body()
            if (userResponse == null) {
                Log.e(TAG, "getUserInfo: Empty response body")
                return Result.failure(Exception("서버에서 사용자 정보를 받지 못했습니다"))
            }
            val userEntity = userResponse.toEntity()
            saveUserLocally(userEntity)

            return Result.success(response)
        } catch (e: Exception) {
            Log.e(TAG, "getUserInfo: Exception occurred, ${e.message}")
            return Result.failure(e)
        } finally {
            Log.d(TAG, "getUserInfo: Completed")
        }
    }

    override suspend fun updateUserProfile(request: UserProfileUpdateRequest): Result<Response<UserResponse>> {
        return try {
            val token = supabaseAuthRepository.getAccessToken()
            if (token.isNullOrEmpty()) {
                Log.e(TAG, "updateUserProfile: No Supabase access token found")
                return Result.failure(Exception("Supabase 액세스 토큰이 없습니다"))
            }
            
            Log.d(TAG, "updateUserProfile: Updating user profile on K-HIGH server")

            val response = userApi.updateUserProfile("Bearer $token", request)

            if (!response.isSuccessful) {
                Log.e(TAG, "updateUserProfile: Failed - ${response.code()}: ${response.message()}")
                return Result.failure(Exception("사용자 프로필 업데이트 실패: ${response.message()}"))
            }

            val userResponse = response.body()
            if (userResponse == null) {
                Log.e(TAG, "updateUserProfile: Empty response body")
                return Result.failure(Exception("서버에서 사용자 정보를 받지 못했습니다"))
            }
            val userEntity = userResponse.toEntity()
            saveUserLocally(userEntity)

            return Result.success(response)
        } catch (e: Exception) {
            Log.e(TAG, "updateUserProfile: Exception occurred, ${e.message}")
            return Result.failure(e)
        } finally {
            Log.d(TAG, "updateUserProfile: Completed")
        }
    }

    override suspend fun updateFcmToken(request: FcmTokenRequest): Result<Response<UserResponse>> {
        return try {
            val token = supabaseAuthRepository.getAccessToken()
            if (token.isNullOrEmpty()) {
                Log.e(TAG, "updateFcmToken: No Supabase access token found")
                return Result.failure(Exception("Supabase 액세스 토큰이 없습니다"))
            }
            
            Log.d(TAG, "updateFcmToken: Updating FCM token on K-HIGH server")

            val response = userApi.updateFcmToken("Bearer $token", request)

            if (!response.isSuccessful) {
                Log.e(TAG, "updateFcmToken: Failed - ${response.code()}: ${response.message()}")
                return Result.failure(Exception("FCM 토큰 업데이트 실패: ${response.message()}"))
            }

            val userResponse = response.body()
            if (userResponse == null) {
                Log.e(TAG, "updateFcmToken: Empty response body")
                return Result.failure(Exception("서버에서 사용자 정보를 받지 못했습니다"))
            }
            val userEntity = userResponse.toEntity()
            saveUserLocally(userEntity)

            return Result.success(response)
        } catch (e: Exception) {
            Log.e(TAG, "updateFcmToken: Exception occurred, ${e.message}")
            return Result.failure(e)
        } finally {
            Log.d(TAG, "updateFcmToken: Completed")
        }
    }

    override suspend fun updateAlertFlag(request: AlertFlagRequest): Result<Response<UserResponse>> {
        return try {
            val token = supabaseAuthRepository.getAccessToken()
            if (token.isNullOrEmpty()) {
                Log.e(TAG, "updateAlertFlag: No Supabase access token found")
                return Result.failure(Exception("Supabase 액세스 토큰이 없습니다"))
            }
            
            Log.d(TAG, "updateAlertFlag: Updating alert flag on K-HIGH server")

            val response = userApi.updateAlertFlag("Bearer $token", request)

            if (!response.isSuccessful) {
                Log.e(TAG, "updateAlertFlag: Failed - ${response.code()}: ${response.message()}")
                return Result.failure(Exception("알림 설정 업데이트 실패: ${response.message()}"))
            }

            val userResponse = response.body()
            if (userResponse == null) {
                Log.e(TAG, "updateFcmToken: Empty response body")
                return Result.failure(Exception("서버에서 사용자 정보를 받지 못했습니다"))
            }
            val supabaseUser = supabaseAuthRepository.getLocalUser()
            val supabaseAccessToken = supabaseAuthRepository.getAccessToken() ?: ""
            val supabaseRefreshToken = supabaseAuthRepository.getRefreshToken() ?: ""
            val userEntity = userResponse.toEntity()
            saveUserLocally(userEntity)

            return Result.success(response)
        } catch (e: Exception) {
            Log.e(TAG, "updateAlertFlag: Exception occurred, ${e.message}")
            return Result.failure(e)
        } finally {
            Log.d(TAG, "updateAlertFlag: Completed")
        }
    }

    override suspend fun deleteUser(): Result<Response<ApiMessage>> {
        return try {
            val token = supabaseAuthRepository.getAccessToken()
            if (token.isNullOrEmpty()) {
                Log.e(TAG, "deleteUser: No Supabase access token found")
                return Result.failure(Exception("Supabase 액세스 토큰이 없습니다"))
            }
            
            Log.d(TAG, "deleteUser: Deleting user account on K-HIGH server")

            val response = userApi.deleteUser("Bearer $token")

            if (!response.isSuccessful) {
                Log.e(TAG, "deleteUser: Failed - ${response.code()}: ${response.message()}")
                return Result.failure(Exception("사용자 계정 삭제 실패: ${response.message()}"))
            }

            clearUserLocally()

            return Result.success(response)
        } catch (e: Exception) {
            Log.e(TAG, "deleteUser: Exception occurred, ${e.message}")
            return Result.failure(e)
        } finally {
            Log.d(TAG, "deleteUser: Completed")
        }
    }

    // =============================== Private Methods ===============================

    private suspend fun getUserEntity(): UserEntity? {
        val preferences = dataStore.data.first()
        return preferences[USER_ENTITY_KEY]?.let { Json.decodeFromString<UserEntity>(it) }
    }

    private suspend fun saveUserLocally(userEntity: UserEntity) {
        dataStore.edit { preferences ->
            preferences[USER_ENTITY_KEY] = Json.encodeToString(userEntity)
        }
    }

    private suspend fun clearUserLocally() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}
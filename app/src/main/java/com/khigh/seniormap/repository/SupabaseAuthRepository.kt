package com.khigh.seniormap.repository

import android.content.Intent
import com.khigh.seniormap.model.entity.UserEntity
import io.github.jan.supabase.auth.status.SessionStatus
import io.github.jan.supabase.auth.user.UserInfo
import io.github.jan.supabase.auth.providers.OAuthProvider
import kotlinx.coroutines.flow.Flow

/**
 * 인증 관련 Repository 인터페이스 (Supabase 기반)
 */
interface SupabaseAuthRepository {
    
    /**
     * Supabase OAuth 로그인 (카카오, 구글)
     */
    suspend fun loginWithOAuth(provider: OAuthProvider): Result<Unit>
    
    /**
     * OAuth 딥링크 처리
     */
    suspend fun handleCallback(intent: Intent)

    /**
     * 현재 사용자 정보 조회
     */
    suspend fun getCurrentUser(): Result<UserInfo?>
    
    /**
     * 토큰 갱신
     */
    suspend fun refreshSession(): Result<UserInfo>
    
    /**
     * 로그아웃
     */
    suspend fun logout(): Result<Unit>
    
    /**
     * 사용자 프로필 업데이트
     */
//    suspend fun updateProfile(request: UserProfileUpdateRequest): Result<UserInfo>
    
    /**
     * 계정 삭제
     */
    suspend fun deleteUser(): Result<Unit>
    
    /**
     * 로컬 사용자 정보 저장
     */
    suspend fun saveUserLocally(user: UserEntity)
    
    /**
     * 로컬 사용자 정보 조회
     */
    suspend fun getLocalUser(): UserEntity?
    
    /**
     * 로컬 사용자 정보 삭제
     */
    suspend fun clearLocalUser()
    
    /**
     * 액세스 토큰 저장
     */
    suspend fun saveAccessToken(token: String)
    
    /**
     * 액세스 토큰 조회
     */
    suspend fun getAccessToken(): String?
    
    /**
     * 리프레시 토큰 저장
     */
    suspend fun saveRefreshToken(token: String)
    
    /**
     * 리프레시 토큰 조회
     */
    suspend fun getRefreshToken(): String?
    
    /**
     * 모든 토큰 삭제
     */
    suspend fun clearTokens()

    /**
     * 로그인 상태 관찰
     */
    fun observeAuthState(): Flow<SessionStatus>
    
    /**
     * 현재 세션 상태 조회
     */
    fun getCurrentSessionStatus(): SessionStatus

    /**
     * 현재 사용자 정보 관찰
     */
    fun observeCurrentUser(): Flow<UserEntity?>
} 
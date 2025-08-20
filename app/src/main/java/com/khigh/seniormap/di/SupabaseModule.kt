package com.khigh.seniormap.network

import com.khigh.seniormap.BuildConfig
import com.khigh.seniormap.repository.AuthRepository
import com.khigh.seniormap.repository.AuthRepositoryImpl
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.FlowType
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.storage.storage
import io.github.jan.supabase.storage.Storage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import io.github.jan.supabase.logging.LogLevel

/**
 * Supabase 클라이언트 래퍼
 * 인증과 실시간 기능을 제공
 * OAuth 딥링크 처리를 위한 설정 포함
 */
@InstallIn(SingletonComponent::class)
@Module
object SupabaseModule {
    @Provides
    @Singleton
    fun provideSupabaseClient(): SupabaseClient {
        return createSupabaseClient(
            supabaseUrl = BuildConfig.SUPABASE_URL,
            supabaseKey = BuildConfig.SUPABASE_PUBLISHABLE_KEY
        ) {
            install(Postgrest)
            install(Auth) {
                flowType = FlowType.IMPLICIT
                scheme = "com.khigh.seniormap"
                host = "oauth"
                // 세션 지속성을 위한 기본 설정 사용
            }
            install(Storage)
        }
    }
    
    @Provides @Singleton fun provideSupabaseDatabase(client: SupabaseClient): Postgrest = client.postgrest
    @Provides @Singleton fun provideSupabaseAuth(client: SupabaseClient): Auth = client.auth
    @Provides @Singleton fun provideSupabaseStorage(client: SupabaseClient): Storage = client.storage
}
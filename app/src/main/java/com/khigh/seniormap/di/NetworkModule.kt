package com.khigh.seniormap.di

import com.khigh.seniormap.BuildConfig
import com.khigh.seniormap.constants.AppConstants
import com.khigh.seniormap.network.api.SupabaseAuthApi
import com.khigh.seniormap.network.api.KHighApi
import com.khigh.seniormap.network.interceptor.AuthInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

/**
 * 네트워크 관련 의존성 주입 모듈
 * 
 * Supabase API와 K-HIGH 서버 API를 위한
 * Retrofit, OkHttpClient 등의 네트워크 구성요소를 제공합니다.
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    
    // ==================== Qualifiers ====================
    
    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class SupabaseRetrofit
    
    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class KHighRetrofit
    
    // ==================== Common Dependencies ====================
    
    @Provides
    @Singleton
    fun provideJson(): Json {
        return Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
            encodeDefaults = true
            isLenient = true
        }
    }
    
    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
    }
    
    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        authInterceptor: AuthInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .connectTimeout(AppConstants.Api.TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(AppConstants.Api.TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(AppConstants.Api.TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .build()
    }
    
    // ==================== Supabase API Dependencies ====================
    
    @Provides
    @Singleton
    @SupabaseRetrofit
    fun provideSupabaseRetrofit(
        okHttpClient: OkHttpClient,
        json: Json
    ): Retrofit {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
    }
    
    @Provides
    @Singleton
    fun provideSupabaseAuthApi(@SupabaseRetrofit retrofit: Retrofit): SupabaseAuthApi {
        return retrofit.create(SupabaseAuthApi::class.java)
    }
    
    // ==================== K-HIGH API Dependencies ====================
    
    @Provides
    @Singleton
    @KHighRetrofit
    fun provideKHighRetrofit(
        json: Json
    ): Retrofit {
        val contentType = "application/json".toMediaType()
        
        // K-HIGH API용 별도 OkHttpClient (AuthInterceptor 제외)
        val kHighClient = OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = if (BuildConfig.DEBUG) {
                        HttpLoggingInterceptor.Level.BODY
                    } else {
                        HttpLoggingInterceptor.Level.NONE
                    }
                }
            )
            .connectTimeout(AppConstants.Api.TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(AppConstants.Api.TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(AppConstants.Api.TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .build()
        
        return Retrofit.Builder()
            .baseUrl("http://121.157.24.40:63001/") // K-HIGH 서버 URL
            .client(kHighClient)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
    }
    
    @Provides
    @Singleton
    fun provideKHighApi(@KHighRetrofit retrofit: Retrofit): KHighApi {
        return retrofit.create(KHighApi::class.java)
    }
} 
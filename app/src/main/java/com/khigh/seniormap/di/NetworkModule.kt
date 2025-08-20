package com.khigh.seniormap.di

import com.khigh.seniormap.BuildConfig
import com.khigh.seniormap.constants.AppConstants
import com.khigh.seniormap.network.TokenProvider
import com.khigh.seniormap.network.api.AuthApi
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
import javax.inject.Singleton

/**
 * 네트워크 관련 의존성 주입 모듈
 * 
 * Retrofit, OkHttp, API 인터페이스 등 네트워크 통신에 필요한
 * 의존성들을 제공합니다.
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    /**
     * kotlinx.serialization Json 인스턴스 제공
     */
    @Provides
    @Singleton
    fun provideJson(): Json {
        return Json {
            ignoreUnknownKeys = true // 알 수 없는 키 무시
            coerceInputValues = true // null이나 잘못된 값을 기본값으로 변환
            encodeDefaults = true // 기본값도 인코딩
            isLenient = true // 유연한 파싱
        }
    }

    /**
     * HTTP 로깅 인터셉터 제공
     */
    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG_MODE) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
    }

    /**
     * 인증 인터셉터 제공
     */
    @Provides
    @Singleton
    fun provideAuthInterceptor(tokenProvider: TokenProvider): AuthInterceptor {
        return AuthInterceptor(tokenProvider)
    }

    /**
     * OkHttpClient 제공
     */
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

    /**
     * Retrofit 인스턴스 제공
     */
    @Provides
    @Singleton
    fun provideRetrofit(
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

    /**
     * 인증 API 인터페이스 제공
     */
    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi {
        return retrofit.create(AuthApi::class.java)
    }
} 
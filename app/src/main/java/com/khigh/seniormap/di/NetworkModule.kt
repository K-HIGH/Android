package com.khigh.seniormap.di

import com.khigh.seniormap.BuildConfig
import com.khigh.seniormap.constants.AppConstants
import com.khigh.seniormap.network.api.SupabaseAuthApi
import com.khigh.seniormap.network.api.AuthApi
import com.khigh.seniormap.network.api.UserApi
import com.khigh.seniormap.network.api.CaregiverApi
import com.khigh.seniormap.network.api.RouteApi
import com.khigh.seniormap.network.api.TrackApi
import com.khigh.seniormap.network.api.FavoriteApi
import com.khigh.seniormap.network.api.SafetyAreaApi
import com.khigh.seniormap.network.api.CaretakerApi
import com.khigh.seniormap.network.api.KakaoMapApi
import com.khigh.seniormap.network.api.LocationApi
import com.khigh.seniormap.network.interceptor.AuthInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
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
annotation class ServerRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class KakaoMapRetrofit
    
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
    @ServerRetrofit
    fun provideServerRetrofit(
        json: Json
    ): Retrofit {
        val contentType = "application/json".toMediaType()
        
        // Server API용 별도 OkHttpClient (AuthInterceptor 제외)
        val serverClient = OkHttpClient.Builder()
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
            .baseUrl("http://121.157.24.40:63001/") // Server URL
            .client(serverClient)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
    }
    
    @Provides
    @Singleton
    fun provideAuthApi(@ServerRetrofit retrofit: Retrofit): AuthApi {
        return retrofit.create(AuthApi::class.java)
    }
    
    @Provides
    @Singleton
    fun provideUserApi(@ServerRetrofit retrofit: Retrofit): UserApi {
        return retrofit.create(UserApi::class.java)
    }
    
    @Provides
    @Singleton
    fun provideCaregiverApi(@ServerRetrofit retrofit: Retrofit): CaregiverApi {
        return retrofit.create(CaregiverApi::class.java)
    }
    
    @Provides
    @Singleton
    fun provideRouteApi(@ServerRetrofit retrofit: Retrofit): RouteApi {
        return retrofit.create(RouteApi::class.java)
    }
    
    @Provides
    @Singleton
    fun provideTrackApi(@ServerRetrofit retrofit: Retrofit): TrackApi {
        return retrofit.create(TrackApi::class.java)
    }
    
    @Provides
    @Singleton
    fun provideFavoriteApi(@ServerRetrofit retrofit: Retrofit): FavoriteApi {
        return retrofit.create(FavoriteApi::class.java)
    }
    
    @Provides
    @Singleton
    fun provideSafetyAreaApi(@ServerRetrofit retrofit: Retrofit): SafetyAreaApi {
        return retrofit.create(SafetyAreaApi::class.java)
    }
    
    @Provides
    @Singleton
    fun provideCaretakerApi(@ServerRetrofit retrofit: Retrofit): CaretakerApi {
        return retrofit.create(CaretakerApi::class.java)
    }
    
    // ==================== Kakao Map API Dependencies ====================
    
    @Provides
    @Singleton
    @KakaoMapRetrofit
    fun provideKakaoMapRetrofit(
        json: Json
    ): Retrofit {
        val contentType = "application/json".toMediaType()
        
        // Kakao Map API용 별도 OkHttpClient
        val kakaoMapClient = OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = if (BuildConfig.DEBUG) {
                        HttpLoggingInterceptor.Level.BODY
                    } else {
                        HttpLoggingInterceptor.Level.NONE
                    }
                }
            )
            .addInterceptor(
                Interceptor { chain ->
                    val newRequest = chain.request().newBuilder()
                        .header("Authorization", "KakaoAK ${BuildConfig.KAKAO_REST_API_KEY}")
                        .build()
                    return@Interceptor chain.proceed(newRequest)
                }
            )
            .connectTimeout(AppConstants.Api.TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(AppConstants.Api.TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(AppConstants.Api.TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .build()
        
        return Retrofit.Builder()
            .baseUrl("https://dapi.kakao.com/") // Kakao Map API URL
            .client(kakaoMapClient)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
    }
    
    @Provides
    @Singleton
    fun provideKakaoMapApi(@KakaoMapRetrofit retrofit: Retrofit): KakaoMapApi {
        return retrofit.create(KakaoMapApi::class.java)
    }
    
    @Provides
    @Singleton
    fun provideLocationApi(@KakaoMapRetrofit retrofit: Retrofit): LocationApi {
        return retrofit.create(LocationApi::class.java)
    }
} 
package com.khigh.seniormap.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.khigh.seniormap.repository.SupabaseAuthRepository
import com.khigh.seniormap.repository.SupabaseAuthRepositoryImpl
import com.khigh.seniormap.repository.AuthRepository
import com.khigh.seniormap.repository.AuthRepositoryImpl
import com.khigh.seniormap.repository.UserRepository
import com.khigh.seniormap.repository.UserRepositoryImpl
import com.khigh.seniormap.repository.CaregiverRepository
import com.khigh.seniormap.repository.CaregiverRepositoryImpl
import com.khigh.seniormap.repository.RouteRepository
import com.khigh.seniormap.repository.RouteRepositoryImpl
import com.khigh.seniormap.repository.TrackRepository
import com.khigh.seniormap.repository.TrackRepositoryImpl
import com.khigh.seniormap.repository.FavoriteRepository
import com.khigh.seniormap.repository.FavoriteRepositoryImpl
import com.khigh.seniormap.repository.SafetyAreaRepository
import com.khigh.seniormap.repository.SafetyAreaRepositoryImpl
import com.khigh.seniormap.repository.CaretakerRepository
import com.khigh.seniormap.repository.CaretakerRepositoryImpl
import com.khigh.seniormap.repository.KakaoMapRepository
import com.khigh.seniormap.repository.KakaoMapRepositoryImpl
import com.khigh.seniormap.repository.LocationRepository
import com.khigh.seniormap.repository.LocationRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * 데이터 레이어 의존성 주입 모듈
 * 
 * Repository 구현체와 DataStore 등
 * 데이터 관련 의존성들을 제공합니다.
 */

// DataStore 확장 프로퍼티
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_preferences")

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {
    
    // ==================== Repository Bindings ====================
    
    /**
     * SupabaseAuthRepository 구현체 바인딩
     */
    @Binds
    @Singleton
    abstract fun bindSupabaseAuthRepository(
        supabaseAuthRepositoryImpl: SupabaseAuthRepositoryImpl
    ): SupabaseAuthRepository
    
    /**
     * AuthRepository 구현체 바인딩
     */
    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository
    
    /**
     * UserRepository 구현체 바인딩
     */
    @Binds
    @Singleton
    abstract fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository
    
    /**
     * CaregiverRepository 구현체 바인딩
     */
    @Binds
    @Singleton
    abstract fun bindCaregiverRepository(
        caregiverRepositoryImpl: CaregiverRepositoryImpl
    ): CaregiverRepository
    
    /**
     * RouteRepository 구현체 바인딩
     */
    @Binds
    @Singleton
    abstract fun bindRouteRepository(
        routeRepositoryImpl: RouteRepositoryImpl
    ): RouteRepository
    
    /**
     * TrackRepository 구현체 바인딩
     */
    @Binds
    @Singleton
    abstract fun bindTrackRepository(
        trackRepositoryImpl: TrackRepositoryImpl
    ): TrackRepository
    
    /**
     * FavoriteRepository 구현체 바인딩
     */
    @Binds
    @Singleton
    abstract fun bindFavoriteRepository(
        favoriteRepositoryImpl: FavoriteRepositoryImpl
    ): FavoriteRepository
    
    /**
     * SafetyAreaRepository 구현체 바인딩
     */
    @Binds
    @Singleton
    abstract fun bindSafetyAreaRepository(
        safetyAreaRepositoryImpl: SafetyAreaRepositoryImpl
    ): SafetyAreaRepository
    
    /**
     * CaretakerRepository 구현체 바인딩
     */
    @Binds
    @Singleton
    abstract fun bindCaretakerRepository(
        caretakerRepositoryImpl: CaretakerRepositoryImpl
    ): CaretakerRepository
    
    /**
     * KakaoMapRepository 구현체 바인딩
     */
    @Binds
    @Singleton
    abstract fun bindKakaoMapRepository(
        kakaoMapRepositoryImpl: KakaoMapRepositoryImpl
    ): KakaoMapRepository

    /**
     * LocationRepository 구현체 바인딩
     */
    @Binds
    @Singleton
    abstract fun bindLocationRepository(
        locationRepositoryImpl: LocationRepositoryImpl
    ): LocationRepository

    companion object {
        
        // ==================== DataStore Providers ====================
        
        /**
         * DataStore<Preferences> 인스턴스 제공
         */
        @Provides
        @Singleton
        fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
            return context.dataStore
        }
    }

    
} 
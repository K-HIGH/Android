package com.khigh.seniormap.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.khigh.seniormap.repository.SupabaseAuthRepository
import com.khigh.seniormap.repository.SupabaseAuthRepositoryImpl
import com.khigh.seniormap.repository.AuthRepository
import com.khigh.seniormap.repository.AuthRepositoryImpl
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
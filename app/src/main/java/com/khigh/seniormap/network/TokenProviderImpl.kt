package com.khigh.seniormap.network

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton

/**
 * TokenProvider의 구현체
 * DataStore를 사용하여 토큰을 동기적으로 제공
 */
@Singleton
class TokenProviderImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : TokenProvider {
    
    companion object {
        private val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
        private val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")
    }
    
    override fun getAccessToken(): String? {
        return runBlocking {
            try {
                dataStore.data.first()[ACCESS_TOKEN_KEY]
            } catch (e: Exception) {
                null
            }
        }
    }
    
    override fun getRefreshToken(): String? {
        return runBlocking {
            try {
                dataStore.data.first()[REFRESH_TOKEN_KEY]
            } catch (e: Exception) {
                null
            }
        }
    }
} 
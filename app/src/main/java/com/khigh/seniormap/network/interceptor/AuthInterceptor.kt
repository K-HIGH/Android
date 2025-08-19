package com.khigh.seniormap.network.interceptor

import com.khigh.seniormap.network.TokenProvider
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 네트워크 요청에 인증 토큰을 자동으로 추가하는 인터셉터
 */
@Singleton
class AuthInterceptor @Inject constructor(
    private val tokenProvider: TokenProvider
) : Interceptor {
    
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        
        // 이미 Authorization 헤더가 있는 경우 그대로 진행
        if (originalRequest.header("Authorization") != null) {
            return chain.proceed(originalRequest)
        }
        
        // 액세스 토큰이 있는 경우 헤더에 추가
        val accessToken = tokenProvider.getAccessToken()
        
        val newRequest = if (accessToken != null) {
            originalRequest.newBuilder()
                .addHeader("Authorization", "Bearer $accessToken")
                .build()
        } else {
            originalRequest
        }
        
        return chain.proceed(newRequest)
    }
} 
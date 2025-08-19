package com.khigh.seniormap.network

/**
 * 인증 토큰 제공을 위한 인터페이스
 * AuthInterceptor의 순환 참조를 방지하기 위해 분리됨
 */
interface TokenProvider {
    
    /**
     * 현재 저장된 액세스 토큰을 동기적으로 반환
     * 
     * @return 액세스 토큰 또는 null
     */
    fun getAccessToken(): String?
    
    /**
     * 현재 저장된 리프레시 토큰을 동기적으로 반환
     * 
     * @return 리프레시 토큰 또는 null
     */
    fun getRefreshToken(): String?
} 
package com.khigh.seniormap.repository

import com.khigh.seniormap.model.dto.kakaomap.KakaoMapAddressResponse
import com.khigh.seniormap.model.dto.kakaomap.KakaoMapSearchResponse

interface KakaoMapRepository {
    
    // 장소 검색 관련
    suspend fun searchPlaces(
        query: String,
        categoryGroupCode: String? = null,
        x: String? = null,
        y: String? = null,
        radius: Int? = null,
        rect: String? = null,
        page: Int? = null,
        size: Int? = null,
        sort: String? = null
    ): Result<KakaoMapSearchResponse>
    
    // 주소 검색 관련
    suspend fun searchAddresses(
        query: String,
        analyzeType: String? = null,
        page: Int? = null,
        size: Int? = null
    ): Result<KakaoMapAddressResponse>
    
    // 카테고리별 검색
    suspend fun searchByCategory(
        categoryGroupCode: String,
        x: String,
        y: String,
        radius: Int? = null,
        rect: String? = null,
        page: Int? = null,
        size: Int? = null,
        sort: String? = null
    ): Result<KakaoMapSearchResponse>
    
    // 좌표로 주소 변환
    suspend fun getAddressFromCoordinates(
        x: String,
        y: String,
        inputCoord: String = "WGS84"
    ): Result<KakaoMapAddressResponse>
    
    // 주소로 좌표 변환
    suspend fun getCoordinatesFromAddress(
        x: String,
        y: String,
        inputCoord: String = "WGS84"
    ): Result<KakaoMapAddressResponse>
    
    // 오프라인 저장소 관련
    suspend fun saveSearchResultsToLocal(query: String, results: KakaoMapSearchResponse)
    suspend fun getSearchResultsFromLocal(query: String): KakaoMapSearchResponse?
    suspend fun clearLocalSearchResults()
    
    // 캐시 관리
    suspend fun clearExpiredCache()
    suspend fun getCacheSize(): Long
} 
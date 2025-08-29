package com.khigh.seniormap.repository

import com.khigh.seniormap.model.dto.kakaomap.PlaceSearchResult
import com.khigh.seniormap.network.api.LocationApi
import com.khigh.seniormap.repository.LocationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * LocationRepository의 구현체
 * 카카오맵 API를 통해 위치 검색 기능을 제공
 */
@Singleton
class LocationRepositoryImpl @Inject constructor(
    private val locationApi: LocationApi
) : LocationRepository {
    
    override suspend fun searchPlacesByKeyword(
        query: String,
        size: Int,
        page: Int,
        category: String?,
        centerLat: Double?,
        centerLng: Double?,
        radius: Int?,
        sort: String
    ): Result<List<PlaceSearchResult>> = withContext(Dispatchers.IO) {
        runCatching {
            val response = locationApi.searchPlacesByKeyword(
                query = query,
                size = size,
                page = page,
                category = category,
                x = centerLng?.toString(),
                y = centerLat?.toString(),
                radius = radius,
                sort = sort
            )
            
            // KakaoMapSearchResponse를 PlaceSearchResult 리스트로 변환
            response.documents.map { it.toPlaceSearchResult() }
        }.recoverCatching { exception ->
            throw LocationSearchException(
                message = "키워드 검색 중 오류가 발생했습니다: ${exception.message}",
                cause = exception
            )
        }
    }
    
    override suspend fun searchPlacesByCategory(
        category: String,
        centerLat: Double,
        centerLng: Double,
        radius: Int,
        size: Int,
        page: Int,
        sort: String
    ): Result<List<PlaceSearchResult>> = withContext(Dispatchers.IO) {
        runCatching {
            val response = locationApi.searchPlacesByCategory(
                category = category,
                x = centerLng.toString(),
                y = centerLat.toString(),
                radius = radius,
                size = size,
                page = page,
                sort = sort
            )
            
            // KakaoMapSearchResponse를 PlaceSearchResult 리스트로 변환
            response.documents.map { it.toPlaceSearchResult() }
        }.recoverCatching { exception ->
            throw LocationSearchException(
                message = "카테고리 검색 중 오류가 발생했습니다: ${exception.message}",
                cause = exception
            )
        }
    }
    
    override suspend fun searchAddress(
        address: String,
        analyzeType: String
    ): Result<List<PlaceSearchResult>> = withContext(Dispatchers.IO) {
        runCatching {
            val response = locationApi.searchAddress(
                query = address,
                analyzeType = analyzeType
            )
            
            // KakaoMapSearchResponse를 PlaceSearchResult 리스트로 변환
            response.documents.map { it.toPlaceSearchResult() }
        }.recoverCatching { exception ->
            throw LocationSearchException(
                message = "주소 검색 중 오류가 발생했습니다: ${exception.message}",
                cause = exception
            )
        }
    }
    
    override suspend fun getAddressFromCoordinates(
        lat: Double,
        lng: Double
    ): Result<List<PlaceSearchResult>> = withContext(Dispatchers.IO) {
        runCatching {
            val response = locationApi.getAddressFromCoordinates(
                x = lng.toString(),
                y = lat.toString()
            )
            
            // KakaoMapSearchResponse를 PlaceSearchResult 리스트로 변환
            response.documents.map { it.toPlaceSearchResult() }
        }.recoverCatching { exception ->
            throw LocationSearchException(
                message = "좌표 검색 중 오류가 발생했습니다: ${exception.message}",
                cause = exception
            )
        }
    }
    
    /**
     * 장소 검색 관련 커스텀 예외 클래스
     */
    class LocationSearchException(
        message: String,
        cause: Throwable? = null
    ) : Exception(message, cause)
}
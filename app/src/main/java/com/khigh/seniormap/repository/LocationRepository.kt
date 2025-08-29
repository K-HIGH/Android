// LocationRepository.kt
package com.khigh.seniormap.repository

import com.khigh.seniormap.model.dto.kakaomap.PlaceSearchResult

/**
 * 장소 검색 Repository 인터페이스
 * 다양한 위치 기반 검색 기능을 정의
 */
interface LocationRepository {
    
    /**
     * 키워드로 장소 검색
     * @param query 검색할 키워드
     * @param size 검색 결과 개수 (기본값: 10)
     * @param page 페이지 번호 (기본값: 1)
     * @param category 카테고리 그룹 코드 (선택사항)
     * @param centerLat 중심 위도 (선택사항)
     * @param centerLng 중심 경도 (선택사항)
     * @param radius 검색 반경 미터 (선택사항)
     * @param sort 정렬 방식 (기본값: accuracy)
     * @return Result<List<PlaceSearchResult>> 검색 결과 리스트 또는 에러
     */
    suspend fun searchPlacesByKeyword(
        query: String,
        size: Int = 10,
        page: Int = 1,
        category: String? = null,
        centerLat: Double? = null,
        centerLng: Double? = null,
        radius: Int? = null,
        sort: String = "accuracy"
    ): Result<List<PlaceSearchResult>>
    
    /**
     * 카테고리로 장소 검색
     * 특정 카테고리의 장소들을 중심점 기준으로 반경 내에서 검색
     * @param category 카테고리 그룹 코드 (예: MT1-대형마트, CS2-편의점 등)
     * @param centerLat 중심 위도
     * @param centerLng 중심 경도
     * @param radius 검색 반경 미터 (기본값: 500m)
     * @param size 검색 결과 개수 (기본값: 10)
     * @param page 페이지 번호 (기본값: 1)
     * @param sort 정렬 방식 (기본값: accuracy)
     * @return Result<List<PlaceSearchResult>> 검색 결과 리스트 또는 에러
     */
    suspend fun searchPlacesByCategory(
        category: String,
        centerLat: Double,
        centerLng: Double,
        radius: Int = 500,
        size: Int = 10,
        page: Int = 1,
        sort: String = "accuracy"
    ): Result<List<PlaceSearchResult>>
    
    /**
     * 주소로 좌표 검색 (지오코딩)
     * 주소 문자열을 위도, 경도 좌표로 변환
     * @param address 검색할 주소 (예: "서울특별시 강남구 테헤란로 152")
     * @param analyzeType 주소 분석 타입 (기본값: similar)
     *                   - similar: 비슷한 주소 포함
     *                   - exact: 정확한 주소만
     * @return Result<List<PlaceSearchResult>> 주소 검색 결과 리스트 또는 에러
     */
    suspend fun searchAddress(
        address: String,
        analyzeType: String = "similar"
    ): Result<List<PlaceSearchResult>>
    
    /**
     * 좌표로 주소 검색 (역지오코딩)
     * 위도, 경도 좌표를 주소 문자열로 변환
     * @param lat 위도 (latitude)
     * @param lng 경도 (longitude)
     * @return Result<List<PlaceSearchResult>> 좌표에 해당하는 주소 정보 리스트 또는 에러
     */
    suspend fun getAddressFromCoordinates(
        lat: Double,
        lng: Double
    ): Result<List<PlaceSearchResult>>
}
package com.khigh.seniormap.network.api

import com.khigh.seniormap.model.dto.kakaomap.KakaoMapSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * 카카오맵 장소 검색 API 인터페이스
 * 카카오맵 REST API를 통해 장소 검색 기능 제공
 * 
 * @see <a href="https://developers.kakao.com/docs/latest/ko/local/dev-guide#address-coord">카카오맵 로컬 API 문서</a>
 */
interface LocationApi {
    
    /**
     * 키워드로 장소 검색
     * @param query 검색할 키워드
     * @param size 검색 결과 개수 (최대 15개)
     * @param page 페이지 번호 (1부터 시작)
     * @param category 카테고리 그룹 코드 (선택사항)
     * @param x 중심 좌표 X (경도, 선택사항)
     * @param y 중심 좌표 Y (위도, 선택사항)
     * @param radius 검색 반경 (미터 단위, 선택사항)
     * @param sort 정렬 방식 (accuracy: 정확도순, distance: 거리순)
     * 
     * @see <a href="https://developers.kakao.com/docs/latest/ko/local/dev-guide#search-by-keyword">키워드로 장소 검색</a>
     */
    @GET("v2/local/search/keyword.json")
    suspend fun searchPlacesByKeyword(
        @Query("query") query: String,
        @Query("size") size: Int = 10,
        @Query("page") page: Int = 1,
        @Query("category_group_code") category: String? = null,
        @Query("x") x: String? = null,
        @Query("y") y: String? = null,
        @Query("radius") radius: Int? = null,
        @Query("sort") sort: String = "accuracy"
    ): KakaoMapSearchResponse
    
    /**
     * 카테고리로 장소 검색
     * @param category 카테고리 그룹 코드
     * @param x 중심 좌표 X (경도)
     * @param y 중심 좌표 Y (위도)
     * @param radius 검색 반경 (미터 단위)
     * @param size 검색 결과 개수 (최대 15개)
     * @param page 페이지 번호 (1부터 시작)
     * @param sort 정렬 방식 (accuracy: 정확도순, distance: 거리순)
     * 
     * @see <a href="https://developers.kakao.com/docs/latest/ko/local/dev-guide#search-by-category">카테고리로 장소 검색</a>
     */
    @GET("v2/local/search/category.json")
    suspend fun searchPlacesByCategory(
        @Query("category_group_code") category: String,
        @Query("x") x: String,
        @Query("y") y: String,
        @Query("radius") radius: Int = 500,
        @Query("size") size: Int = 10,
        @Query("page") page: Int = 1,
        @Query("sort") sort: String = "accuracy"
    ): KakaoMapSearchResponse
    
    /**
     * 주소로 좌표 검색 (지오코딩)
     * @param query 검색할 주소
     * @param analyze_type 주소 분석 타입 (similar: 유사도순, exact: 정확도순)
     * 
     * @see <a href="https://developers.kakao.com/docs/latest/ko/local/dev-guide#address-coord">주소로 좌표 변환</a>
     */
    @GET("v2/local/search/address.json")
    suspend fun searchAddress(
        @Query("query") query: String,
        @Query("analyze_type") analyzeType: String = "similar"
    ): KakaoMapSearchResponse
    
    /**
     * 좌표로 주소 검색 (역지오코딩)
     * @param x 경도
     * @param y 위도
     * 
     * @see <a href="https://developers.kakao.com/docs/latest/ko/local/dev-guide#coord-address">좌표로 주소 변환</a>
     */
    @GET("v2/local/geo/coord2address.json")
    suspend fun getAddressFromCoordinates(
        @Query("x") x: String,
        @Query("y") y: String
    ): KakaoMapSearchResponse
}
package com.khigh.seniormap.network.api

import com.khigh.seniormap.model.dto.kakaomap.KakaoMapAddressRequest
import com.khigh.seniormap.model.dto.kakaomap.KakaoMapAddressResponse
import com.khigh.seniormap.model.dto.kakaomap.KakaoMapCategoryRequest
import com.khigh.seniormap.model.dto.kakaomap.KakaoMapSearchRequest
import com.khigh.seniormap.model.dto.kakaomap.KakaoMapSearchResponse
import retrofit2.Response
import retrofit2.http.*

interface KakaoMapApi {
    
    /**
     * 키워드로 장소 검색
     */
    @GET("v2/local/search/keyword.json")
    suspend fun searchPlaces(
        @Header("Authorization") apiKey: String,
        @Query("query") query: String,
        @Query("category_group_code") categoryGroupCode: String? = null,
        @Query("x") x: String? = null,
        @Query("y") y: String? = null,
        @Query("radius") radius: Int? = null,
        @Query("rect") rect: String? = null,
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null,
        @Query("sort") sort: String? = null
    ): Response<KakaoMapSearchResponse>
    
    /**
     * 주소로 장소 검색
     */
    @GET("v2/local/search/address.json")
    suspend fun searchAddresses(
        @Header("Authorization") apiKey: String,
        @Query("query") query: String,
        @Query("analyze_type") analyzeType: String? = null,
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null
    ): Response<KakaoMapAddressResponse>
    
    /**
     * 카테고리로 장소 검색
     */
    @GET("v2/local/search/category.json")
    suspend fun searchByCategory(
        @Header("Authorization") apiKey: String,
        @Query("category_group_code") categoryGroupCode: String,
        @Query("x") x: String,
        @Query("y") y: String,
        @Query("radius") radius: Int? = null,
        @Query("rect") rect: String? = null,
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null,
        @Query("sort") sort: String? = null
    ): Response<KakaoMapSearchResponse>
    
    /**
     * 좌표로 주소 변환
     */
    @GET("v2/local/geo/coord2address.json")
    suspend fun getAddressFromCoordinates(
        @Header("Authorization") apiKey: String,
        @Query("x") x: String,
        @Query("y") y: String,
        @Query("input_coord") inputCoord: String = "WGS84"
    ): Response<KakaoMapAddressResponse>
    
    /**
     * 주소로 좌표 변환
     */
    @GET("v2/local/geo/coord2regioncode.json")
    suspend fun getCoordinatesFromAddress(
        @Header("Authorization") apiKey: String,
        @Query("x") x: String,
        @Query("y") y: String,
        @Query("input_coord") inputCoord: String = "WGS84"
    ): Response<KakaoMapAddressResponse>
} 
package com.khigh.seniormap.network.api

import com.khigh.seniormap.model.dto.route.RouteRequest
import com.khigh.seniormap.model.dto.route.RouteResponse
import retrofit2.Response
import retrofit2.http.*

/**
 * 경로 관련 API 인터페이스
 * K-HIGH 백엔드 API의 경로 탐색 및 히스토리 기능을 제공
 * 
 * @see <a href="http://121.157.24.40:63001/docs">K-HIGH Backend API 문서</a>
 */
interface RouteApi {
    
    /**
     * 경로 탐색
     * POST /api/v1/locations/routes/
     * 
     * @param request 경로 탐색 요청 정보
     * @return RouteResponse 경로 탐색 결과
     */
    @POST("api/v1/locations/routes/")
    suspend fun findRoute(
        @Header("Authorization") token: String,
        @Body request: RouteRequest
    ): Response<RouteResponse>
    
    /**
     * 경로 히스토리 조회
     * GET /api/v1/locations/routes/
     * 
     * @return List<RouteResponse> 경로 히스토리 목록
     */
    @GET("api/v1/locations/routes/")
    suspend fun getRouteHistory(
        @Header("Authorization") token: String
    ): Response<List<RouteResponse>>
    
    /**
     * 특정 경로 히스토리 조회
     * GET /api/v1/locations/routes/{route_id}
     * 
     * @param routeId 경로 ID
     * @return RouteResponse 경로 상세 정보
     */
    @GET("api/v1/locations/routes/{route_id}")
    suspend fun getRouteHistoryById(
        @Header("Authorization") token: String,
        @Path("route_id") routeId: String
    ): Response<RouteResponse>
    
    /**
     * 경로 히스토리 삭제
     * DELETE /api/v1/locations/routes/{route_id}
     * 
     * @param routeId 경로 ID
     */
    @DELETE("api/v1/locations/routes/{route_id}")
    suspend fun deleteRouteHistory(
        @Header("Authorization") token: String,
        @Path("route_id") routeId: String
    ): Response<Unit>
} 
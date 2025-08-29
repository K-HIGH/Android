package com.khigh.seniormap.repository

import com.khigh.seniormap.model.dto.route.RouteRequest
import com.khigh.seniormap.model.dto.route.RouteResponse

/**
 * 경로 탐색 Repository 인터페이스
 * K-HIGH 백엔드 API를 통해 경로 탐색 기능을 제공
 * 
 * @see <a href="http://121.157.24.40:63001/docs">K-HIGH Backend API 문서</a>
 */
interface RouteRepository {
    
    /**
     * 경로 탐색
     * 출발지와 도착지 좌표를 기반으로 최적 경로를 찾습니다
     * 
     * @param request 경로 탐색 요청 정보
     * @return Result<RouteResponse> 경로 탐색 결과 또는 에러
     */
    suspend fun findRoute(request: RouteRequest): Result<RouteResponse>
    
    /**
     * 경로 히스토리 조회
     * 사용자의 이전 경로 탐색 기록을 조회합니다
     * 
     * @param userId 사용자 ID
     * @return Result<List<RouteResponse>> 경로 히스토리 목록 또는 에러
     */
    suspend fun getRouteHistory(userId: String): Result<List<RouteResponse>>
    
    /**
     * 특정 경로 히스토리 조회
     * 특정 경로 ID에 해당하는 상세 정보를 조회합니다
     * 
     * @param routeId 경로 ID
     * @return Result<RouteResponse> 경로 상세 정보 또는 에러
     */
    suspend fun getRouteHistoryById(routeId: String): Result<RouteResponse>
    
    /**
     * 경로 히스토리 삭제
     * 특정 경로 ID에 해당하는 기록을 삭제합니다
     * 
     * @param routeId 경로 ID
     * @return Result<Boolean> 삭제 성공 여부 또는 에러
     */
    suspend fun deleteRouteHistory(routeId: String): Result<Boolean>
} 
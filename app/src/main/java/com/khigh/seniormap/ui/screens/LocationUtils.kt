package com.khigh.seniormap.ui.screens

import kotlin.math.*

/**
 * 위치 관련 유틸리티 클래스
 * 
 * 지리적 좌표 간의 거리 계산과 위치 상태 판단을 담당합니다.
 */
object LocationUtils {
    
    /**
     * 두 지점 간의 거리를 미터 단위로 계산합니다.
     * Haversine 공식을 사용하여 지구의 곡률을 고려합니다.
     * 
     * @param lat1 첫 번째 지점의 위도 (도)
     * @param lon1 첫 번째 지점의 경도 (도)
     * @param lat2 두 번째 지점의 위도 (도)
     * @param lon2 두 번째 지점의 경도 (도)
     * @return 두 지점 간의 거리 (미터)
     */
    fun calculateDistance(
        lat1: Double, lon1: Double,
        lat2: Double, lon2: Double
    ): Double {
        val earthRadius = 6371000.0 // 지구 반지름 (미터)
        
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        
        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2) * sin(dLon / 2)
        
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        
        return earthRadius * c
    }
    
    /**
     * 현재 위치가 집에 있는지 여부를 판단합니다.
     * 
     * @param homeLat 집의 위도 (도)
     * @param homeLon 집의 경도 (도)
     * @param currentLat 현재 위치의 위도 (도)
     * @param currentLon 현재 위치의 경도 (도)
     * @param threshold 집으로 간주하는 거리 임계값 (미터, 기본값: 100m)
     * @return 집에 있으면 true, 외출 중이면 false
     */
    fun isAtHome(
        homeLat: Double, homeLon: Double,
        currentLat: Double, currentLon: Double,
        threshold: Double = 100.0
    ): Boolean {
        val distance = calculateDistance(homeLat, homeLon, currentLat, currentLon)
        return distance <= threshold
    }
    
    /**
     * 거리를 사람이 읽기 쉬운 형태로 변환합니다.
     * 
     * @param distanceMeters 거리 (미터)
     * @return 포맷된 거리 문자열
     */
    fun formatDistance(distanceMeters: Double): String {
        return when {
            distanceMeters < 1000 -> "${distanceMeters.toInt()}m"
            else -> "${(distanceMeters / 1000).roundToInt()}km"
        }
    }
    
    /**
     * 위도와 경도가 유효한지 확인합니다.
     * 
     * @param latitude 위도
     * @param longitude 경도
     * @return 유효한 좌표이면 true
     */
    fun isValidCoordinate(latitude: Double, longitude: Double): Boolean {
        return latitude in -90.0..90.0 && longitude in -180.0..180.0
    }
}

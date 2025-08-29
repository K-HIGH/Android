package com.khigh.seniormap.ui.screens.senior.components

/**
 * 즐겨찾기 경로 데이터
 */
data class FavoriteRoute(
    val id: String,
    val name: String,
    val startLocation: String,
    val endLocation: String,
    val description: String = "${startLocation} → ${endLocation}"
)

/**
 * 보호인 정보 데이터
 */
data class CaregiverData(
    val id: String,
    val name: String,
    val phoneNumber: String,
    val isApproved: Boolean = true
)

/**
 * 위치 검색 결과 데이터
 */
data class LocationSearchResult(
    val id: String,
    val name: String,
    val address: String,
    val distance: String? = null
)

/**
 * 현재 위치 정보
 */
data class CurrentLocation(
    val latitude: Double,
    val longitude: Double,
    val address: String = "현위치"
)

/**
 * Mock 데이터 제공자
 */
object SeniorMockData {
    
    val favoriteRoutes = listOf(
        FavoriteRoute(
            id = "1",
            name = "병원 가는 길",
            startLocation = "우리집",
            endLocation = "서울대학교병원"
        ),
        FavoriteRoute(
            id = "2", 
            name = "마트 가는 길",
            startLocation = "하나로마트성수점",
            endLocation = "우리집"
        ),
        FavoriteRoute(
            id = "3",
            name = "교회 가는 길", 
            startLocation = "교회",
            endLocation = "서울대학교병원"
        )
    )
    
    val caregivers = listOf(
        CaregiverData(
            id = "1",
            name = "김보호",
            phoneNumber = "010-1234-5678",
            isApproved = false
        ),
        CaregiverData(
            id = "2",
            name = "이돌봄",
            phoneNumber = "010-9876-5432",
            isApproved = true
        ),
        CaregiverData(
            id = "3",
            name = "박케어",
            phoneNumber = "010-5555-7777",
            isApproved = true
        )
    )
    
    val locationSearchResults = listOf(
        LocationSearchResult(
            id = "1",
            name = "서울대학교병원",
            address = "서울특별시 종로구 대학로 101",
            distance = "2.3km"
        ),
        LocationSearchResult(
            id = "2",
            name = "하나로마트 성수점",
            address = "서울특별시 성동구 성수동1가 656-1",
            distance = "800m"
        ),
        LocationSearchResult(
            id = "3",
            name = "성수교회",
            address = "서울특별시 성동구 성수동2가 289-1",
            distance = "1.2km"
        ),
        LocationSearchResult(
            id = "4",
            name = "성수역",
            address = "서울특별시 성동구 성수동1가",
            distance = "1.5km"
        )
    )
    
    val currentLocation = CurrentLocation(
        latitude = 37.5665,
        longitude = 126.9780,
        address = "현위치"
    )
} 
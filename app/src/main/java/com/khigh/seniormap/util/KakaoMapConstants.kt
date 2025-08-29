package com.khigh.seniormap.util

object KakaoMapConstants {
    
    // 카테고리 그룹 코드
    object CategoryGroupCode {
        const val RESTAURANT = "FD6"           // 음식점
        const val CAFE = "CE7"                 // 카페
        const val CONVENIENCE_STORE = "CS2"    // 편의점
        const val PHARMACY = "PM9"             // 약국
        const val HOSPITAL = "HP8"             // 병원
        const val BANK = "BK9"                 // 은행
        const val GAS_STATION = "OL7"          // 주유소
        const val SUBWAY = "SW8"               // 지하철역
        const val BUS_STOP = "BL7"             // 버스정류장
        const val PARKING = "PK6"              // 주차장
        const val TOILET = "WC8"               // 화장실
        const val PHONE = "PS3"                // 공중전화
        const val ATM = "AT4"                  // ATM
        const val POST_OFFICE = "PO3"          // 우체국
        const val POLICE = "PS4"               // 경찰서
        const val FIRE_STATION = "PS5"         // 소방서
        const val SCHOOL = "SC4"               // 학교
        const val LIBRARY = "LB7"              // 도서관
        const val MUSEUM = "CT1"               // 문화시설
        const val THEATER = "CT2"              // 공연장
        const val HOTEL = "AD5"                // 숙박
        const val SHOPPING = "MT1"             // 쇼핑
        const val SUPERMARKET = "MT1"          // 대형마트
        const val DEPARTMENT_STORE = "MT1"     // 백화점
        const val MARKET = "MT1"               // 전통시장
    }
    
    // 정렬 옵션
    object SortOption {
        const val ACCURACY = "accuracy"        // 정확도순
        const val DISTANCE = "distance"        // 거리순
    }
    
    // 좌표계
    object CoordinateSystem {
        const val WGS84 = "WGS84"              // WGS84
        const val TM = "TM"                    // TM
        const val KTM = "KTM"                  // KTM
        const val UTM = "UTM"                  // UTM
        const val BESSEL = "BESSEL"            // BESSEL
        const val WKTM = "WKTM"                // WKTM
    }
    
    // 주소 분석 타입
    object AnalyzeType {
        const val SIMILAR = "similar"          // 유사도순
        const val EXACT = "exact"              // 정확도순
    }
    
    // 기본 설정값
    object Default {
        const val SEARCH_RADIUS = 1000         // 기본 검색 반경 (미터)
        const val SEARCH_SIZE = 15             // 기본 검색 결과 수
        const val SEARCH_PAGE = 1              // 기본 페이지 번호
        const val CACHE_EXPIRY_HOURS = 24L    // 캐시 만료 시간 (시간)
    }
} 
package com.khigh.seniormap.model.dto.route

// 데이터 클래스들
data class RouteInfo(
    val distance: String,
    val duration: String,
    val steps: List<RouteStep>
)

data class RouteStep(
    val order: Int,
    val instruction: String,
    val distance: String,
    val duration: String
)

enum class RouteOption {
    CAR,           // 자동차
    WALK,          // 도보
    PUBLIC_TRANSPORT // 대중교통
}


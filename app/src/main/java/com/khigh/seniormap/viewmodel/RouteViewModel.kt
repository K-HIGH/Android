package com.khigh.seniormap.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kakao.vectormap.LatLng
import com.khigh.seniormap.model.dto.kakaomap.PlaceSearchResult
import com.khigh.seniormap.model.dto.route.TMapCoordinate
import com.khigh.seniormap.model.dto.route.RouteRequest
import com.khigh.seniormap.model.dto.route.RouteResponse
import com.khigh.seniormap.model.dto.route.RouteOption
import com.khigh.seniormap.model.dto.route.RouteInfo
import com.khigh.seniormap.model.dto.route.RouteDetail
import com.khigh.seniormap.repository.RouteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 경로 탐색 ViewModel
 * K-HIGH 백엔드 API를 통해 경로 탐색 기능을 제공
 * 
 * @see <a href="http://121.157.24.40:63001/docs">K-HIGH Backend API 문서</a>
 */
@HiltViewModel
class RouteViewModel @Inject constructor(
    private val routeRepository: RouteRepository
) : ViewModel() {

    // 경로 탐색 상태
    private val _routeSearchState = MutableStateFlow<Result<RouteResponse?>>(Result.success(null))
    val routeSearchState: StateFlow<Result<RouteResponse?>> = _routeSearchState.asStateFlow()

    // 경로 옵션 상태
    private val _selectedRouteOption = MutableStateFlow(RouteOption.CAR)
    val selectedRouteOption: StateFlow<RouteOption> = _selectedRouteOption.asStateFlow()

    // 경로 계산 중 상태
    private val _isCalculating = MutableStateFlow(false)
    val isCalculating: StateFlow<Boolean> = _isCalculating.asStateFlow()

    // 선택된 경로 인덱스
    private val _selectedRouteIndex = MutableStateFlow(0)
    val selectedRouteIndex: StateFlow<Int> = _selectedRouteIndex.asStateFlow()

    // 출발지와 도착지
    private val _startLocation = MutableStateFlow<PlaceSearchResult?>(null)
    val startLocation: StateFlow<PlaceSearchResult?> = _startLocation.asStateFlow()

    private val _endLocation = MutableStateFlow<PlaceSearchResult?>(null)
    val endLocation: StateFlow<PlaceSearchResult?> = _endLocation.asStateFlow()

    /**
     * 출발지 설정
     * @param location 출발지 정보
     */
    fun setStartLocation(location: PlaceSearchResult) {
        _startLocation.value = location
        // 출발지가 변경되면 기존 경로 정보 초기화
        _routeSearchState.value = Result.success(null)
        _selectedRouteIndex.value = 0
    }

    /**
     * 도착지 설정
     * @param location 도착지 정보
     */
    fun setEndLocation(location: PlaceSearchResult) {
        _endLocation.value = location
        // 도착지가 변경되면 기존 경로 정보 초기화
        _routeSearchState.value = Result.success(null)
        _selectedRouteIndex.value = 0
    }

    /**
     * 경로 옵션 변경
     * @param option 새로운 경로 옵션
     */
    fun setRouteOption(option: RouteOption) {
        _selectedRouteOption.value = option
        // 경로 옵션이 변경되면 경로 재계산
        if (_startLocation.value != null && _endLocation.value != null) {
            searchRoute()
        }
    }

    /**
     * 선택된 경로 인덱스 변경
     * @param index 새로운 경로 인덱스
     */
    fun setSelectedRouteIndex(index: Int) {
        _selectedRouteIndex.value = index
    }

    /**
     * 경로 탐색 실행
     * 출발지와 도착지가 모두 설정되어 있을 때만 실행
     */
    fun searchRoute() {
        val start = _startLocation.value
        val end = _endLocation.value

        if (start == null || end == null) {
            _routeSearchState.value = Result.failure(Exception("출발지와 도착지를 모두 설정해주세요."))
            return
        }

        _isCalculating.value = true
        _routeSearchState.value = Result.success(null) // 로딩 상태는 별도로 관리

        viewModelScope.launch {
            try {
                val routeRequest = RouteRequest(
                    src = TMapCoordinate(
                        lon = start.latLng.longitude,
                        lat = start.latLng.latitude
                    ),
                    dst = TMapCoordinate(
                        lon = end.latLng.longitude,
                        lat = end.latLng.latitude
                    ),
                    count = 3 // 기본적으로 3개의 경로 옵션 제공
                )

                val result = routeRepository.findRoute(routeRequest)
                _routeSearchState.value = result
                
                if (result.isSuccess) {
                    _selectedRouteIndex.value = 0 // 첫 번째 경로를 기본 선택
                }
            } catch (e: Exception) {
                _routeSearchState.value = Result.failure(Exception("경로 탐색 중 오류가 발생했습니다: ${e.message}"))
            } finally {
                _isCalculating.value = false
            }
        }
    }

    /**
     * 현재 선택된 경로 정보 반환
     * @return 선택된 경로 정보 또는 null
     */
    fun getSelectedRoute(): RouteDetail? {
        val routeResponse = _routeSearchState.value.getOrNull() ?: return null
        val routes = routeResponse.routes ?: return null
        val selectedIndex = _selectedRouteIndex.value
        
        return if (selectedIndex < routes.size) {
            routes[selectedIndex]
        } else {
            null
        }
    }

    /**
     * 경로 탐색 가능 여부 확인
     * @return 출발지와 도착지가 모두 설정되어 있으면 true
     */
    fun canSearchRoute(): Boolean {
        return _startLocation.value != null && _endLocation.value != null
    }

    /**
     * 경로 정보 초기화
     */
    fun clearRoute() {
        _routeSearchState.value = Result.success(null)
        _selectedRouteIndex.value = 0
        _isCalculating.value = false
    }

    /**
     * 출발지와 도착지 초기화
     */
    fun clearLocations() {
        _startLocation.value = null
        _endLocation.value = null
        clearRoute()
    }

    /**
     * 출발지와 도착지 위치 교환
     */
    fun swapLocations() {
        val temp = _startLocation.value
        _startLocation.value = _endLocation.value
        _endLocation.value = temp
        
        // 위치가 변경되면 경로 재계산
        if (_startLocation.value != null && _endLocation.value != null) {
            searchRoute()
        }
    }

    /**
     * 현재 위치를 출발지로 설정
     * @param lat 위도
     * @param lng 경도
     * @param address 주소 (선택사항)
     */
    fun setCurrentLocationAsStart(lat: Double, lng: Double, address: String? = null) {
        val currentLocation = PlaceSearchResult(
            id = "current_location_${System.currentTimeMillis()}",
            name = "현재 위치",
            address = address ?: "현재 위치 (${"%.6f".format(lat)}, ${"%.6f".format(lng)})",
            roadAddress = address,
            latLng = LatLng.from(lat, lng),
            category = "현재위치",
            distance = null,
            phone = null,
            categoryGroup = "현재위치",
            placeUrl = null
        )
        setStartLocation(currentLocation)
    }

    /**
     * 현재 위치를 도착지로 설정
     * @param lat 위도
     * @param lng 경도
     * @param address 주소 (선택사항)
     */
    fun setCurrentLocationAsEnd(lat: Double, lng: Double, address: String? = null) {
        val currentLocation = PlaceSearchResult(
            id = "current_location_${System.currentTimeMillis()}",
            name = "현재 위치",
            address = address ?: "현재 위치 (${"%.6f".format(lat)}, ${"%.6f".format(lng)})",
            roadAddress = address,
            latLng = LatLng.from(lat, lng),
            category = "현재위치",
            distance = null,
            phone = null,
            categoryGroup = "현재위치",
            placeUrl = null
        )
        setEndLocation(currentLocation)
    }
}

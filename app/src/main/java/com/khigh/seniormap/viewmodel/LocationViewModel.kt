package com.khigh.seniormap.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khigh.seniormap.model.dto.kakaomap.PlaceSearchResult
import com.khigh.seniormap.repository.LocationRepository
import com.kakao.vectormap.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 장소 검색 ViewModel
 * 장소 검색 관련 상태 관리 및 비즈니스 로직 처리
 */
@HiltViewModel
class LocationViewModel @Inject constructor(
    private val locationRepository: LocationRepository
) : ViewModel() {

    // 검색 상태 관리
    private val _startLocationSearchState = MutableStateFlow<Result<List<PlaceSearchResult>>>(
        Result.success(emptyList())
    )
    val startLocationSearchState: StateFlow<Result<List<PlaceSearchResult>>> = _startLocationSearchState.asStateFlow()

    private val _endLocationSearchState = MutableStateFlow<Result<List<PlaceSearchResult>>>(
        Result.success(emptyList())
    )
    val endLocationSearchState: StateFlow<Result<List<PlaceSearchResult>>> = _endLocationSearchState.asStateFlow()

    // 선택된 위치 상태
    private val _selectedStartLocation = MutableStateFlow<PlaceSearchResult?>(null)
    val selectedStartLocation: StateFlow<PlaceSearchResult?> = _selectedStartLocation.asStateFlow()

    private val _selectedEndLocation = MutableStateFlow<PlaceSearchResult?>(null)
    val selectedEndLocation: StateFlow<PlaceSearchResult?> = _selectedEndLocation.asStateFlow()

    // 검색 쿼리 상태
    private val _startLocationQuery = MutableStateFlow("")
    val startLocationQuery: StateFlow<String> = _startLocationQuery.asStateFlow()

    private val _endLocationQuery = MutableStateFlow("")
    val endLocationQuery: StateFlow<String> = _endLocationQuery.asStateFlow()

    // UI 표시 상태
    private val _showStartSearchResults = MutableStateFlow(false)
    val showStartSearchResults: StateFlow<Boolean> = _showStartSearchResults.asStateFlow()

    private val _showEndSearchResults = MutableStateFlow(false)
    val showEndSearchResults: StateFlow<Boolean> = _showEndSearchResults.asStateFlow()

    private val _isSearchLoading = MutableStateFlow(false)
    val isSearchLoading: StateFlow<Boolean> = _isSearchLoading.asStateFlow()

    // 검색 Job 관리 (디바운싱을 위함)
    private var startSearchJob: Job? = null
    private var endSearchJob: Job? = null

    companion object {
        private const val SEARCH_DELAY_MS = 500L // 검색 디바운싱 딜레이
        private const val MIN_QUERY_LENGTH = 2 // 최소 검색어 길이
    }

    /**
     * 출발지 검색
     * 디바운싱을 적용하여 사용자 입력이 완료된 후 검색 실행
     * @param query 검색할 키워드
     * @param centerLat 중심 위도 (선택사항)
     * @param centerLng 중심 경도 (선택사항)
     */
    fun searchStartLocation(
        query: String,
        centerLat: Double? = null,
        centerLng: Double? = null
    ) {
        // 검색어 상태 업데이트
        _startLocationQuery.value = query

        // 기존 검색 작업 취소
        startSearchJob?.cancel()

        // 검색어가 비어있으면 결과 초기화
        if (query.isEmpty()) {
            resetStartSearchState()
            return
        }

        // 검색어가 너무 짧으면 검색하지 않음
        if (query.length < MIN_QUERY_LENGTH) {
            _showStartSearchResults.value = false
            return
        }

        // 디바운싱 적용하여 검색 실행
        startSearchJob = viewModelScope.launch {
            delay(SEARCH_DELAY_MS)
            executeStartLocationSearch(query, centerLat, centerLng)
        }
    }

    /**
     * 도착지 검색
     * 디바운싱을 적용하여 사용자 입력이 완료된 후 검색 실행
     * @param query 검색할 키워드
     * @param centerLat 중심 위도 (선택사항)
     * @param centerLng 중심 경도 (선택사항)
     */
    fun searchEndLocation(
        query: String,
        centerLat: Double? = null,
        centerLng: Double? = null
    ) {
        // 검색어 상태 업데이트
        _endLocationQuery.value = query

        // 기존 검색 작업 취소
        endSearchJob?.cancel()

        // 검색어가 비어있으면 결과 초기화
        if (query.isEmpty()) {
            resetEndSearchState()
            return
        }

        // 검색어가 너무 짧으면 검색하지 않음
        if (query.length < MIN_QUERY_LENGTH) {
            _showEndSearchResults.value = false
            return
        }

        // 디바운싱 적용하여 검색 실행
        endSearchJob = viewModelScope.launch {
            delay(SEARCH_DELAY_MS)
            executeEndLocationSearch(query, centerLat, centerLng)
        }
    }

    /**
     * 실제 출발지 검색 실행
     */
    private suspend fun executeStartLocationSearch(
        query: String,
        centerLat: Double? = null,
        centerLng: Double? = null
    ) {
        _isSearchLoading.value = true
        _showStartSearchResults.value = true

        try {
            val result = locationRepository.searchPlacesByKeyword(
                query = query,
                size = 15, // 더 많은 결과 제공
                centerLat = centerLat,
                centerLng = centerLng,
                sort = "accuracy"
            )

            _startLocationSearchState.value = result

        } catch (e: Exception) {
            _startLocationSearchState.value = Result.failure(e)
        } finally {
            _isSearchLoading.value = false
        }
    }

    /**
     * 실제 도착지 검색 실행
     */
    private suspend fun executeEndLocationSearch(
        query: String,
        centerLat: Double? = null,
        centerLng: Double? = null
    ) {
        _isSearchLoading.value = true
        _showEndSearchResults.value = true

        try {
            val result = locationRepository.searchPlacesByKeyword(
                query = query,
                size = 15, // 더 많은 결과 제공
                centerLat = centerLat,
                centerLng = centerLng,
                sort = "accuracy"
            )

            _endLocationSearchState.value = result

        } catch (e: Exception) {
            _endLocationSearchState.value = Result.failure(e)
        } finally {
            _isSearchLoading.value = false
        }
    }

    /**
     * 카테고리로 주변 장소 검색
     * @param category 카테고리 코드 (예: "MT1", "CS2" 등)
     * @param centerLat 중심 위도
     * @param centerLng 중심 경도
     * @param radius 검색 반경 (기본값: 1000m)
     */
    fun searchNearbyPlaces(
        category: String,
        centerLat: Double,
        centerLng: Double,
        radius: Int = 1000
    ) {
        viewModelScope.launch {
            _isSearchLoading.value = true

            try {
                val result = locationRepository.searchPlacesByCategory(
                    category = category,
                    centerLat = centerLat,
                    centerLng = centerLng,
                    radius = radius,
                    size = 20
                )

                // 결과를 출발지 검색 결과로 표시
                _startLocationSearchState.value = result
                _showStartSearchResults.value = true

            } catch (e: Exception) {
                _startLocationSearchState.value = Result.failure(e)
            } finally {
                _isSearchLoading.value = false
            }
        }
    }

    /**
     * 출발지 선택
     * 선택 후 검색 결과를 숨기고 선택된 위치 정보를 업데이트
     * @param location 선택된 장소
     */
    fun selectStartLocation(location: PlaceSearchResult) {
        _selectedStartLocation.value = location
        _startLocationQuery.value = location.name
        _showStartSearchResults.value = false
        _startLocationSearchState.value = Result.success(emptyList())

        // 검색 작업 취소
        startSearchJob?.cancel()
    }

    /**
     * 도착지 선택
     * 선택 후 검색 결과를 숨기고 선택된 위치 정보를 업데이트
     * @param location 선택된 장소
     */
    fun selectEndLocation(location: PlaceSearchResult) {
        _selectedEndLocation.value = location
        _endLocationQuery.value = location.name
        _showEndSearchResults.value = false
        _endLocationSearchState.value = Result.success(emptyList())

        // 검색 작업 취소
        endSearchJob?.cancel()
    }

    /**
     * 출발지 정보 초기화
     * 선택된 위치, 검색어, 검색 결과를 모두 초기화
     */
    fun clearStartLocation() {
        _selectedStartLocation.value = null
        _startLocationQuery.value = ""
        resetStartSearchState()
        startSearchJob?.cancel()
    }

    /**
     * 도착지 정보 초기화
     * 선택된 위치, 검색어, 검색 결과를 모두 초기화
     */
    fun clearEndLocation() {
        _selectedEndLocation.value = null
        _endLocationQuery.value = ""
        resetEndSearchState()
        endSearchJob?.cancel()
    }

    /**
     * 출발지와 도착지 위치 교환
     * 두 위치가 모두 선택되어 있을 때만 실행
     */
    fun swapLocations() {
        val currentStart = _selectedStartLocation.value
        val currentEnd = _selectedEndLocation.value

        // 두 위치가 모두 선택되어 있을 때만 교환
        if (currentStart != null && currentEnd != null) {
            _selectedStartLocation.value = currentEnd
            _selectedEndLocation.value = currentStart

            _startLocationQuery.value = currentEnd.name
            _endLocationQuery.value = currentStart.name

            // 검색 결과 초기화
            resetStartSearchState()
            resetEndSearchState()
        }
    }

    /**
     * 경로 검색 가능 여부 확인
     * 출발지와 도착지가 모두 선택되었을 때 true 반환
     * @return 경로 검색 가능 여부
     */
    fun canSearchRoute(): Boolean {
        return _selectedStartLocation.value != null && _selectedEndLocation.value != null
    }

    /**
     * 현재 위치를 출발지로 설정
     * GPS에서 가져온 현재 위치를 출발지로 설정
     * @param lat 위도
     * @param lng 경도
     * @param address 현재 위치의 주소 (선택사항)
     */
    fun setCurrentLocationAsStart(lat: Double, lng: Double, address: String? = null) {
        val currentLocation = createCurrentLocationResult(lat, lng, address)
        selectStartLocation(currentLocation)
    }

    /**
     * 현재 위치를 도착지로 설정
     * GPS에서 가져온 현재 위치를 도착지로 설정
     * @param lat 위도
     * @param lng 경도
     * @param address 현재 위치의 주소 (선택사항)
     */
    fun setCurrentLocationAsEnd(lat: Double, lng: Double, address: String? = null) {
        val currentLocation = createCurrentLocationResult(lat, lng, address)
        selectEndLocation(currentLocation)
    }

    /**
     * 좌표로부터 주소 검색 (역지오코딩)
     * @param lat 위도
     * @param lng 경도
     * @param onResult 결과 콜백
     */
    fun getAddressFromCoordinates(
        lat: Double,
        lng: Double,
        onResult: (PlaceSearchResult?) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val result = locationRepository.getAddressFromCoordinates(lat, lng)
                result.fold(
                    onSuccess = { addresses ->
                        onResult(addresses.firstOrNull())
                    },
                    onFailure = {
                        onResult(null)
                    }
                )
            } catch (e: Exception) {
                onResult(null)
            }
        }
    }

    /**
     * 주소로 좌표 검색 (지오코딩)
     * @param address 주소 문자열
     * @param onResult 결과 콜백
     */
    fun searchAddressCoordinates(
        address: String,
        onResult: (PlaceSearchResult?) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val result = locationRepository.searchAddress(address)
                result.fold(
                    onSuccess = { places ->
                        onResult(places.firstOrNull())
                    },
                    onFailure = {
                        onResult(null)
                    }
                )
            } catch (e: Exception) {
                onResult(null)
            }
        }
    }

    // Private helper methods

    /**
     * 출발지 검색 상태 초기화
     */
    private fun resetStartSearchState() {
        _showStartSearchResults.value = false
        _startLocationSearchState.value = Result.success(emptyList())
    }

    /**
     * 도착지 검색 상태 초기화
     */
    private fun resetEndSearchState() {
        _showEndSearchResults.value = false
        _endLocationSearchState.value = Result.success(emptyList())
    }

    /**
     * 현재 위치 PlaceSearchResult 객체 생성
     */
    private fun createCurrentLocationResult(
        lat: Double,
        lng: Double,
        address: String?
    ): PlaceSearchResult {
        return PlaceSearchResult(
            id = "current_location_${System.currentTimeMillis()}",
            name = "현재 위치",
            address = address ?: "현재 위치 (${"%.6f".format(lat)}, ${"%.6f".format(lng)})",
            roadAddress = address,
            latLng = LatLng.from(lat, lng),
            category = "현재위치"
        )
    }

    /**
     * ViewModel이 소멸될 때 진행 중인 검색 작업 취소
     */
    override fun onCleared() {
        super.onCleared()
        startSearchJob?.cancel()
        endSearchJob?.cancel()
    }
}
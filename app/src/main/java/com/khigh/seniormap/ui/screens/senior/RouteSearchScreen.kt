package com.khigh.seniormap.ui.screens.senior

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.LatLngBounds
import com.kakao.vectormap.camera.CameraPosition
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.Label
import com.kakao.vectormap.label.LabelManager
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles
import com.kakao.vectormap.route.RouteLineManager
import com.kakao.vectormap.route.RouteLineOptions
import com.kakao.vectormap.route.RouteLineSegment
import com.kakao.vectormap.route.RouteLineStyle
import com.kakao.vectormap.route.RouteLineStyles
import com.khigh.seniormap.model.dto.kakaomap.PlaceSearchResult
import com.khigh.seniormap.ui.screens.senior.components.KakaoMapComponent
import com.khigh.seniormap.R
import com.khigh.seniormap.viewmodel.RouteViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import android.util.Log
import com.khigh.seniormap.model.dto.route.RouteInfo
import com.khigh.seniormap.model.dto.route.RouteLocation
import com.khigh.seniormap.model.dto.route.RouteOption
import com.khigh.seniormap.model.dto.route.RouteRequest
import com.khigh.seniormap.model.dto.route.RouteResponse
import com.khigh.seniormap.model.dto.route.RouteStep
import com.khigh.seniormap.model.dto.route.RouteDetail

/**
 * 경로 검색 결과 화면
 * 선택된 출발지와 도착지 간의 경로를 지도에 표시하고 경로 정보를 제공
 *
 * @param modifier 레이아웃 수정자
 * @param onBackClick 뒤로가기 버튼 클릭 콜백
 * @param startLocation 출발지 정보
 * @param endLocation 도착지 정보
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RouteSearchScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    routeViewModel: RouteViewModel
) {
    // ViewModel 상태 수집
    val routeSearchState by routeViewModel.routeSearchState.collectAsStateWithLifecycle()
    val selectedRouteOption by routeViewModel.selectedRouteOption.collectAsStateWithLifecycle()
    val isCalculating by routeViewModel.isCalculating.collectAsStateWithLifecycle()
    val selectedRouteIndex by routeViewModel.selectedRouteIndex.collectAsStateWithLifecycle()
    
    val startLocation by routeViewModel.startLocation.collectAsStateWithLifecycle()
    val endLocation by routeViewModel.endLocation.collectAsStateWithLifecycle()

    // 경로 탐색 실행
    LaunchedEffect(startLocation, endLocation, selectedRouteOption, Unit) {
        Log.d("com.khigh.seniormap", "[RouteSearchScreen] startLocation: $startLocation, endLocation: $endLocation, selectedRouteOption: $selectedRouteOption")
        if (startLocation != null && endLocation != null) {
            routeViewModel.searchRoute()
        }
    }
    
    // 상태 관리
    var kakaoMap by remember { mutableStateOf<KakaoMap?>(null) }
    var labelManager by remember { mutableStateOf<LabelManager?>(null) }
    var routeLineManager by remember { mutableStateOf<RouteLineManager?>(null) }
    var showRouteOptions by remember { mutableStateOf(false) }

    // 경로 정보 상태 계산
    val routeDetail = routeViewModel.getSelectedRoute()
    val routeDistance = routeDetail?.distance?.toString()?.plus("m") ?: "계산 중..."
    val routeDuration = routeDetail?.duration?.toString()?.plus("분") ?: "계산 중..."
    val routeSteps = routeDetail?.let { generateRouteSteps(it) } ?: emptyList()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "경로 안내",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "뒤로가기"
                        )
                    }
                },
                actions = {
                    // 경로 옵션 토글 버튼
                    IconButton(onClick = { showRouteOptions = !showRouteOptions }) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_route),
                            contentDescription = "경로 옵션"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // 경로 요약 정보
            RouteSummaryCard(
                startLocation = startLocation,
                endLocation = endLocation,
                routeDistance = routeDistance,
                routeDuration = routeDuration,
                routeOption = selectedRouteOption,
                isCalculating = isCalculating,
                modifier = Modifier.padding(16.dp)
            )

            // 지도 영역
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 16.dp)
            ) {
                KakaoMapComponent(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(12.dp)),
                    onKakaoMapReady = { map ->
                        kakaoMap = map
                        labelManager = map.labelManager
                        routeLineManager = map.routeLineManager

                        // 지도 초기 설정
                        setupMapForRoute(map, startLocation, endLocation)
                    },
                    onMapLoaded = { /* 지도 로드 완료 */ },
                    onLocationSelected = { _, _, _ -> /* 위치 선택 처리 */ },
                    onCameraMoveEnd = { /* 카메라 이동 완료 */ },
                    onMapViewInfoChanged = { /* 지도 정보 변경 */ }
                )

                // 경로 옵션 오버레이
                if (showRouteOptions) {
                    RouteOptionsOverlay(
                        selectedOption = selectedRouteOption,
                        onOptionSelected = { option ->
                            routeViewModel.setRouteOption(option)
                            showRouteOptions = false
                        },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(16.dp)
                    )
                }

                // 로딩 오버레이
                if (isCalculating) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.3f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            )
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    strokeWidth = 3.dp
                                )
                                Text(
                                    text = "경로를 계산하고 있습니다...",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }

            // 경로 상세 정보 또는 에러 상태
            when {
                isCalculating -> {
                    // 로딩 상태는 이미 지도 위에 표시됨
                }
                routeSearchState.isFailure -> {
                    // 에러 상태 표시
                    val errorMessage = routeSearchState.exceptionOrNull()?.message ?: "알 수 없는 오류가 발생했습니다"
                    ErrorCard(
                        message = errorMessage,
                        onRetry = { routeViewModel.searchRoute() },
                        modifier = Modifier.padding(16.dp)
                    )
                }
                routeSteps.isNotEmpty() -> {
                    // 경로 상세 정보 표시
                    RouteDetailsSection(
                        routeSteps = routeSteps,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}

/**
 * 경로 요약 정보 카드
 */
@Composable
private fun RouteSummaryCard(
    startLocation: PlaceSearchResult?,
    endLocation: PlaceSearchResult?,
    routeDistance: String,
    routeDuration: String,
    routeOption: RouteOption,
    isCalculating: Boolean,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.1f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // 경로 옵션 표시
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "경로 정보",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                RouteOptionChip(routeOption)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 출발지 정보
            LocationInfoRow(
                icon = ImageVector.vectorResource(id = R.drawable.ic_start_marker),
                iconTint = MaterialTheme.colorScheme.primary,
                title = "출발지",
                location = startLocation
            )

            // 경로 정보 중앙 표시
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                if (isCalculating) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            strokeWidth = 2.dp
                        )
                        Text(
                            text = "경로 계산 중...",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Medium
                        )
                    }
                } else {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "경로",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            text = "$routeDistance • $routeDuration",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // 도착지 정보
            LocationInfoRow(
                icon = ImageVector.vectorResource(id = R.drawable.ic_end_marker),
                iconTint = MaterialTheme.colorScheme.error,
                title = "도착지",
                location = endLocation
            )
        }
    }
}

/**
 * 경로 옵션 칩
 */
@Composable
private fun RouteOptionChip(
    routeOption: RouteOption,
    modifier: Modifier = Modifier
) {
    val (icon, text, color) = when (routeOption) {
        RouteOption.CAR -> Triple(ImageVector.vectorResource(id = R.drawable.ic_car), "자동차", MaterialTheme.colorScheme.primary)
        RouteOption.WALK -> Triple(ImageVector.vectorResource(id = R.drawable.ic_walk), "도보", MaterialTheme.colorScheme.secondary)
        RouteOption.PUBLIC_TRANSPORT -> Triple(ImageVector.vectorResource(id = R.drawable.ic_bus), "대중교통", MaterialTheme.colorScheme.tertiary)
    }

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = color.copy(alpha = 0.1f)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = text,
                tint = color,
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = text,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = color
            )
        }
    }
}

/**
 * 위치 정보 행
 */
@Composable
private fun LocationInfoRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconTint: Color,
    title: String,
    location: PlaceSearchResult?,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = iconTint,
            modifier = Modifier.size(20.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Medium
            )

            location?.let { loc ->
                Text(
                    text = loc.name,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Medium
                )

                if (loc.address.isNotEmpty()) {
                    Text(
                        text = loc.address,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } ?: run {
                Text(
                    text = "위치를 선택해주세요",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                )
            }
        }
    }
}

/**
 * 경로 옵션 오버레이
 */
@Composable
private fun RouteOptionsOverlay(
    selectedOption: RouteOption,
    onOptionSelected: (RouteOption) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            RouteOptionButton(
                icon = ImageVector.vectorResource(id = R.drawable.ic_car),
                text = "자동차",
                isSelected = selectedOption == RouteOption.CAR,
                onClick = { onOptionSelected(RouteOption.CAR) }
            )

            RouteOptionButton(
                icon = ImageVector.vectorResource(id = R.drawable.ic_walk),
                text = "도보",
                isSelected = selectedOption == RouteOption.WALK,
                onClick = { onOptionSelected(RouteOption.WALK) }
            )

            RouteOptionButton(
                icon = ImageVector.vectorResource(id = R.drawable.ic_bus),
                text = "대중교통",
                isSelected = selectedOption == RouteOption.PUBLIC_TRANSPORT,
                onClick = { onOptionSelected(RouteOption.PUBLIC_TRANSPORT) }
                )
        }
    }
}

/**
 * 경로 옵션 버튼
 */
@Composable
private fun RouteOptionButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (isSelected) {
        MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
    } else {
        Color.Transparent
    }

    val contentColor = if (isSelected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.onSurface
    }

    TextButton(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        colors = ButtonDefaults.textButtonColors(
            containerColor = backgroundColor,
            contentColor = contentColor
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = text,
                modifier = Modifier.size(18.dp)
            )
            Text(
                text = text,
                fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal
            )
        }
    }
}

/**
 * 경로 상세 정보 섹션
 */
@Composable
private fun RouteDetailsSection(
    routeSteps: List<RouteStep>,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "상세 경로",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = "${routeSteps.size}단계",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.heightIn(max = 200.dp)
            ) {
                items(routeSteps) { step ->
                    RouteStepItem(step = step)
                }
            }
        }
    }
}

/**
 * 경로 단계 아이템
 */
@Composable
private fun RouteStepItem(
    step: RouteStep,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 단계 번호
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = step.order.toString(),
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        // 단계 정보
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = step.instruction,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Medium
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (step.distance.isNotEmpty()) {
                    Text(
                        text = step.distance,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                if (step.duration.isNotEmpty()) {
                    Text(
                        text = "• ${step.duration}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

/**
 * 에러 상태를 표시하는 카드
 */
@Composable
private fun ErrorCard(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.1f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_error),
                contentDescription = "에러",
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(32.dp)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = message,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onErrorContainer,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Button(
                onClick = onRetry,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("다시 시도")
            }
        }
    }
}

// 카카오맵 SDK 관련 헬퍼 함수들

/**
 * 경로 화면용 지도 초기 설정
 */
private fun setupMapForRoute(
    map: KakaoMap,
    startLocation: PlaceSearchResult?,
    endLocation: PlaceSearchResult?
) {
    if (startLocation != null && endLocation != null) {
        try {
            // 출발지와 도착지를 포함하는 영역으로 카메라 이동
            val points = arrayOf(startLocation.latLng, endLocation.latLng)
            val cameraUpdate = CameraUpdateFactory.fitMapPoints(points, 100)
            map.moveCamera(cameraUpdate)

            // 마커 추가
            addLocationMarkers(map, startLocation, endLocation)
        } catch (e: Exception) {
            // 에러 발생 시 기본 위치로 설정
            val defaultPosition = LatLng.from(37.5665, 126.9780)
            val cameraUpdate = CameraUpdateFactory.newCenterPosition(defaultPosition, 12)
            map.moveCamera(cameraUpdate)
        }
    } else {
        // 기본 위치 (서울 시청)
        val defaultPosition = LatLng.from(37.5665, 126.9780)
        val cameraUpdate = CameraUpdateFactory.newCenterPosition(defaultPosition, 12)
        map.moveCamera(cameraUpdate)
    }
}

/**
 * 출발지와 도착지 마커 추가
 */
private fun addLocationMarkers(
    map: KakaoMap,
    startLocation: PlaceSearchResult,
    endLocation: PlaceSearchResult
) {
    val labelManager = map.labelManager ?: return

    try {
        // 출발지 마커
        val startLabelStyle = LabelStyles.from(LabelStyle.from(R.drawable.ic_start_marker))
        val startLabelOptions = LabelOptions.from(startLocation.latLng)
            .setStyles(startLabelStyle)
            // .setTexts(startLocation.name)
        labelManager.layer?.addLabel(startLabelOptions)

        // 도착지 마커
        val endLabelStyle = LabelStyles.from(LabelStyle.from(R.drawable.ic_end_marker))
        val endLabelOptions = LabelOptions.from(endLocation.latLng)
            .setStyles(endLabelStyle)
            // .setTexts(endLocation.name)
        labelManager.layer?.addLabel(endLabelOptions)
    } catch (e: Exception) {
        // 마커 추가 실패 시 로그 또는 처리
        e.printStackTrace()
    }
}

/**
 * 경로 계산 및 지도에 표시
 */
private suspend fun calculateAndDisplayRoute(
    kakaoMap: KakaoMap?,
    routeLineManager: RouteLineManager?,
    start: PlaceSearchResult,
    end: PlaceSearchResult,
    onResult: (RouteInfo) -> Unit,
    viewModel: RouteViewModel
) {
    val selectedRouteOption = viewModel.selectedRouteOption.value
    try {
        // 실제 카카오내비 API 호출
        // 여기서는 더미 데이터로 대체
        val routeInfo = when (selectedRouteOption) {
            RouteOption.CAR -> RouteInfo(
                distance = "5.2km",
                duration = "15분",
                steps = listOf(
                    RouteStep(
                        order = 1,
                        instruction = "출발지에서 이동",
                        distance = "5.2km",
                        duration = "15분"
                    )
                )
            )
            RouteOption.WALK -> RouteInfo(
                distance = "4.8km",
                duration = "58분",
                steps = listOf(
                    RouteStep(
                        order = 1,
                        instruction = "출발지에서 이동",
                        distance = "4.8km",
                        duration = "58분"
                    )
                )
            )
            RouteOption.PUBLIC_TRANSPORT -> RouteInfo(
                distance = "6.1km",
                duration = "32분",
                steps = listOf(
                    RouteStep(
                        order = 1,
                        instruction = "출발지에서 이동",
                        distance = "6.1km",
                        duration = "32분"
                    )
                )
            )
            else -> RouteInfo(
                distance = "알 수 없음",
                duration = "알 수 없음",
                steps = emptyList()
            )
        }

        // 경로선 지도에 그리기
        drawRouteOnMap(kakaoMap, routeLineManager, start, end, viewModel)
        
        Log.d("com.khigh.seniormap", "[calculateAndDisplayRoute] routeInfo: $routeInfo")
        onResult(routeInfo)

    } catch (e: Exception) {
        // 에러 발생 시 기본 경로 정보 제공
        onResult(RouteInfo("알 수 없음", "계산 불가", emptyList()))
    }
}

/**
 * 지도에 경로선 그리기
 */
private fun drawRouteOnMap(
    map: KakaoMap?,
    routeLineManager: RouteLineManager?,
    start: PlaceSearchResult,
    end: PlaceSearchResult,
    routeViewModel: RouteViewModel
) {
    val _tag = "com.khigh.seniormap.RouteSearchScreen"
    map ?: return
    routeLineManager ?: return
    val selectedRouteOption = routeViewModel.selectedRouteOption.value
    Log.d(_tag, "[drawRouteOnMap] selectedRouteOption: $selectedRouteOption")

    try {
        // 경로선 스타일 설정
        val routeColor = when (selectedRouteOption) {
            RouteOption.CAR -> 0xFF4285F4.toInt() // 파란색
            RouteOption.WALK -> 0xFF34A853.toInt() // 초록색
            RouteOption.PUBLIC_TRANSPORT -> 0xFFEA4335.toInt() // 빨간색
            else -> 0xFF000000.toInt() // 기본 검정색
        }

        val routeStyle = RouteLineStyles.from(
            RouteLineStyle.from(8.dp.value, routeColor)
        )

        val routePoints = arrayOf(start.latLng, end.latLng)

        // 간단한 직선 경로 (실제로는 API에서 받은 경로 좌표 사용)
        val routeSegment = RouteLineSegment.from(routePoints, routeStyle)
        val routeOptions = RouteLineOptions.from(routeSegment)

        routeLineManager.layer?.addRouteLine(routeOptions)
    } catch (e: Exception) {
        // 경로선 그리기 실패 시 로그 또는 처리
        e.printStackTrace()
    }
}

/**
 * RouteInfo를 RouteStep 리스트로 변환
 * 실제 API 응답을 UI에서 사용할 수 있는 형태로 변환
 */
private fun generateRouteSteps(routeDetail: RouteDetail): List<RouteStep> {
    // 실제로는 API에서 받은 상세 경로 정보를 사용
    // 여기서는 기본적인 경로 단계를 생성
    return listOf(
        RouteStep(1, "출발지에서 이동", "${routeDetail.distance ?: 0}m", "${routeDetail.duration ?: 0}분"),
        RouteStep(2, "경로 진행 중", "중간 지점", "진행 중"),
        RouteStep(3, "도착지에 도착", "0m", "0분")
    )
}
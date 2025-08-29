package com.khigh.seniormap.ui.screens.senior.components

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.kakao.vectormap.MapView
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.Poi
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.KakaoMapReadyCallback
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.compose.runtime.DisposableEffect
import androidx.lifecycle.compose.LocalLifecycleOwner
import android.graphics.PointF
import android.util.Log
import com.kakao.vectormap.MapViewInfo
import com.kakao.vectormap.camera.CameraPosition

/**
 * 카카오맵을 표시하는 컴포넌트
 * Android SDK를 사용하여 네이티브 카카오맵을 표시
 */
@Composable
fun KakaoMapComponent(
    modifier: Modifier = Modifier,
    onMapLoaded: () -> Unit = {},
    onLocationSelected: (lat: Double, lng: Double, address: String?) -> Unit = { _, _, _ -> },
    onKakaoMapReady: (KakaoMap) -> Unit = {},
    onCameraMoveEnd: (camera: CameraPosition) -> Unit = {},
    onMapViewInfoChanged: (info: MapViewInfo) -> Unit = {}
) {
    val TAG = "KakaoMapComponent"
    val context = LocalContext.current
    var isLoading by remember { mutableStateOf(true) }
    var hasError by remember { mutableStateOf(false) }

    val lifecycle = LocalLifecycleOwner.current.lifecycle
    var mapView by remember { mutableStateOf<MapView?>(null) }
    var kakaoMap by remember { mutableStateOf<KakaoMap?>(null) }
    
    // MapView 수명주기 연결 (권장)
    DisposableEffect(lifecycle) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> {
                    Log.d("com.khigh.seniormap.ui.screens.senior.components.KakaoMapComponent", "[DisposableEffect.onResume]")
                    Log.d("com.khigh.seniormap.ui.screens.senior.components.KakaoMapComponent", "[DisposableEffect.onResume.mapView] ${mapView}")
                    mapView?.resume()
                }
                Lifecycle.Event.ON_PAUSE  -> {
                    Log.d("com.khigh.seniormap.ui.screens.senior.components.KakaoMapComponent", "[DisposableEffect.onPause]")
                    Log.d("com.khigh.seniormap.ui.screens.senior.components.KakaoMapComponent", "[DisposableEffect.onPause.mapView] ${mapView}")
                    mapView?.pause()
                }
                else -> Unit
            }
        }
        lifecycle.addObserver(observer)
    
        onDispose {
            Log.d("com.khigh.seniormap.ui.screens.senior.components.KakaoMapComponent", "[DisposableEffect.onDispose]")
            lifecycle.removeObserver(observer)
            mapView?.finish()
        }
    }
    
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 카카오맵 MapView
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { ctx ->
                val mv = createKakaoMapView(
                    context = ctx,
                    // onMapLoaded = {
                    //     // 지도 준비 완료시 UI 업데이트 등
                    // },
                    onMapLoaded = onMapLoaded,
                    // onLocationSelected = { lat, lng, address ->
                    //     // 지도 단일 탭으로 선택된 위치 콜백 처리
                    //     // 예: viewModel에 전달, 스낵바 노출, 카메라 이동 등
                    // }
                    onLocationSelected = onLocationSelected,
                    onKakaoMapReady = {
                        Log.d(TAG, "onKakaoMapReady")
                        onKakaoMapReady(it)
                        kakaoMap = it
                        isLoading = false
                    },
                    onKakaoMapError = {
                        Log.d(TAG, "[createKakaoMapView.onMapError] ${it.message}")
                        isLoading = false
                        hasError = true
                    },
                    onCameraMoveEnd = {
                        Log.d(TAG, "[createKakaoMapView.onCameraMoveEnd] ${it}")
                        // mapView?.resume()
                        onCameraMoveEnd(it)
                    },
                    onMapViewInfoChanged = {
                        Log.d(TAG, "[createKakaoMapView.onMapViewInfoChanged] ${it}")
                        // mapView?.resume()
                        onMapViewInfoChanged(it)
                    }
                )

                mapView = mv
                Log.d("com.khigh.seniormap.ui.screens.senior.components.KakaoMapComponent", "[createKakaoMapView.mapView] ${mv}")
                // if (lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
                mv.resume()
                // }
                mv
            },
            // update = { /* 옵션 갱신이 필요하면 이곳에서 */ }
        )

        // 로딩 오버레이
        if (isLoading) {
            Box(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        // 에러 오버레이
        if (hasError) {
            Box(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("지도를 불러올 수 없습니다", color = MaterialTheme.colorScheme.error)
                    Spacer(Modifier.height(8.dp))
                    Button(onClick = {
                        hasError = false
                            isLoading = true
                        // 필요하면 mapView?.restart() 대신 다시 Composable 재진입 유도
                    }) { Text("다시 시도") }
                }
            }
        }
    }
}

/**
 * 카카오맵 MapView 생성
 */
private fun createKakaoMapView(
    context: Context,
    onMapLoaded: () -> Unit,
    onLocationSelected: (lat: Double, lng: Double, address: String?) -> Unit = { _, _, _ -> },
    onKakaoMapReady: (KakaoMap) -> Unit = {},
    onKakaoMapError: (Exception) -> Unit = {},
    onCameraMoveEnd: (camera: CameraPosition) -> Unit = {},
    onMapViewInfoChanged: (info: MapViewInfo) -> Unit = {}
): MapView {
    return MapView(context).apply {
        // v2: MapView.start(...)로 라이프사이클/준비 콜백 연결
        start(
            object : MapLifeCycleCallback() {
                override fun onMapDestroy() { /* no-op */ }
                override fun onMapError(error: Exception) {
                    // 인증/초기화 에러 등 처리
                    Log.d("com.khigh.seniormap.ui.screens.senior.components.KakaoMapComponent", "[createKakaoMapView.onMapError] ${error.message}")
                    onKakaoMapError(error)
                }
            },
            object : KakaoMapReadyCallback() {
                override fun onMapReady(kakaoMap: KakaoMap) {
                    Log.d("com.khigh.seniormap.ui.screens.senior.components.KakaoMapComponent", "[createKakaoMapView.onMapReady]")
                    // 지도 준비 완료
                    onMapLoaded()
                    onKakaoMapReady(kakaoMap)
                    // 초기 카메라: 서울시청
                    kakaoMap.moveCamera(
                        CameraUpdateFactory.newCenterPosition(
                            LatLng.from(37.5665, 126.9780)   // v2: LatLng.from(...)
                        )
                    )
                    // 초기 줌 레벨(예시)
                    kakaoMap.moveCamera(CameraUpdateFactory.zoomTo(10))

                    // 맵 업데이트 콜백 등록
                    kakaoMap.setOnCameraMoveEndListener { _, cameraPosition, _ ->
                        Log.d("com.khigh.seniormap.ui.screens.senior.components.KakaoMapComponent", "[createKakaoMapView.onCameraMoveEndListener] ${cameraPosition}")
                        onCameraMoveEnd(cameraPosition)
                    }
                    kakaoMap.setOnMapViewInfoChangeListener(object : KakaoMap.OnMapViewInfoChangeListener {
                        override fun onMapViewInfoChanged(mapViewInfo: com.kakao.vectormap.MapViewInfo) {
                            Log.d("com.khigh.seniormap.ui.screens.senior.components.KakaoMapComponent", "[createKakaoMapView.onMapViewInfoChangeListener] ${mapViewInfo}")
                            onMapViewInfoChanged(mapViewInfo)
                        }
                        override fun onMapViewInfoChangeFailed() { /* no-op */ }
                    })

                    // 지도 탭 이벤트
                    kakaoMap.setOnMapClickListener(object : KakaoMap.OnMapClickListener {
                        override fun onMapClicked(
                            map: KakaoMap,
                            position: LatLng,
                            screenPoint: PointF,
                            poi: Poi
                        ) {
                            Log.d("com.khigh.seniormap.ui.screens.senior.components.KakaoMapComponent", "[createKakaoMapView.onMapClickListener] ${position}")
                            onLocationSelected(
                                position.latitude,     // v2 LatLng의 위도
                                position.longitude,    // v2 LatLng의 경도
                                "선택된 위치"          // 필요시 Kakao Local API로 역지오코딩 연동
                            )
                        }
                    })
                }
            }
        )
    }
} 
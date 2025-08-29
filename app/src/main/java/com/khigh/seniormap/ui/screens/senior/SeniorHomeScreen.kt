package com.khigh.seniormap.ui.screens.senior

import android.content.pm.PackageManager
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.khigh.seniormap.ui.screens.senior.components.*
import com.khigh.seniormap.ui.screens.senior.SeniorCaregiverListScreen
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.LatLng
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import android.util.Log
import androidx.core.content.ContextCompat.*
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.kakao.vectormap.MapViewInfo

/**
 * 피보호인 홈 화면
 * 지도가 메인 화면이고, 사이드바를 통해 다른 기능에 접근
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeniorHomeScreen(
    onNavigateToLogin: () -> Unit = {},
    modifier: Modifier = Modifier,
    onNavigateToLocationSearch: () -> Unit = {},
    onNavigateToFavoriteEdit: () -> Unit = {}
) {
    var showSidebar by remember { mutableStateOf(false) }
    var showFallDetection by remember { mutableStateOf(false) }
    var showCaregiverList by remember { mutableStateOf(false) }
    val favoriteRoutes = remember { SeniorMockData.favoriteRoutes }
    
    // 사이드바 애니메이션
    val sidebarOffset by animateFloatAsState(
        targetValue = if (showSidebar) 0f else -300f,
        animationSpec = tween(durationMillis = 300),
        label = "sidebar_animation"
    )
    
    // 낙상 감지 화면 오버레이
    if (showFallDetection) {
        FallDetectionScreen(
            onDismiss = { showFallDetection = false },
            onEmergencyCall = { 
                // TODO: 실제 응급 신고 로직
                showFallDetection = false
            }
        )
    }
    
    Box(modifier = modifier.fillMaxSize()) {
        when {
            showCaregiverList -> {
                // 보호인 목록 화면
                SeniorCaregiverListScreen(
                    onBackClick = { showCaregiverList = false },
                    modifier = Modifier.fillMaxSize()
                )
            }
            else -> {
                // 메인 지도 화면
                MainMapScreen(
                    onMenuClick = { showSidebar = true },
                    onFallDetectionClick = { showFallDetection = true },
                    onNavigateToLocationSearch = onNavigateToLocationSearch
                )
                
                // 사이드바
                Sidebar(
                    isVisible = showSidebar,
                    offset = sidebarOffset,
                    onDismiss = { showSidebar = false },
                    onNavigateToLocationSearch = onNavigateToLocationSearch,
                    onNavigateToFavoriteEdit = onNavigateToFavoriteEdit,
                    onNavigateToLogin = onNavigateToLogin,
                    onNavigateToCaregiverList = { showCaregiverList = true }
                )
                
                // 사이드바 배경 오버레이
                if (showSidebar) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.3f))
                            .clickable { showSidebar = false }
                            .zIndex(1f)
                    )
                }
            }
        }
    }
}

/**
 * 메인 지도 화면
 */
@Composable
private fun MainMapScreen(
    onMenuClick: () -> Unit,
    onFallDetectionClick: () -> Unit,
    modifier: Modifier = Modifier,
    onNavigateToLocationSearch: () -> Unit = {}
) {
    val context = LocalContext.current
    val cts = CancellationTokenSource()
    val fusedLocationClient: FusedLocationProviderClient = remember(context) {
        LocationServices.getFusedLocationProviderClient(context)
    }
    var kakaoMap by remember { mutableStateOf<com.kakao.vectormap.KakaoMap?>(null) }
    val REQUEST_LOCATION_PERMISSION = 1000

    fun moveToLocation(lat: Double, lng: Double) {
        Log.d("com.khigh.seniormap", "[MainMapScreen] moveToLocation: ${lat}, ${lng}")
        Log.d("com.khigh.seniormap", "[MainMapScreen] moveToLocation.kakaoMap: ${kakaoMap}")
        kakaoMap?.moveCamera(
            CameraUpdateFactory.newCenterPosition(
                LatLng.from(lat, lng)
            )
        )
    }

    fun onMapViewInfoChanged(info: MapViewInfo) {
        Log.d("com.khigh.seniormap", "[MainMapScreen] onMapViewInfoChanged: ${info}")
    }

    Box(modifier = modifier.fillMaxSize()) {
        // 카카오맵 컴포넌트
        KakaoMapComponent(
            modifier = Modifier.fillMaxSize(),
            onMapLoaded = { /* 지도 로드 완료 */ },
            onLocationSelected = { lat, lng, address ->
                // 선택된 위치 처리
                // TODO: 선택된 위치를 저장하거나 다른 기능과 연동
            },
            onKakaoMapReady = { map ->
                kakaoMap = map
                Log.d("com.khigh.seniormap", "[MainMapScreen] onKakaoMapReady: ${kakaoMap}")
            },
            onCameraMoveEnd = {
                Log.d("com.khigh.seniormap", "[MainMapScreen] onCameraMoveEnd: ${it}")
            },
            onMapViewInfoChanged = {
                Log.d("com.khigh.seniormap", "[MainMapScreen] onMapViewInfoChanged: ${it}")
            }
        )
        
        // 상단 앱바
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
            shape = RoundedCornerShape(16.dp),
            shadowElevation = 4.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 메뉴 버튼
                IconButton(onClick = onMenuClick) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "메뉴",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
                
                // 제목
                Text(
                    text = "시니어 맵",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                // 우측 버튼들
                Row {
                    // 현재 위치 버튼
                    IconButton(onClick = {
                        Log.d("com.khigh.seniormap", "[MainMapScreen] moveToLocation.start")
                        fun moveTo(lat: Double, lng: Double) {
                            Log.d("com.khigh.seniormap", "[MainMapScreen] moveToLocation.moveTo: ${lat}, ${lng}")
                            moveToLocation(lat, lng)
                        }

                        if (checkSelfPermission(
                            context,
                            android.Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED) {
                            Log.d("com.khigh.seniormap", "[MainMapScreen] moveToLocation.success.permission granted")
                            fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, cts.token)
                                .addOnSuccessListener { loc ->
                                    Log.d("com.khigh.seniormap", "[MainMapScreen] moveToLocation.success.loc: ${loc}")
                                    if (loc != null) {
                                        val lat = loc.latitude
                                        val lng = loc.longitude
                                        moveTo(lat, lng)
                                    } else {
                                        // 폴백: 마지막 위치
                                        fusedLocationClient.getLastLocation()
                                            .addOnSuccessListener { last ->
                                                Log.d("com.khigh.seniormap", "[MainMapScreen] moveToLocation.success.last: ${last}")
                                                if (last != null) {
                                                    val lat = last.latitude
                                                    val lng = last.longitude
                                                    moveTo(lat, lng)
                                                } else {
                                                    Log.d("com.khigh.seniormap", "[MainMapScreen] moveToLocation.failure: last is null")
                                                    moveTo(37.5665, 126.9780)
                                                }
                                            }
                                            .addOnFailureListener {
                                                Log.d("com.khigh.seniormap", "[MainMapScreen] moveToLocation.failure: ${it.message}")
                                                moveTo(37.5665, 126.9780)
                                            }

                                    }
                                }
                                .addOnFailureListener {
                                    Log.d("com.khigh.seniormap", "[MainMapScreen] moveToLocation.failure: ${it.message}")
                                    moveTo(37.5665, 126.9780)
                                }
                        } else {
                            Log.d("com.khigh.seniormap", "[MainMapScreen] moveToLocation.failure: permission denied")
                            moveTo(37.5665, 126.9780)
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "현재 위치",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    
                    // 검색 버튼
                    IconButton(onClick = { 
                        // 위치 검색 화면으로 이동
                        onNavigateToLocationSearch()
                    }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "장소 검색",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
        
        // 하단 플로팅 액션 버튼들
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // 즐겨찾기 버튼
            Surface(
                modifier = Modifier
                    .size(56.dp)
                    .clickable { /* TODO: 즐겨찾기 목록 */ },
                shape = RoundedCornerShape(28.dp),
                color = MaterialTheme.colorScheme.secondary,
                shadowElevation = 6.dp
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "즐겨찾기",
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.onSecondary
                    )
                }
            }
            
            // 낙상 감지 버튼
            Surface(
                modifier = Modifier
                    .size(56.dp)
                    .clickable { onFallDetectionClick() },
                shape = RoundedCornerShape(28.dp),
                color = MaterialTheme.colorScheme.error,
                shadowElevation = 6.dp
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "낙상 감지",
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.onError
                    )
                }
            }
        }
    }
}

/**
 * 사이드바
 */
@Composable
private fun Sidebar(
    isVisible: Boolean,
    offset: Float,
    onDismiss: () -> Unit,
    onNavigateToLocationSearch: () -> Unit,
    onNavigateToFavoriteEdit: () -> Unit,
    onNavigateToLogin: () -> Unit,
    onNavigateToCaregiverList: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxHeight()
            .width(300.dp)
            .offset(x = offset.dp)
            .background(
                MaterialTheme.colorScheme.surface,
                RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp)
            )
            .zIndex(2f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // 헤더
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "메뉴",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "닫기",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
            
            // 메뉴 항목들
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    SidebarMenuItem(
                        icon = Icons.Default.Search,
                        title = "위치 검색",
                        description = "장소를 검색하고 경로를 찾습니다",
                        onClick = onNavigateToLocationSearch
                    )
                }
                
                item {
                    SidebarMenuItem(
                        icon = Icons.Default.Favorite,
                        title = "즐겨찾기",
                        description = "자주 가는 장소를 관리합니다",
                        onClick = onNavigateToFavoriteEdit
                    )
                }
                
                item {
                    SidebarMenuItem(
                        icon = Icons.Default.Person,
                        title = "가족 구성원",
                        description = "자녀의 정보를 확인할 수 있습니다",
                        onClick = { 
                            // showSidebar = false
                            onNavigateToCaregiverList()
                        }
                    )
                }
                
                item {
                    SidebarMenuItem(
                        icon = Icons.Default.Settings,
                        title = "설정",
                        description = "앱 설정을 관리합니다",
                        onClick = { /* TODO: 설정 화면 */ }
                    )
                }
                
                item {
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 16.dp),
                        color = MaterialTheme.colorScheme.outline
                    )
                }
                
                item {
                    SidebarMenuItem(
                        icon = Icons.Default.ExitToApp,
                        title = "로그아웃",
                        description = "앱에서 로그아웃합니다",
                        onClick = onNavigateToLogin,
                        isDestructive = true
                    )
                }
            }
        }
    }
}

/**
 * 사이드바 메뉴 아이템
 */
@Composable
private fun SidebarMenuItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    description: String,
    onClick: () -> Unit,
    isDestructive: Boolean = false,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                modifier = Modifier.size(24.dp),
                tint = if (isDestructive) {
                    MaterialTheme.colorScheme.error
                } else {
                    MaterialTheme.colorScheme.primary
                }
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (isDestructive) {
                        MaterialTheme.colorScheme.error
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    }
                )
                
                Spacer(modifier = Modifier.height(2.dp))
                
                Text(
                    text = description,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
} 
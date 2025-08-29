package com.khigh.seniormap.ui.screens.guardian

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.khigh.seniormap.ui.screens.guardian.components.GuardianMapHeader
import com.khigh.seniormap.ui.screens.guardian.components.GuardianMapInfoPanel
import com.khigh.seniormap.ui.screens.guardian.components.GuardianMapControls
import com.khigh.seniormap.R
import com.khigh.seniormap.ui.screens.senior.components.KakaoMapComponent
import com.kakao.vectormap.KakaoMap
import android.util.Log

/**
 * 보호인 지도 화면 컴포넌트
 * 
 * 보호인이 피보호인들의 위치를 지도에서 확인할 수 있는 화면입니다.
 * 상단 헤더, 지도 영역, 하단 정보 패널을 포함합니다.
 * 
 * @param modifier 레이아웃 수정자
 * @param onBackClick 뒤로가기 버튼 클릭 콜백
 * @param caregiverViewModel 보호자 관계 ViewModel
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuardianMapScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    caregiverViewModel: com.khigh.seniormap.viewmodel.CaregiverViewModel
) {
    // 현재 선택된 피보호인
    var selectedGuardian by remember { mutableStateOf<String?>(null) }
    
    // 지도 줌 레벨 상태
    var zoomLevel by remember { mutableStateOf(15) }
    
    // 지도 중심 좌표 (서울 시청 기준)
    var centerLat by remember { mutableStateOf(37.5665) }
    var centerLng by remember { mutableStateOf(126.9780) }

    var kakaoMap: KakaoMap? = null

    Scaffold(
        modifier = modifier,
        topBar = {
            GuardianMapHeader(
                onBackClick = onBackClick,
                onSearchClick = { /* TODO: 검색 기능 구현 */ },
                onFilterClick = { /* TODO: 필터 기능 구현 */ }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // 지도 영역 (현재는 빈 박스)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFF5F5F5))
                    .padding(16.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {

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
                // Column(
                //     horizontalAlignment = Alignment.CenterHorizontally,
                //     verticalArrangement = Arrangement.Center
                // ) {
                    // Icon(
                    //     imageVector = ImageVector.vectorResource(R.drawable.ic_map_focused),
                    //     contentDescription = "지도",
                    //     modifier = Modifier.size(64.dp),
                    //     tint = Color.Gray
                    // )
                    // Spacer(modifier = Modifier.height(16.dp))
                    // Text(
                    //     text = "지도 영역",
                    //     style = MaterialTheme.typography.bodyLarge,
                    //     color = Color.Gray
                    // )
                    // Text(
                    //     text = "카카오맵 SDK가 여기에 표시됩니다",
                    //     style = MaterialTheme.typography.bodyMedium,
                    //     color = Color.Gray.copy(alpha = 0.7f)
                    // )
                // }
            }
            
            // 지도 컨트롤 버튼들
            GuardianMapControls(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 80.dp, end = 20.dp),
                onZoomIn = { zoomLevel = (zoomLevel + 1).coerceAtMost(20) },
                onZoomOut = { zoomLevel = (zoomLevel - 1).coerceAtLeast(1) },
                onMyLocation = { /* TODO: 현재 위치로 이동 */ },
                onFullScreen = { /* TODO: 전체 화면 모드 */ }
            )
            
            // 하단 정보 패널
            GuardianMapInfoPanel(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 20.dp),
                selectedGuardian = selectedGuardian,
                onGuardianSelect = { guardianId -> selectedGuardian = guardianId },
                onNavigateClick = { /* TODO: 경로 안내 */ }
            )
        }
    }
} 
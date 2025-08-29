package com.khigh.seniormap.ui.screens.senior

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.khigh.seniormap.ui.screens.senior.components.*
import com.kakao.vectormap.KakaoMap
import android.util.Log
import com.khigh.seniormap.ui.screens.senior.components.KakaoMapComponent

/**
 * 피보호인 즐겨찾기 수정 화면
 * Figma 디자인 기반: 즐겨찾기 목록 + 삭제 버튼
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteEditScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {}
) {
    var favoriteRoutes by remember { mutableStateOf(SeniorMockData.favoriteRoutes) }
    var isEditMode by remember { mutableStateOf(false) }
    
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "즐겨찾기",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
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
                    TextButton(
                        onClick = { isEditMode = !isEditMode }
                    ) {
                        Text(
                            text = if (isEditMode) "완료" else "수정",
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.primary
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
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // 지도 영역 (미니 버전)
            MiniMapSection(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // 위치 검색 버튼
            Button(
                onClick = { /* TODO: 위치 검색으로 이동 */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(horizontal = 16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Text(
                    text = "위치 검색",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // 즐겨찾기 목록
            FavoriteEditList(
                favoriteRoutes = favoriteRoutes,
                isEditMode = isEditMode,
                onDeleteRoute = { route ->
                    favoriteRoutes = favoriteRoutes.filter { it.id != route.id }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 16.dp)
            )
        }
    }
}

@Composable
private fun MiniMapSection(
    modifier: Modifier = Modifier
) {
    var kakaoMap: KakaoMap? = null

    Box(
        modifier = modifier
    ) {
        // 미니 지도 영역 (임시)
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.surfaceVariant
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
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
            }
        }
        
        // 현위치 버튼
        Button(
            onClick = { /* TODO: 현위치 기능 */ },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text(
                text = "현위치",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Composable
private fun FavoriteEditList(
    favoriteRoutes: List<FavoriteRoute>,
    isEditMode: Boolean,
    onDeleteRoute: (FavoriteRoute) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        // 헤더
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "즐겨찾기",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        
        // 즐겨찾기 목록
        if (favoriteRoutes.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "등록된 즐겨찾기가 없습니다",
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(favoriteRoutes) { route ->
                    FavoriteEditItem(
                        route = route,
                        isEditMode = isEditMode,
                        onDeleteClick = { onDeleteRoute(route) },
                        onClick = { /* TODO: 경로 안내 시작 */ }
                    )
                }
            }
        }
    }
}

@Composable
private fun FavoriteEditItem(
    route: FavoriteRoute,
    isEditMode: Boolean,
    onDeleteClick: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = if (!isEditMode) onClick else { {} },
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
            // 경로 정보
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = route.description,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            
            // 삭제 버튼 (수정 모드에서만 표시)
            if (isEditMode) {
                IconButton(
                    onClick = onDeleteClick,
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "삭제"
                    )
                }
            } else {
                // 수정 모드가 아닐 때는 빈 공간
                Spacer(modifier = Modifier.width(48.dp))
            }
        }
    }
} 
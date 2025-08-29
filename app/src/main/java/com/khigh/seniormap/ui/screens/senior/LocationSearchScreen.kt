package com.khigh.seniormap.ui.screens.senior

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.khigh.seniormap.ui.screens.senior.components.*
import com.khigh.seniormap.model.dto.kakaomap.PlaceSearchResult
import com.khigh.seniormap.viewmodel.LocationViewModel
import com.kakao.vectormap.LatLng
import com.khigh.seniormap.viewmodel.RouteViewModel


/**
 * 피보호인 위치 검색 화면
 * LocationViewModel을 사용하여 장소 검색 및 경로 옵션 제공
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationSearchScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onRouteSelected: () -> Unit = {},
    locationViewModel: LocationViewModel,
    routeViewModel: RouteViewModel
) {
    // ViewModel 상태 수집
    val startLocationQuery by locationViewModel.startLocationQuery.collectAsStateWithLifecycle()
    val endLocationQuery by locationViewModel.endLocationQuery.collectAsStateWithLifecycle()
    val startLocationSearchState by locationViewModel.startLocationSearchState.collectAsStateWithLifecycle()
    val endLocationSearchState by locationViewModel.endLocationSearchState.collectAsStateWithLifecycle()
    val selectedStartLocation by locationViewModel.selectedStartLocation.collectAsStateWithLifecycle()
    val selectedEndLocation by locationViewModel.selectedEndLocation.collectAsStateWithLifecycle()
    val showStartSearchResults by locationViewModel.showStartSearchResults.collectAsStateWithLifecycle()
    val showEndSearchResults by locationViewModel.showEndSearchResults.collectAsStateWithLifecycle()
    val isSearchLoading by locationViewModel.isSearchLoading.collectAsStateWithLifecycle()
    
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "위치 검색",
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
                .padding(16.dp)
        ) {
            // 출발/도착 입력 섹션
            LocationInputSection(
                startLocation = startLocationQuery,
                endLocation = endLocationQuery,
                selectedStartLocation = selectedStartLocation,
                selectedEndLocation = selectedEndLocation,
                onStartLocationChange = { query ->
                    locationViewModel.searchStartLocation(query)
                },
                onEndLocationChange = { query ->
                    locationViewModel.searchEndLocation(query)
                },
                onStartLocationSelect = { result ->
                    locationViewModel.selectStartLocation(result)
                },
                onEndLocationSelect = { result ->
                    locationViewModel.selectEndLocation(result)
                },
                onSwapLocations = {
                    locationViewModel.swapLocations()
                },
                onClearStartLocation = {
                    locationViewModel.clearStartLocation()
                },
                onClearEndLocation = {
                    locationViewModel.clearEndLocation()
                }
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // 검색 결과 표시 - 표준 Result 사용
            if (showStartSearchResults) {
                startLocationSearchState.fold(
                    onSuccess = { results ->
                        if (results.isNotEmpty()) {
                            SearchResultsSection(
                                title = "출발지 검색 결과",
                                results = results,
                                onResultClick = { result ->
                                    locationViewModel.selectStartLocation(result)
                                }
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    },
                    onFailure = { /* 에러는 아래에서 별도 처리 */ }
                )
            }
            
            if (showEndSearchResults) {
                endLocationSearchState.fold(
                    onSuccess = { results ->
                        if (results.isNotEmpty()) {
                            SearchResultsSection(
                                title = "도착지 검색 결과",
                                results = results,
                                onResultClick = { result ->
                                    locationViewModel.selectEndLocation(result)
                                }
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    },
                    onFailure = { /* 에러는 아래에서 별도 처리 */ }
                )
            }
            
            // 로딩 상태 표시
            if (isSearchLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp
                        )
                        Text(
                            text = "검색 중...",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            // 에러 상태 표시 - 표준 Result의 에러 처리
            val errorMessage = when {
                startLocationSearchState.isFailure -> startLocationSearchState.exceptionOrNull()?.message
                endLocationSearchState.isFailure -> endLocationSearchState.exceptionOrNull()?.message
                else -> null
            }
            
            errorMessage?.let { message ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "에러",
                            tint = MaterialTheme.colorScheme.onErrorContainer,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = message,
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            fontSize = 14.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            // 경로 검색 버튼 또는 안내 메시지
            val canSearchRoute = selectedStartLocation != null && selectedEndLocation != null
            
            if (canSearchRoute) {
                Button(
                    onClick = { 
                        selectedStartLocation?.let { start ->
                            selectedEndLocation?.let { end ->
                                routeViewModel.setStartLocation(start)
                                routeViewModel.setEndLocation(end)
                                onRouteSelected()
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "경로 검색",
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "경로 검색하기",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            } else {
                // 검색 안내 메시지
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "위치",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                            modifier = Modifier.size(48.dp)
                        )
                        Text(
                            text = "출발지와 목적지를 검색하여 선택해주세요",
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center,
                            lineHeight = 22.sp
                        )
                        Text(
                            text = "키워드를 입력하시면 관련 장소를 찾아드립니다",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LocationInputSection(
    startLocation: String,
    endLocation: String,
    selectedStartLocation: PlaceSearchResult?,
    selectedEndLocation: PlaceSearchResult?,
    onStartLocationChange: (String) -> Unit,
    onEndLocationChange: (String) -> Unit,
    onStartLocationSelect: (PlaceSearchResult) -> Unit,
    onEndLocationSelect: (PlaceSearchResult) -> Unit,
    onSwapLocations: () -> Unit,
    onClearStartLocation: () -> Unit,
    onClearEndLocation: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        // 출발 입력
        LocationInputField(
            label = "출발",
            value = startLocation,
            onValueChange = onStartLocationChange,
            placeholder = "출발지를 검색하세요",
            selectedLocation = selectedStartLocation,
            onClear = onClearStartLocation
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // 바꾸기 버튼
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(
                onClick = onSwapLocations,
                enabled = selectedStartLocation != null && selectedEndLocation != null
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "바꾸기",
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "바꾸기",
                    fontSize = 16.sp
                )
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // 도착 입력
        LocationInputField(
            label = "도착",
            value = endLocation,
            onValueChange = onEndLocationChange,
            placeholder = "목적지를 검색하세요",
            selectedLocation = selectedEndLocation,
            onClear = onClearEndLocation
        )
    }
}

@Composable
private fun LocationInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    selectedLocation: PlaceSearchResult?,
    onClear: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(
                    text = placeholder,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "검색",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            trailingIcon = {
                if (selectedLocation != null) {
                    IconButton(onClick = onClear) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "지우기",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline
            )
        )
        
        // 선택된 위치 정보 표시
        selectedLocation?.let { location ->
            Spacer(modifier = Modifier.height(8.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "위치",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = location.name,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = location.address,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchResultsSection(
    title: String,
    results: List<PlaceSearchResult>,
    onResultClick: (PlaceSearchResult) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.heightIn(max = 300.dp) // 최대 높이 제한
        ) {
            items(results.take(10)) { result ->
                SearchResultItem(
                    result = result,
                    onClick = { onResultClick(result) }
                )
            }
        }
    }
}

@Composable
private fun SearchResultItem(
    result: PlaceSearchResult,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp,
            pressedElevation = 4.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = "위치",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = result.name,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                Spacer(modifier = Modifier.height(2.dp))
                
                Text(
                    text = result.address,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                result.category?.let { category ->
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = category,
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}
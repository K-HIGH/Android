package com.khigh.seniormap.ui.screens.guardian

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.khigh.seniormap.ui.screens.guardian.components.*

/**
 * 보호인 홈 화면 컴포넌트
 * 
 * 보호인이 피보호인들의 현재 상태와 위치를 확인할 수 있는 메인 화면입니다.
 * 피보호인 목록과 각각의 상태 정보를 표시하며, 하단 네비게이션을 통해
 * 다른 기능들로 이동할 수 있습니다.
 * 
 * @param modifier 레이아웃 수정자
 * @param onNavigateToLogin 로그인 화면으로 이동하는 콜백
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuardianHomeScreen(
    modifier: Modifier = Modifier,
    onNavigateToLogin: () -> Unit = {},
    onNavigateToEdit: (GuardianData) -> Unit = {},
    onNavigateToAdd: () -> Unit = {} // 추가된 콜백
) {
    // 수정 모드 상태 추가
    var isEditMode by remember { mutableStateOf(false) }
    // 임시 데이터 - 실제로는 ViewModel에서 가져올 데이터
    val guardians = remember {
        listOf(
            GuardianData(
                id = "1",
                name = "김할자",
                location = "집",
                isAtHome = true
            ),
            GuardianData(
                id = "2", 
                name = "송진호",
                location = "마을",
                isAtHome = false
            ),
            GuardianData(
                id = "3",
                name = "나문희", 
                location = "집",
                isAtHome = true
            ),
            GuardianData(
                id = "4",
                name = "최불암",
                location = "마을",
                isAtHome = false
            )
        )
    }
    
    // 현재 선택된 하단 네비게이션 탭
    var selectedTab by remember { mutableIntStateOf(0) }
    
    Scaffold(
        modifier = modifier,
        topBar = {
            // 수정된 헤더 (수정 버튼 추가)
            GuardianHeader(
                title = "나의 피보호인",
                isEditMode = isEditMode,
                onEditClick = {
                    isEditMode = !isEditMode // 수정 모드 토글
                },
                onAddClick = {
                    onNavigateToAdd() // 피보호인 추가 화면으로 이동
                }
            )
        },
        bottomBar = {
            // 하단 네비게이션 바
            BottomNavigationBar(
                selectedTab = selectedTab,
                onTabSelected = { tabIndex ->
                    selectedTab = tabIndex
                    // TODO: 각 탭에 따른 화면 전환 로직
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (selectedTab) {
                0 -> {
                    // 홈 탭 - 피보호인 목록 (수정 모드 전달)
                    GuardianList(
                        guardians = guardians,
                        isEditMode = isEditMode,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 8.dp),
                        onGuardianClick = { guardian ->
                            // 일반 모드에서만 상세 화면 이동
                            if (!isEditMode) {
                                // TODO: 피보호인 상세 화면으로 이동
                            }
                        },
                        onGuardianEdit = { guardian ->
                            // 수정 화면으로 이동
                            onNavigateToEdit(guardian)
                        }
                    )
                }
                1 -> {
                    // 지도 탭 - 지도 화면 (임시)
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = androidx.compose.ui.Alignment.Center
                    ) {
                        Text(
                            text = "지도 화면 (구현 예정)",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
                2 -> {
                    // 설정 탭 - 설정 화면 (임시)
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = androidx.compose.ui.Alignment.Center
                    ) {
                        Text(
                            text = "설정 화면 (구현 예정)",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    }
}
package com.khigh.seniormap.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * 로딩 화면 컴포넌트
 * 
 * 앱 내에서 데이터 로딩이나 비동기 작업 진행 중에 표시되는 화면입니다.
 * 사용자에게 작업이 진행 중임을 알려주는 역할을 합니다.
 * 
 * @param modifier 레이아웃 수정자
 * @param message 로딩 중 표시할 메시지 (기본값: "로딩 중...")
 */
@Composable
fun LoadingScreen(
    modifier: Modifier = Modifier,
    message: String = "로딩 중..."
) {
    Log.d("com.khigh.seniormap", "[LoadingScreen] message: $message")
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // 로딩 인디케이터
            CircularProgressIndicator(
                modifier = Modifier.size(48.dp),
                color = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // 로딩 메시지
            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
} 
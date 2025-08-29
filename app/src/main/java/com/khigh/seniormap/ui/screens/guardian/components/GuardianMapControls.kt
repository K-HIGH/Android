package com.khigh.seniormap.ui.screens.guardian.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.unit.dp
import com.khigh.seniormap.R

/**
 * 보호인 지도 화면 컨트롤 버튼들 컴포넌트
 * 
 * @param onZoomIn 줌 인 버튼 클릭 콜백
 * @param onZoomOut 줌 아웃 버튼 클릭 콜백
 * @param onMyLocation 현재 위치 버튼 클릭 콜백
 * @param onFullScreen 전체 화면 버튼 클릭 콜백
 * @param modifier 레이아웃 수정자
 */
@Composable
fun GuardianMapControls(
    onZoomIn: () -> Unit,
    onZoomOut: () -> Unit,
    onMyLocation: () -> Unit,
    onFullScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // 줌 인 버튼
        IconButton(
            onClick = onZoomIn,
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(22.dp))
                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.9f))
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_plus_circle),
                contentDescription = "줌 인",
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(24.dp)
            )
        }
        
        // 줌 아웃 버튼
        IconButton(
            onClick = onZoomOut,
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(22.dp))
                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.9f))
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_minus_circle),
                contentDescription = "줌 아웃",
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(24.dp)
            )
        }
        
        // 현재 위치 버튼
        IconButton(
            onClick = onMyLocation,
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(22.dp))
                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.9f))
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_current_location),
                contentDescription = "현재 위치",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
        }
        
        // 전체 화면 버튼
        // IconButton(
        //     onClick = onFullScreen,
        //     modifier = Modifier
        //         .size(44.dp)
        //         .clip(RoundedCornerShape(22.dp))
        //         .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.9f))
        // ) {
        //     Icon(
        //         imageVector = Icons.Default.Fullscreen,
        //         contentDescription = "전체 화면",
        //         tint = MaterialTheme.colorScheme.onSurface,
        //         modifier = Modifier.size(24.dp)
        //     )
        // }
    }
} 
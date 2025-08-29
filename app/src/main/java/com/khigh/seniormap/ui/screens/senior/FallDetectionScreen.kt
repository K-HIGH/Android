package com.khigh.seniormap.ui.screens.senior

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

/**
 * 피보호인 낙상 감지 화면
 * Figma 디자인 기반: 낙상 감지 알림 + 자동 신고 안내
 */
@Composable
fun FallDetectionScreen(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit = {},
    onEmergencyCall: () -> Unit = {}
) {
    var countdown by remember { mutableIntStateOf(10) }
    var isCountdownActive by remember { mutableStateOf(true) }
    
    // 카운트다운 효과
    LaunchedEffect(isCountdownActive) {
        if (isCountdownActive) {
            while (countdown > 0) {
                delay(1000)
                countdown--
            }
            // 카운트다운이 끝나면 자동으로 신고
            if (countdown == 0) {
                onEmergencyCall()
            }
        }
    }
    
    // 애니메이션 효과
    val infiniteTransition = rememberInfiniteTransition(label = "warning")
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )
    
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.9f)
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 8.dp
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // 경고 아이콘
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .scale(scale)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.tertiary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "낙상 감지",
                        modifier = Modifier.size(60.dp),
                        tint = MaterialTheme.colorScheme.onTertiary
                    )
                }
                
                // 제목
                Text(
                    text = "낙상 감지",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    textAlign = TextAlign.Center
                )
                
                // 설명 메시지
                Text(
                    text = "보호자와 119에\n신고합니다.",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    textAlign = TextAlign.Center,
                    lineHeight = 28.sp
                )
                
                // 카운트다운
                if (isCountdownActive && countdown > 0) {
                    Card(
                        modifier = Modifier.size(80.dp),
                        shape = CircleShape,
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = countdown.toString(),
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                    
                    Text(
                        text = "${countdown}초 후 자동 신고됩니다",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        textAlign = TextAlign.Center
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // 버튼들
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // 취소 버튼
                    OutlinedButton(
                        onClick = {
                            isCountdownActive = false
                            onDismiss()
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.onErrorContainer
                        ),
                        border = ButtonDefaults.outlinedButtonBorder.copy(
                            brush = androidx.compose.foundation.BorderStroke(
                                2.dp,
                                MaterialTheme.colorScheme.onErrorContainer
                            ).brush
                        )
                    ) {
                        Text(
                            text = "괜찮아요",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    
                    // 즉시 신고 버튼
                    Button(
                        onClick = {
                            isCountdownActive = false
                            onEmergencyCall()
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error,
                            contentColor = MaterialTheme.colorScheme.onError
                        )
                    ) {
                        Text(
                            text = "즉시 신고",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                
                // 안내 문구
                Text(
                    text = "잘못된 감지인 경우 '괜찮아요'를 눌러주세요",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onErrorContainer.copy(alpha = 0.8f),
                    textAlign = TextAlign.Center,
                    lineHeight = 18.sp
                )
            }
        }
        
        // 배경 오버레이 (선택적)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.3f))
        )
    }
} 
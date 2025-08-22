package com.khigh.seniormap.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

/**
 * SeniorMap 앱의 라이트 컬러 스키마
 */
private val LightColorScheme = lightColorScheme(
    primary = SeniorMapGreen,
    onPrimary = SeniorMapTextPrimary,
    primaryContainer = SeniorMapGreen,
    onPrimaryContainer = SeniorMapTextPrimary,
    secondary = SeniorMapBlue,
    onSecondary = SeniorMapTextPrimary,
    tertiary = SeniorMapYellow,
    onTertiary = SeniorMapTextPrimary,
    background = SeniorMapBackground,
    onBackground = SeniorMapTextPrimary,
    surface = SeniorMapSurface,
    onSurface = SeniorMapTextPrimary,
    surfaceVariant = SeniorMapSurface,
    onSurfaceVariant = SeniorMapTextSecondary,
    error = SeniorMapError,
    onError = SeniorMapWhite,
    errorContainer = SeniorMapRed,
    onErrorContainer = SeniorMapWhite,
    outline = SeniorMapBorder,
    outlineVariant = SeniorMapBorder
)

/**
 * SeniorMap 앱의 다크 컬러 스키마
 */
private val DarkColorScheme = darkColorScheme(
    primary = SeniorMapGreen,
    onPrimary = SeniorMapTextPrimary,
    primaryContainer = SeniorMapGreen,
    onPrimaryContainer = SeniorMapTextPrimary,
    secondary = SeniorMapBlue,
    onSecondary = SeniorMapTextPrimary,
    tertiary = SeniorMapYellow,
    onTertiary = SeniorMapTextPrimary,
    background = SeniorMapBackground,
    onBackground = SeniorMapTextPrimary,
    surface = SeniorMapSurface,
    onSurface = SeniorMapTextPrimary,
    surfaceVariant = SeniorMapSurface,
    onSurfaceVariant = SeniorMapTextSecondary,
    error = SeniorMapError,
    onError = SeniorMapWhite,
    errorContainer = SeniorMapRed,
    onErrorContainer = SeniorMapWhite,
    outline = SeniorMapBorder,
    outlineVariant = SeniorMapBorder
)

/**
 * SeniorMap 앱의 메인 테마
 * 
 * @param darkTheme 다크 테마 사용 여부
 * @param dynamicColor 동적 컬러 사용 여부 (Android 12+)
 * @param content 테마를 적용할 컨텐츠
 */
@Composable
fun SeniorMapTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
} 
package com.khigh.seniormap.ui.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.khigh.seniormap.ui.composables.navigation.AppNavigation
import com.khigh.seniormap.ui.theme.SeniorMapTheme

/**
 * 메인 Activity
 * 앱의 진입점이며 네비게이션과 전체 테마를 관리
 */
class MainActivity : ComponentActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        setContent {
            SeniorMapTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainContent()
                }
            }
        }
    }
    
    @Composable
    private fun MainContent() {
        Scaffold(
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            AppNavigation(
                modifier = Modifier.padding(innerPadding),
                isAuthenticated = false,
                isLoading = false,
                errorMessage = null,
                onClearError = { }
            )
        }
    }
} 
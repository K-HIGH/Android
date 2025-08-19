package com.khigh.seniormap

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * SeniorMap 애플리케이션 클래스
 */
@HiltAndroidApp
class SeniorMapApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
        // 애플리케이션 초기화 로직
    }
} 
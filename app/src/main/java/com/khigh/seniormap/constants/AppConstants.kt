package com.khigh.seniormap.constants

/**
 * 앱 전반에서 사용하는 상수 정의
 */
object AppConstants {
    
    // API 관련 상수
    object Api {
        const val BASE_URL = "https://api.seniormap.com/"
        const val TIMEOUT_SECONDS = 30L
        const val RETRY_COUNT = 3
    }
    
    // OAuth 제공자
    object OAuthProvider {
        const val GOOGLE = "google"
        const val KAKAO = "kakao"
    }
    
    // 알림 관련 상수
    object Notification {
        const val CHANNEL_ID_LOCATION = "location_alerts"
        const val CHANNEL_ID_EMERGENCY = "emergency_alerts"
        const val CHANNEL_ID_GENERAL = "general_notifications"
        
        const val NOTIFICATION_ID_LOCATION = 1001
        const val NOTIFICATION_ID_EMERGENCY = 1002
        const val NOTIFICATION_ID_GENERAL = 1003
        
        const val FCM_TOKEN_REFRESH_INTERVAL_HOURS = 24L
    }
    
    // 위치 추적 관련 상수
    object Location {
        const val DEFAULT_UPDATE_INTERVAL_MS = 30000L // 30초
        const val FASTEST_UPDATE_INTERVAL_MS = 10000L // 10초
        const val MIN_DISTANCE_CHANGE_M = 10f // 10미터
        const val LOCATION_ACCURACY_THRESHOLD_M = 50f // 50미터
        
        const val GEOFENCE_RADIUS_M = 100f // 100미터
        const val GEOFENCE_EXPIRATION_MS = 24 * 60 * 60 * 1000L // 24시간
    }
    
    // 지도 관련 상수
    object Map {
        const val DEFAULT_ZOOM_LEVEL = 15f
        const val MIN_ZOOM_LEVEL = 10f
        const val MAX_ZOOM_LEVEL = 20f
        
        const val SEARCH_DEBOUNCE_DELAY_MS = 300L
        const val MAX_SEARCH_SUGGESTIONS = 5
        const val SEARCH_RADIUS_KM = 5.0
    }
    
    // SharedPreferences 키
    object PrefsKeys {
        const val ACCESS_TOKEN = "access_token"
        const val REFRESH_TOKEN = "refresh_token"
        const val USER_ID = "user_id"
        const val FCM_TOKEN = "fcm_token"
        const val NOTIFICATION_SETTINGS = "notification_settings"
        const val LOCATION_SETTINGS = "location_settings"
        const val LAST_KNOWN_LOCATION = "last_known_location"
    }
    
    // 데이터베이스 관련 상수
    object Database {
        const val DATABASE_NAME = "senior_map_db"
        const val DATABASE_VERSION = 1
        
        // 테이블명
        const val TABLE_USERS = "users"
        const val TABLE_LOCATIONS = "locations"
        const val TABLE_FAVORITES = "favorites"
        const val TABLE_NOTIFICATIONS = "notifications"
    }
    
    // 네트워크 상태
    object NetworkStatus {
        const val CONNECTED = "connected"
        const val DISCONNECTED = "disconnected"
        const val CONNECTING = "connecting"
    }
    
    // 에러 코드
    object ErrorCodes {
        const val NETWORK_ERROR = 1000
        const val AUTH_ERROR = 1001
        const val LOCATION_ERROR = 1002
        const val PERMISSION_ERROR = 1003
        const val SERVER_ERROR = 1004
        const val UNKNOWN_ERROR = 9999
    }
    
    // 권한 요청 코드
    object PermissionRequestCodes {
        const val LOCATION_PERMISSION = 1001
        const val NOTIFICATION_PERMISSION = 1002
        const val CAMERA_PERMISSION = 1003
        const val STORAGE_PERMISSION = 1004
    }
    
    // Intent 액션
    object IntentActions {
        const val LOCATION_UPDATE = "com.khigh.seniormap.LOCATION_UPDATE"
        const val EMERGENCY_ALERT = "com.khigh.seniormap.EMERGENCY_ALERT"
        const val NOTIFICATION_RECEIVED = "com.khigh.seniormap.NOTIFICATION_RECEIVED"
    }
    
    // 앱 설정
    object AppSettings {
        const val THEME_MODE_SYSTEM = "system"
        const val THEME_MODE_LIGHT = "light"
        const val THEME_MODE_DARK = "dark"
        
        const val LANGUAGE_SYSTEM = "system"
        const val LANGUAGE_KOREAN = "ko"
        const val LANGUAGE_ENGLISH = "en"
    }
} 
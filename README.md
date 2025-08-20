# Android
Kotlin

# 1 디버그 APK 빌드
./gradlew :app:assembleDebug

# 연결된 시뮬레이터에 설치까지 한다면?
./gradlew :app:installDebug

# 2 장치 확인 (PATH 없으면 절대경로)
~/Library/Android/sdk/platform-tools/adb devices

# 3 설치
~/Library/Android/sdk/platform-tools/adb install -r app/build/outputs/apk/debug/app-debug.apk

# 4 실행
adb shell am force-stop com.khigh.seniormap
~/Library/Android/sdk/platform-tools/adb shell am start -n "com.khigh.seniormap/com.khigh.seniormap.ui.activities.MainActivity"
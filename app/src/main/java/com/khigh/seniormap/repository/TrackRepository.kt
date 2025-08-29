package com.khigh.seniormap.repository

import com.khigh.seniormap.model.dto.track.TrackDto
import com.khigh.seniormap.model.dto.track.TrackUpdateReq

interface TrackRepository {
    
    // 온라인 API 호출
    suspend fun getTrack(userUlid: String): Result<TrackDto>
    suspend fun updateTrack(request: TrackUpdateReq): Result<TrackDto>
    
    // 오프라인 저장소 관련
    suspend fun saveTrackToLocal(track: TrackDto)
    suspend fun getTrackFromLocal(): TrackDto?
    suspend fun clearLocalTrack()
    
    // 동기화 관련
    suspend fun syncLocalTrackWithServer()
    suspend fun getOfflineTrack(): TrackDto?
} 
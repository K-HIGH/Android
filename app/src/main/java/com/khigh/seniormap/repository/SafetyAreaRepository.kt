package com.khigh.seniormap.repository

import com.khigh.seniormap.model.dto.safety.SafetyAreaCreateReq
import com.khigh.seniormap.model.dto.safety.SafetyAreaDto
import com.khigh.seniormap.model.dto.safety.SafetyAreaUpdateReq

interface SafetyAreaRepository {
    
    // 온라인 API 호출
    suspend fun getSafetyAreas(): Result<List<SafetyAreaDto>>
    suspend fun createSafetyArea(request: SafetyAreaCreateReq): Result<SafetyAreaDto>
    suspend fun updateSafetyArea(safetyAreaId: Int, request: SafetyAreaUpdateReq): Result<SafetyAreaDto>
    suspend fun deleteSafetyArea(safetyAreaId: Int): Result<Unit>
    
    // 오프라인 저장소 관련
    suspend fun saveSafetyAreasToLocal(safetyAreas: List<SafetyAreaDto>)
    suspend fun getSafetyAreasFromLocal(): List<SafetyAreaDto>
    suspend fun saveSafetyAreaToLocal(safetyArea: SafetyAreaDto)
    suspend fun deleteSafetyAreaFromLocal(safetyAreaId: Int)
    suspend fun clearLocalSafetyAreas()
    
    // 동기화 관련
    suspend fun syncLocalSafetyAreasWithServer()
    suspend fun getOfflineSafetyAreas(): List<SafetyAreaDto>
} 
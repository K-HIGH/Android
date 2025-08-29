package com.khigh.seniormap.repository

import com.khigh.seniormap.model.dto.caretaker.CaretakerCreateReq
import com.khigh.seniormap.model.dto.caretaker.CaretakerDto
import com.khigh.seniormap.model.dto.caretaker.CaretakerUpdateReq

interface CaretakerRepository {
    
    // 온라인 API 호출
    suspend fun getCaretakers(): Result<List<CaretakerDto>>
    suspend fun createCaretaker(request: CaretakerCreateReq): Result<CaretakerDto>
    suspend fun updateCaretaker(caretakerId: Int, request: CaretakerUpdateReq): Result<CaretakerDto>
    suspend fun deleteCaretaker(caretakerId: Int): Result<Unit>
    
    // 오프라인 저장소 관련
    suspend fun saveCaretakersToLocal(caretakers: List<CaretakerDto>)
    suspend fun getCaretakersFromLocal(): List<CaretakerDto>
    suspend fun saveCaretakerToLocal(caretaker: CaretakerDto)
    suspend fun deleteCaretakerFromLocal(caretakerId: Int)
    suspend fun clearLocalCaretakers()
    
    // 동기화 관련
    suspend fun syncLocalCaretakersWithServer()
    suspend fun getOfflineCaretakers(): List<CaretakerDto>
} 
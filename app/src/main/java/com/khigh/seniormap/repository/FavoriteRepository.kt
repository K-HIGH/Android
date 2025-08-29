package com.khigh.seniormap.repository

import com.khigh.seniormap.model.dto.favorite.LocationFavoriteCreateReq
import com.khigh.seniormap.model.dto.favorite.LocationFavoriteDto
import com.khigh.seniormap.model.dto.favorite.LocationFavoriteUpdateReq

interface FavoriteRepository {
    
    // 온라인 API 호출
    suspend fun getLocationFavorites(): Result<List<LocationFavoriteDto>>
    suspend fun createLocationFavorite(request: LocationFavoriteCreateReq): Result<LocationFavoriteDto>
    suspend fun updateLocationFavorite(locationId: Int, request: LocationFavoriteUpdateReq): Result<LocationFavoriteDto>
    suspend fun deleteLocationFavorite(locationId: Int): Result<Unit>
    
    // 오프라인 저장소 관련
    suspend fun saveFavoritesToLocal(favorites: List<LocationFavoriteDto>)
    suspend fun getFavoritesFromLocal(): List<LocationFavoriteDto>
    suspend fun saveFavoriteToLocal(favorite: LocationFavoriteDto)
    suspend fun deleteFavoriteFromLocal(locationId: Int)
    suspend fun clearLocalFavorites()
    
    // 동기화 관련
    suspend fun syncLocalFavoritesWithServer()
    suspend fun getOfflineFavorites(): List<LocationFavoriteDto>
} 
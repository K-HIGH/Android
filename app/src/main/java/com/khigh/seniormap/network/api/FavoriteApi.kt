package com.khigh.seniormap.network.api

import com.khigh.seniormap.model.dto.favorite.LocationFavoriteCreateReq
import com.khigh.seniormap.model.dto.favorite.LocationFavoriteDto
import com.khigh.seniormap.model.dto.favorite.LocationFavoriteUpdateReq
import retrofit2.Response
import retrofit2.http.*

interface FavoriteApi {
    
    @GET("/api/v1/locations/favorite/")
    suspend fun getLocationFavorites(
        @Header("Authorization") token: String
    ): Response<List<LocationFavoriteDto>>
    
    @POST("/api/v1/locations/favorite/")
    suspend fun createLocationFavorite(
        @Header("Authorization") token: String,
        @Body request: LocationFavoriteCreateReq
    ): Response<LocationFavoriteDto>
    
    @PUT("/api/v1/locations/favorite/{location_id}")
    suspend fun updateLocationFavorite(
        @Header("Authorization") token: String,
        @Path("location_id") locationId: Int,
        @Body request: LocationFavoriteUpdateReq
    ): Response<LocationFavoriteDto>
    
    @DELETE("/api/v1/locations/favorite/{location_id}")
    suspend fun deleteLocationFavorite(
        @Header("Authorization") token: String,
        @Path("location_id") locationId: Int
    ): Response<Unit>
} 
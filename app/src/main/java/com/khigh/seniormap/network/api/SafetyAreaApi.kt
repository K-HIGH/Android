package com.khigh.seniormap.network.api

import com.khigh.seniormap.model.dto.safety.SafetyAreaCreateReq
import com.khigh.seniormap.model.dto.safety.SafetyAreaDto
import com.khigh.seniormap.model.dto.safety.SafetyAreaUpdateReq
import retrofit2.Response
import retrofit2.http.*

interface SafetyAreaApi {
    
    @GET("/api/v1/locations/safety-areas/")
    suspend fun getSafetyAreas(
        @Header("Authorization") token: String
    ): Response<List<SafetyAreaDto>>
    
    @POST("/api/v1/locations/safety-areas/")
    suspend fun createSafetyArea(
        @Header("Authorization") token: String,
        @Body request: SafetyAreaCreateReq
    ): Response<SafetyAreaDto>
    
    @PUT("/api/v1/locations/safety-areas/{safety_area_id}")
    suspend fun updateSafetyArea(
        @Header("Authorization") token: String,
        @Path("safety_area_id") safetyAreaId: Int,
        @Body request: SafetyAreaUpdateReq
    ): Response<SafetyAreaDto>
    
    @DELETE("/api/v1/locations/safety-areas/{safety_area_id}")
    suspend fun deleteSafetyArea(
        @Header("Authorization") token: String,
        @Path("safety_area_id") safetyAreaId: Int
    ): Response<Unit>
} 
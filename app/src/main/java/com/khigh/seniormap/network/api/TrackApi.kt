package com.khigh.seniormap.network.api

import com.khigh.seniormap.model.dto.track.TrackDto
import com.khigh.seniormap.model.dto.track.TrackUpdateReq
import retrofit2.Response
import retrofit2.http.*

interface TrackApi {
    
    @GET("/api/v1/locations/track/{user_ulid}")
    suspend fun getTrack(
        @Header("Authorization") token: String,
        @Path("user_ulid") userUlid: String
    ): Response<TrackDto>
    
    @PUT("/api/v1/locations/track/")
    suspend fun updateTrack(
        @Header("Authorization") token: String,
        @Body request: TrackUpdateReq
    ): Response<TrackDto>
} 
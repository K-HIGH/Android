package com.khigh.seniormap.network.api

import com.khigh.seniormap.model.dto.caretaker.CaretakerCreateReq
import com.khigh.seniormap.model.dto.caretaker.CaretakerDto
import com.khigh.seniormap.model.dto.caretaker.CaretakerUpdateReq
import retrofit2.Response
import retrofit2.http.*

interface CaretakerApi {
    
    @GET("/api/v1/users/caretakers/")
    suspend fun getCaretakers(
        @Header("Authorization") token: String
    ): Response<List<CaretakerDto>>
    
    @POST("/api/v1/users/caretakers/")
    suspend fun createCaretaker(
        @Header("Authorization") token: String,
        @Body request: CaretakerCreateReq
    ): Response<CaretakerDto>
    
    @PUT("/api/v1/users/caretakers/{caretaker_id}")
    suspend fun updateCaretaker(
        @Header("Authorization") token: String,
        @Path("caretaker_id") caretakerId: Int,
        @Body request: CaretakerUpdateReq
    ): Response<CaretakerDto>
    
    @DELETE("/api/v1/users/caretakers/{caretaker_id}")
    suspend fun deleteCaretaker(
        @Header("Authorization") token: String,
        @Path("caretaker_id") caretakerId: Int
    ): Response<Unit>
} 
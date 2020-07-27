package com.rizkidzulkarnain.core.data.remote

import com.rizkidzulkarnain.core.domain.BaseApiResponse
import com.rizkidzulkarnain.core.domain.model.MaterialViewParam
import com.rizkidzulkarnain.core.domain.model.SavedMaterialViewParam
import com.rizkidzulkarnain.core.domain.requestbody.MaterialRequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * Created by Rizki Dzulkarnain on 7/23/2020.
 */
interface AppJardisService {
    @GET("Material/getAllMaterials")
    suspend fun getAllMaterials(): MaterialViewParam

    @POST("Material/saveCheckedMaterials")
    suspend fun saveCheckedMaterials(@Body requestBody: MaterialRequestBody): BaseApiResponse

    @GET("Material/getAllSavedMaterials")
    suspend fun getAllSavedMaterials(): SavedMaterialViewParam
}
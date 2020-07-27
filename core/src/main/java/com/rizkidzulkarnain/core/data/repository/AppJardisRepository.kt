package com.rizkidzulkarnain.core.data.repository

import com.rizkidzulkarnain.core.data.datasource.AppJardisDataSource
import com.rizkidzulkarnain.core.data.remote.AppJardisService
import com.rizkidzulkarnain.core.domain.BaseApiResponse
import com.rizkidzulkarnain.core.domain.model.MaterialViewParam
import com.rizkidzulkarnain.core.domain.model.SavedMaterialViewParam
import com.rizkidzulkarnain.core.domain.requestbody.MaterialRequestBody

/**
 * Created by Rizki Dzulkarnain on 7/23/2020.
 */
class AppJardisRepository(private val appJardisService: AppJardisService) : AppJardisDataSource {
    override suspend fun getAllMaterials(): MaterialViewParam {
        return appJardisService.getAllMaterials()
    }

    override suspend fun saveCheckedMaterials(requestBody: MaterialRequestBody): BaseApiResponse {
        return appJardisService.saveCheckedMaterials(requestBody)
    }

    override suspend fun getAllSavedMaterials(): SavedMaterialViewParam {
        return appJardisService.getAllSavedMaterials()
    }
}
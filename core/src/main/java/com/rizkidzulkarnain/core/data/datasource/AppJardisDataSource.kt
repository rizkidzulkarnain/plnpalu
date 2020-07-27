package com.rizkidzulkarnain.core.data.datasource

import com.rizkidzulkarnain.core.domain.BaseApiResponse
import com.rizkidzulkarnain.core.domain.model.MaterialViewParam
import com.rizkidzulkarnain.core.domain.model.SavedMaterialViewParam
import com.rizkidzulkarnain.core.domain.requestbody.MaterialRequestBody

/**
 * Created by Rizki Dzulkarnain on 7/23/2020.
 */
interface AppJardisDataSource {
    suspend fun getAllMaterials(): MaterialViewParam

    suspend fun saveCheckedMaterials(requestBody: MaterialRequestBody): BaseApiResponse

    suspend fun getAllSavedMaterials(): SavedMaterialViewParam
}
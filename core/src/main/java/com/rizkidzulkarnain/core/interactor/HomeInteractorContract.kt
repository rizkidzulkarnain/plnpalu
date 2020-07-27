package com.rizkidzulkarnain.core.interactor

import com.rizkidzulkarnain.core.domain.BaseApiResponse
import com.rizkidzulkarnain.core.domain.Result
import com.rizkidzulkarnain.core.domain.model.MaterialViewParam
import com.rizkidzulkarnain.core.domain.model.SavedMaterialViewParam
import com.rizkidzulkarnain.core.domain.requestbody.MaterialRequestBody

/**
 * Created by Rizki Dzulkarnain on 7/25/2020.
 */
interface HomeInteractorContract {
    suspend fun getAllMaterials(): Result<MaterialViewParam>

    suspend fun saveCheckedMaterials(reqBody: MaterialRequestBody): Result<BaseApiResponse>

    suspend fun getAllSavedMaterials(): Result<SavedMaterialViewParam>
}
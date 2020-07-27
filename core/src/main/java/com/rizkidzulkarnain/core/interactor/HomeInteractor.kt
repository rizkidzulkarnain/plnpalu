package com.rizkidzulkarnain.core.interactor

import com.rizkidzulkarnain.core.data.datasource.AppJardisDataSource
import com.rizkidzulkarnain.core.domain.BaseApiResponse
import com.rizkidzulkarnain.core.domain.Result
import com.rizkidzulkarnain.core.domain.model.MaterialViewParam
import com.rizkidzulkarnain.core.domain.model.SavedMaterialViewParam
import com.rizkidzulkarnain.core.domain.requestbody.MaterialRequestBody
import com.rizkidzulkarnain.core.util.getError

/**
 * Created by Rizki Dzulkarnain on 7/25/2020.
 */
class HomeInteractor(private val appJardisDataSource: AppJardisDataSource) :
    HomeInteractorContract {

    override suspend fun getAllMaterials(): Result<MaterialViewParam> {
        return try {
            val result = appJardisDataSource.getAllMaterials()
            result.getResult(result)
        } catch (e: Throwable) {
            e.getError()
        }
    }

    override suspend fun saveCheckedMaterials(reqBody: MaterialRequestBody): Result<BaseApiResponse> {
        return try {
            val result = appJardisDataSource.saveCheckedMaterials(reqBody)
            result.getResult(result)
        } catch (e: Throwable) {
            e.getError()
        }
    }

    override suspend fun getAllSavedMaterials(): Result<SavedMaterialViewParam> {
        return try {
            val result = appJardisDataSource.getAllSavedMaterials()
            result.getResult(result)
        } catch (e: Throwable) {
            e.getError()
        }
    }
}
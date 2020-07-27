package com.rizkidzulkarnain.core.domain.model

import com.google.gson.annotations.SerializedName
import com.rizkidzulkarnain.core.domain.BaseApiResponse

/**
 * Created by Rizki Dzulkarnain on 7/25/2020.
 */
class SavedMaterialViewParam(val data: List<SavedMaterial>) : BaseApiResponse() {
    class SavedMaterial(
        val id: String,
        val latitude: String,
        val longitude: String,
        @SerializedName("code_material")
        val codeMaterial: String
    )
}
package com.rizkidzulkarnain.core.domain.requestbody

import com.rizkidzulkarnain.core.domain.model.MaterialViewParam

/**
 * Created by Rizki Dzulkarnain on 7/26/2020.
 */
class MaterialRequestBody(
    val latitude: String, val longitude: String,
    val materials: List<MaterialViewParam.Material>
)
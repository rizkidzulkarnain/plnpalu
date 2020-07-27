package com.rizkidzulkarnain.core.domain.model

import com.rizkidzulkarnain.core.domain.BaseApiResponse

/**
 * Created by Rizki Dzulkarnain on 7/25/2020.
 */
class MaterialViewParam(val data: List<Material>?) : BaseApiResponse() {
    class Material(val id: Int, val code: String, val name: String, var isChecked: Boolean = false)
}
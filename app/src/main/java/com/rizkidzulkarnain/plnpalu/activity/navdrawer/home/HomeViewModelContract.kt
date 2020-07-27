package com.rizkidzulkarnain.plnpalu.activity.navdrawer.home

import androidx.lifecycle.LiveData
import com.google.android.gms.maps.model.Marker
import com.rizkidzulkarnain.core.domain.model.MaterialViewParam
import com.rizkidzulkarnain.core.domain.model.SavedMaterialViewParam

/**
 * Created by Rizki Dzulkarnain on 7/25/2020.
 */
interface HomeViewModelContract {
    fun onGetGPSLocation(latitude: Double, longitude: Double)
    fun onViewLoaded()
    fun onSaveFabButtonClicked()
    fun onMarkerClicked(selectedMarker: Marker)
    fun onSaveDialogClicked(materials: List<MaterialViewParam.Material>)

    fun doShowAllSavedMaterialsLiveData(): LiveData<SavedMaterialViewParam>
    fun doShowGoogleMapLiveData(): LiveData<Pair<Double, Double>>
    fun doShowLoadingLiveData(): LiveData<Boolean>
    fun doShowErrorLiveData(): LiveData<Pair<String, String>>
    fun doShowSuccessSaveMaterialLiveData(): LiveData<String>
    fun doShowDialogMaterialLiveData(): LiveData<Triple<List<MaterialViewParam.Material>, Boolean, Marker?>>
}
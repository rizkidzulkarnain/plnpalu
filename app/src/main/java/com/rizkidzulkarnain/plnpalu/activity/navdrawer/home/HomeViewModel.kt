package com.rizkidzulkarnain.plnpalu.activity.navdrawer.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.Marker
import com.rizkidzulkarnain.core.domain.BaseApiResponse.Companion.ERROR
import com.rizkidzulkarnain.core.domain.Result
import com.rizkidzulkarnain.core.domain.model.MaterialViewParam
import com.rizkidzulkarnain.core.domain.model.SavedMaterialViewParam
import com.rizkidzulkarnain.core.domain.requestbody.MaterialRequestBody
import com.rizkidzulkarnain.core.interactor.HomeInteractorContract
import com.rizkidzulkarnain.movieapps.thread.AppSchedulerProviderContract
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(
    private val interactor: HomeInteractorContract,
    private val schedulerProvider: AppSchedulerProviderContract
) : ViewModel(), HomeViewModelContract {

    private var myLocationLatitude = 0.0
    private var myLocationLongitude = 0.0

    private val showAllSavedMaterialsLiveData = MutableLiveData<SavedMaterialViewParam>()
    private val showGoogleMapLiveData = MutableLiveData<Pair<Double, Double>>()
    private val showDialogMaterialLiveData =
        MutableLiveData<Triple<List<MaterialViewParam.Material>, Boolean, Marker?>>()
    private val showLoadingLiveData = MutableLiveData<Boolean>()
    private val showErrorLiveData = MutableLiveData<Pair<String, String>>()
    private val showSuccessSaveMaterialLiveData = MutableLiveData<String>()

    override fun onViewLoaded() {
        GlobalScope.launch(schedulerProvider.ui()) {
            showLoadingLiveData.value = true

            when (val result = withContext(schedulerProvider.io()) {
                interactor.getAllSavedMaterials()
            }) {
                is Result.Success -> {
                    showAllSavedMaterialsLiveData.value = result.value
                }

                is Result.Error -> {
                    showErrorLiveData.value = Pair("Belum ada data map", ERROR)
                }
            }

            showLoadingLiveData.value = false
        }
    }

    override fun onGetGPSLocation(latitude: Double, longitude: Double) {
        this.myLocationLatitude = latitude
        this.myLocationLongitude = longitude
        showGoogleMapLiveData.value = Pair(latitude, longitude)
    }

    override fun onSaveFabButtonClicked() {
        getListMaterial(false)
    }

    override fun onMarkerClicked(selectedMarker: Marker) {
        myLocationLatitude = selectedMarker.position.latitude
        myLocationLongitude = selectedMarker.position.longitude
        getListMaterial(true, selectedMarker)
    }

    private fun getListMaterial(isFormMarkerClicked: Boolean, selectedMarker: Marker? = null) {
        GlobalScope.launch(schedulerProvider.ui()) {
            showLoadingLiveData.value = true

            when (val result = withContext(schedulerProvider.io()) {
                interactor.getAllMaterials()
            }) {
                is Result.Success -> {
                    showDialogMaterialLiveData.value =
                        Triple(
                            result.value.data ?: mutableListOf(),
                            isFormMarkerClicked,
                            selectedMarker
                        )
                }

                is Result.Error -> {
                    showErrorLiveData.value = Pair(result.exception.message.orEmpty(), ERROR)
                }
            }

            showLoadingLiveData.value = false
        }
    }

    override fun onSaveDialogClicked(materials: List<MaterialViewParam.Material>) {
        val checkedMaterials = materials.filter { it.isChecked }
        if (checkedMaterials.isNotEmpty()) {
            saveMaterials(checkedMaterials)
        } else {
            showErrorLiveData.value = Pair(
                "Gagal menyimpan !, tidak ada item material yang dipilih",
                ERROR
            )
        }
    }

    private fun saveMaterials(checkedMaterials: List<MaterialViewParam.Material>) {
        GlobalScope.launch(schedulerProvider.ui()) {
            showLoadingLiveData.value = true

            when (val result = withContext(schedulerProvider.io()) {
                interactor.saveCheckedMaterials(
                    MaterialRequestBody(
                        myLocationLatitude.toString(),
                        myLocationLongitude.toString(),
                        checkedMaterials
                    )
                )
            }) {
                is Result.Success -> {
                    showSuccessSaveMaterialLiveData.value = "Berhasil Menyimpan material !"
                }

                is Result.Error -> {
                    showErrorLiveData.value = Pair(result.exception.message.orEmpty(), ERROR)
                }
            }
            showLoadingLiveData.value = false
        }
    }

    override fun doShowAllSavedMaterialsLiveData() = showAllSavedMaterialsLiveData
    override fun doShowGoogleMapLiveData() = showGoogleMapLiveData
    override fun doShowLoadingLiveData() = showLoadingLiveData
    override fun doShowErrorLiveData() = showErrorLiveData
    override fun doShowDialogMaterialLiveData() = showDialogMaterialLiveData
    override fun doShowSuccessSaveMaterialLiveData() = showSuccessSaveMaterialLiveData
}
package com.rizkidzulkarnain.plnpalu.activity.navdrawer.home

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import com.rizkidzulkarnain.core.domain.BaseApiResponse
import com.rizkidzulkarnain.core.domain.BaseApiResponse.Companion.ERROR
import com.rizkidzulkarnain.core.domain.BaseApiResponse.Companion.SUCCESS
import com.rizkidzulkarnain.core.domain.model.MaterialViewParam
import com.rizkidzulkarnain.core.domain.model.SavedMaterialViewParam
import com.rizkidzulkarnain.plnpalu.R
import com.rizkidzulkarnain.plnpalu.adapter.MarkerInfoWindowAdapter
import com.rizkidzulkarnain.plnpalu.databinding.FragmentHomeBinding
import org.koin.android.viewmodel.ext.android.viewModel
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import pub.devrel.easypermissions.PermissionRequest

class HomeFragment : Fragment(), EasyPermissions.PermissionCallbacks {

    companion object {
        private const val REQUEST_CODE_LOCATION = 100
        private val LOCATION = arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    private val viewModel by viewModel<HomeViewModel>()
    private lateinit var binding: FragmentHomeBinding

    private var googleMap: GoogleMap? = null
    private var fusedLocation: FusedLocationProviderClient? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        initUIMap()
        initFabButton()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.onViewLoaded()
        checkLocationPermission()
        subscribeLiveData()
    }

    private fun initFabButton() {
        binding.fab.setOnClickListener {
            viewModel.onSaveFabButtonClicked()
        }
    }

    private fun subscribeLiveData() {
        with(viewModel) {
            doShowAllSavedMaterialsLiveData().observe(requireActivity(), Observer {
                showAllSavedMaterials(it)
            })

            doShowGoogleMapLiveData().observe(requireActivity(), Observer {
                val (lat, long) = Pair(it.first, it.second)
                showGoogleMap(lat, long)
            })

            doShowLoadingLiveData().observe(requireActivity(), Observer {
                showLoading(it)
            })

            doShowDialogMaterialLiveData().observe(requireActivity(), Observer {
                val (materials, isFormMarkerClicked, selectedMarker) = Triple(
                    it.first,
                    it.second,
                    it.third
                )
                showDialogMaterial(materials.toMutableList(), isFormMarkerClicked, selectedMarker)
            })

            doShowErrorLiveData().observe(requireActivity(), Observer {
                val (errorCode, type) = Pair(it.first, it.second)
                showError(errorCode, type)
            })

            doShowSuccessSaveMaterialLiveData().observe(requireActivity(), Observer {
                initUIMap()
                onViewLoaded()
                showError(it, SUCCESS)
            })
        }
    }

    private fun showDialogMaterial(
        materials: MutableList<MaterialViewParam.Material>,
        isFromMarkerCLicked: Boolean,
        selectedMarker: Marker?
    ) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Pilih Material")

        val arrMaterial = mutableListOf<String>()
        val arrMaterialChecked = mutableListOf<Boolean>()

        materials.forEach {
            arrMaterial.add(it.code)
            arrMaterialChecked.add(false)
        }

        builder.setMultiChoiceItems(
            arrMaterial.toTypedArray(), arrMaterialChecked.toBooleanArray()
        ) { dialog, index, isChecked ->
            materials[index].isChecked = isChecked
        }

        builder.setPositiveButton("SAVE") { dialog, index ->
            viewModel.onSaveDialogClicked(materials)
            dialog.dismiss()
        }
        builder.setNegativeButton("Cancel", null)

        if (isFromMarkerCLicked) {
            builder.setNeutralButton("Remove Marker") { dialog, index ->
                selectedMarker?.remove()
            }
        }

        val dialog = builder.create()
        dialog.show()
    }

    private fun showLoading(isShowLoading: Boolean) {
        with(binding.viewLoading.root) {
            visibility = if (isShowLoading) View.VISIBLE else View.GONE
        }
    }

    private fun showError(errorCode: String, type: String) {
        val errorMessage = when (errorCode) {
            BaseApiResponse.NETWORK_ERROR -> getString(R.string.network_error_message)
            BaseApiResponse.SERVER_ERROR, BaseApiResponse.GENERAL_ERROR -> getString(R.string.general_error_message)
            else -> errorCode
        }

        Snackbar.make(binding.root, errorMessage, Snackbar.LENGTH_LONG).apply {
            view.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(), if (type.equals(ERROR, true))
                        R.color.red_E57373 else R.color.green_4DB6AC
                )
            )
            show()
        }
    }

    private fun showGoogleMap(latitude: Double, longitude: Double) {
        val myCurrentLocation = LatLng(latitude, longitude)
        googleMap?.run {
            try {
                isMyLocationEnabled = true
            } catch (ex: SecurityException) {
                ex.printStackTrace()
            }
            moveCamera(CameraUpdateFactory.newLatLng(myCurrentLocation))
            animateCamera(CameraUpdateFactory.zoomTo(25f))
        }
    }

    private fun initUIMap() {
        var mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync {
            googleMap = it
            googleMap?.run {
                setOnMapClickListener { latLong ->
                    addMarker(MarkerOptions().position(latLong).snippet(""))
                }

                setOnInfoWindowClickListener { marker ->
                    if (marker.snippet.isNullOrBlank()) {
                        viewModel.onMarkerClicked(marker)
                    }
                    marker.hideInfoWindow()
                }

                googleMap?.setInfoWindowAdapter(MarkerInfoWindowAdapter(requireContext()))
            }
        }
    }

    private fun showAllSavedMaterials(savedMaterials: SavedMaterialViewParam) {
        val materialMap = savedMaterials.data.groupBy { it.id }
        materialMap.forEach { map ->
            val content = map.value.joinToString("\n") { it.codeMaterial }
            map.value.firstOrNull()?.let {
                val latLong =
                    LatLng(it.latitude.toDouble(), it.longitude.toDouble())
                googleMap?.run {
                    addMarker(MarkerOptions().position(latLong).snippet(content))
                }
            }
        }
    }

    private fun requestPermission() {
        EasyPermissions.requestPermissions(
            PermissionRequest.Builder(
                this@HomeFragment,
                REQUEST_CODE_LOCATION, *LOCATION
            )
                .setRationale(R.string.location_permission_setting_description)
                .setPositiveButtonText(R.string.location_permission_setting_positive)
                .setNegativeButtonText(R.string.location_permission_setting_negative)
                .setTheme(R.style.LocationTheme)
                .build()
        )
    }

    private fun hasPermissionLocation(): Boolean {
        return EasyPermissions.hasPermissions(requireActivity(), *LOCATION)
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            if (perms.containsAll(LOCATION.toList())) {
                AppSettingsDialog.Builder(this@HomeFragment)
                    .setTitle(R.string.location_permission_setting_title)
                    .setRationale(R.string.location_permission_setting_description)
                    .setPositiveButton(R.string.location_permission_setting_positive)
                    .setNegativeButton(R.string.location_permission_setting_negative)
                    .setThemeResId(R.style.LocationTheme)
                    .build()
                    .show()
            }
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        if (hasPermissionLocation()) {
            initFusedLocation()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(
            requestCode, permissions, grantResults,
            this
        )
    }

    private fun checkPlayServices(): Boolean {
        val apiAvailability = GoogleApiAvailability.getInstance()
        val resultCode = apiAvailability.isGooglePlayServicesAvailable(requireActivity())

        if (resultCode != ConnectionResult.SUCCESS) {
            Toast.makeText(
                requireActivity(),
                getString(R.string.location_permission_play_service_not_installed),
                Toast.LENGTH_LONG
            ).show()
            return false
        }
        return true
    }

    private fun checkLocationPermission() {
        when (hasPermissionLocation()) {
            true -> {
                initFusedLocation()
            }
            false -> requestPermission()
        }
    }

    @SuppressLint("missingPermission")
    private fun initFusedLocation() {
        fusedLocation = LocationServices.getFusedLocationProviderClient(requireActivity())
        fusedLocation?.lastLocation?.addOnSuccessListener(requireActivity()) { location ->
            location?.let {
                viewModel.onGetGPSLocation(it.latitude, it.longitude)
            }
        }
    }
}
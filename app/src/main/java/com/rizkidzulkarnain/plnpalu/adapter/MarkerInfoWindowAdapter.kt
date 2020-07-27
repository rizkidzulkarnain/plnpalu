package com.rizkidzulkarnain.plnpalu.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.DataBindingUtil
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter
import com.google.android.gms.maps.model.Marker
import com.rizkidzulkarnain.plnpalu.R
import com.rizkidzulkarnain.plnpalu.databinding.ItemMarkerInfoWindowBinding


/**
 * Created by Rizki Dzulkarnain on 7/26/2020.
 */
class MarkerInfoWindowAdapter(context: Context) :
    InfoWindowAdapter {
    private var binding: ItemMarkerInfoWindowBinding = DataBindingUtil.inflate(
        LayoutInflater.from(context), R.layout.item_marker_info_window,
        null, false
    )

    override fun getInfoContents(marker: Marker): View {
        binding.tvContent.text = marker.snippet
        return binding.root
    }

    override fun getInfoWindow(marker: Marker?): View? {
        return null
    }
}
package com.rizkidzulkarnain.plnpalu.activity.navdrawer.waypoint

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.rizkidzulkarnain.plnpalu.R

class WayPointFragment : Fragment() {

    private lateinit var wayPointViewModel: WayPointViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        wayPointViewModel =
            ViewModelProviders.of(this).get(WayPointViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_way_point, container, false)
        val textView: TextView = root.findViewById(R.id.text_gallery)
        wayPointViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}
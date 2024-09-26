package com.example.sendlocationapp.fragments.home

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startForegroundService
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.Visibility
import com.example.sendlocationapp.Constant.ACTION_STOP_SERVICE
import com.example.sendlocationapp.Constant.REQUEST_CODE_LOCATION
import com.example.sendlocationapp.R
import com.example.sendlocationapp.databinding.FragmentHomeBinding
import com.example.sendlocationapp.service.LocationForegroundService
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private var adapter: UserLocationAdapter? = null

    private var binding: FragmentHomeBinding? = null
    private val homeViewModel: HomeViewModel by viewModels()

    private val sharedPreferences by lazy {
        requireContext().getSharedPreferences("location_state", Context.MODE_PRIVATE)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)
        binding?.let { view ->
            view.rvLocations.layoutManager = LinearLayoutManager(requireContext())
            adapter = UserLocationAdapter(list = listOf())
            view.rvLocations.adapter = adapter

            val isServiceRunning = sharedPreferences.getBoolean("service_running", false)
            if (isServiceRunning) {
                view.btnStartForeground.visibility = View.INVISIBLE
                view.btnStopForeground.visibility = View.VISIBLE
            } else {
                view.btnStartForeground.visibility = View.VISIBLE
                view.btnStopForeground.visibility = View.INVISIBLE
            }
            view.btnStartForeground.setOnClickListener {
                requestLocationPermission()
                updateButtonState(true)
            }

            view.btnStopForeground.setOnClickListener {

                stopLocationForegroundService()
                updateButtonState(false)
            }

            view.btnSeeLogs.setOnClickListener {
                homeViewModel.getAllLocation()
                homeViewModel.userLocation.observe(viewLifecycleOwner) { list ->
                    adapter?.updateList(list)
                }
            }

            view.btnClearLogs.setOnClickListener {
                homeViewModel.clearAllLocation()
                adapter?.updateList(listOf())
            }
        }
    }

    private fun requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE_LOCATION)
        } else {
            startLocationForegroundService()
        }
    }

    private fun startLocationForegroundService() {
        val intent = Intent(requireContext(), LocationForegroundService::class.java)
        startForegroundService(requireContext(), intent)
    }

    private fun stopLocationForegroundService() {
        val intent = Intent(requireContext(), LocationForegroundService::class.java)
        intent.action = ACTION_STOP_SERVICE
        startForegroundService(requireContext(), intent)
    }

    private fun updateButtonState(isServiceRunning: Boolean) {
        binding?.let { view ->
            view.btnStartForeground.visibility = if (isServiceRunning) View.GONE else View.VISIBLE
            view.btnStopForeground.visibility = if (isServiceRunning) View.VISIBLE else View.GONE
            sharedPreferences.edit().putBoolean("service_running", isServiceRunning).apply()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
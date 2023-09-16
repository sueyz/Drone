package com.example.drone.view.ui.map

import android.content.BroadcastReceiver
import android.content.Context.BATTERY_SERVICE
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ActivityInfo
import android.os.BatteryManager
import android.os.Bundle
import android.view.View
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.example.drone.R
import com.example.drone.base.BaseFragment
import com.example.drone.databinding.FragmentMapPageBinding
import com.example.drone.view.extensions.BatteryManagerBroadcastReceiver
import com.example.drone.view.ui.home.HomeViewModel

class MapPage : BaseFragment<FragmentMapPageBinding, HomeViewModel>() {

    private lateinit var broadcastReceiver: BroadcastReceiver

    private val batteryManager: BatteryManager by lazy {
        activityContext.getSystemService(BATTERY_SERVICE) as BatteryManager
    }


    override val layoutId: Int = R.layout.fragment_map_page
    //by viewmodel scope
    override val viewModel: HomeViewModel by viewModels()

    override fun init(savedInstanceState: Bundle?) {
        val window = activityContext.window

        val windowInsetsController =
            WindowCompat.getInsetsController(window, window.decorView)

        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())

    }

    override fun initView(savedInstanceState: Bundle?) {}

    override fun setupView(view: View) {
        navController = Navigation.findNavController(view)

        binding.ivHome.setOnClickListener {
            navController.popBackStack()
        }

        broadcastReceiver = BatteryManagerBroadcastReceiver {
            val propertyCapacity = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)

            binding.tvBattery.text = "$propertyCapacity %"
            binding.ivBattery.setImageLevel(propertyCapacity)
        }

        val filter = IntentFilter().apply {
            addAction(Intent.ACTION_BATTERY_CHANGED)
        }
        activityContext.registerReceiver(broadcastReceiver, filter)

    }

    override fun onResume() {
        super.onResume()
        activityContext.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
    }

    override fun onPause() {
        super.onPause()
        activityContext.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED

    }

    override fun onDestroy() {
        super.onDestroy()

        activityContext.unregisterReceiver(broadcastReceiver)
    }

    private fun setBattery(propertyCapacity: Int) {
        binding.tvBattery.text = propertyCapacity.toString()
        binding.ivBattery.setImageLevel(propertyCapacity)
    }
}
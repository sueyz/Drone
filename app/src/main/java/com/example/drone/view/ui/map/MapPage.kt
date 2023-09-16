package com.example.drone.view.ui.map

import android.content.BroadcastReceiver
import android.content.Context.BATTERY_SERVICE
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ActivityInfo
import android.os.BatteryManager
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.PopupWindow
import android.widget.Spinner
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.example.drone.R
import com.example.drone.base.BaseFragment
import com.example.drone.databinding.FragmentMapPageBinding
import com.example.drone.util.BatteryManagerBroadcastReceiver
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
            val propertyCapacity =
                batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)

            binding.tvBattery.text = "$propertyCapacity %"
            binding.ivBattery.setImageLevel(propertyCapacity)
        }

        val filter = IntentFilter().apply {
            addAction(Intent.ACTION_BATTERY_CHANGED)
        }
        activityContext.registerReceiver(broadcastReceiver, filter)


        binding.spFlightMode.adapter = ArrayAdapter(
            activityContext,
            android.R.layout.simple_spinner_item, resources.getStringArray(R.array.flight_mode)
        )
        binding.spStatus.adapter = ArrayAdapter(
            activityContext,
            android.R.layout.simple_spinner_item, resources.getStringArray(R.array.flight_status)
        )

        binding.spFlightMode.avoidDropdownFocus()
        binding.spStatus.avoidDropdownFocus()

        binding.spStatus.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?, position: Int, id: Long
            ) {
                if (position == 0) {
                    binding.spStatus.background =
                        ContextCompat.getDrawable(activityContext, R.drawable.shape_gradient_white)
                    binding.ivShapeTwo.setColorFilter(
                        ContextCompat.getColor(
                            activityContext,
                            R.color.silver
                        )
                    )
                } else {
                    binding.spStatus.background =
                        ContextCompat.getDrawable(activityContext, R.drawable.shape_gradient_yellow)
                    binding.ivShapeTwo.setColorFilter(
                        ContextCompat.getColor(
                            activityContext,
                            R.color.yellow
                        )
                    )
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }

        binding.ibLanding.isEnabled = false


        binding.ibTakeoff.setOnClickListener {
            binding.main.background =
                ContextCompat.getDrawable(activityContext, R.drawable.map_view)
            binding.ibLanding.isEnabled = true
            binding.ibTakeoff.isEnabled = false
            binding.ibTakeoff.setColorFilter(
                ContextCompat.getColor(
                    activityContext,
                    R.color.silver
                )
            )
            binding.ibLanding.setColorFilter(
                ContextCompat.getColor(
                    activityContext,
                    R.color.white
                )
            )

            binding.spStatus.setSelection(1)
            binding.spFlightMode.setSelection(1)
        }

        binding.ibLanding.setOnClickListener {
            binding.main.background =
                ContextCompat.getDrawable(activityContext, R.drawable.gradient_default)
            binding.ibLanding.isEnabled = false
            binding.ibTakeoff.isEnabled = true
            binding.ibLanding.setColorFilter(
                ContextCompat.getColor(
                    activityContext,
                    R.color.silver
                )
            )
            binding.ibTakeoff.setColorFilter(
                ContextCompat.getColor(
                    activityContext,
                    R.color.white
                )
            )
            binding.spStatus.setSelection(0)
            binding.spFlightMode.setSelection(0)
        }

        binding.ibStart.setOnClickListener {
            Toast.makeText(activityContext, "Mission Start not yet implemented!", Toast.LENGTH_SHORT).show()
        }

        binding.ivMore.setOnClickListener {
            binding.llSettings.visibility = View.VISIBLE
        }

        binding.ibClose.setOnClickListener {
            binding.llSettings.visibility = View.INVISIBLE
        }

        //Recyclerview
        val data = ArrayList<String>()
        for (i in 1..20) {
            data.add("Settings $i")
        }
        binding.rvSettings.adapter = SettingsAdapter(data)


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

    private fun Spinner.avoidDropdownFocus() {
        try {
            val isAppCompat = this is androidx.appcompat.widget.AppCompatSpinner
            val spinnerClass =
                if (isAppCompat) androidx.appcompat.widget.AppCompatSpinner::class.java else Spinner::class.java
            val popupWindowClass =
                if (isAppCompat) androidx.appcompat.widget.ListPopupWindow::class.java else android.widget.ListPopupWindow::class.java

            val listPopup = spinnerClass
                .getDeclaredField("mPopup")
                .apply { isAccessible = true }
                .get(this)
            if (popupWindowClass.isInstance(listPopup)) {
                val popup = popupWindowClass
                    .getDeclaredField("mPopup")
                    .apply { isAccessible = true }
                    .get(listPopup)
                if (popup is PopupWindow) {
                    popup.isFocusable = false
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
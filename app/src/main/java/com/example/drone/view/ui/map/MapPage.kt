package com.example.drone.view.ui.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context.BATTERY_SERVICE
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.BatteryManager
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.PopupWindow
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.preference.PreferenceManager
import com.example.drone.R
import com.example.drone.base.BaseFragment
import com.example.drone.databinding.FragmentMapPageBinding
import com.example.drone.util.BatteryManagerBroadcastReceiver
import com.example.drone.view.ui.home.HomeViewModel
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay


class MapPage : BaseFragment<FragmentMapPageBinding, HomeViewModel>(), LocationListener {

    private lateinit var broadcastReceiver: BroadcastReceiver
    private lateinit var locationManager: LocationManager

    private var currentLocation: GeoPoint = GeoPoint(2.909368, 101.655304)

    private val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->

            if (isGranted) { // permission is granted

                getLocation()

            } else {
                // handle permission denial
            }
        }


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
            R.layout.spinner_text, resources.getStringArray(R.array.flight_mode)
        )
        binding.spStatus.adapter = ArrayAdapter(
            activityContext,
            R.layout.spinner_text, resources.getStringArray(R.array.flight_status)
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

            binding.llTelemetry.visibility = View.VISIBLE

            binding.ivExitMap.background =
                ContextCompat.getDrawable(activityContext, R.drawable.map_view)
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

            binding.llTelemetry.visibility = View.INVISIBLE

            binding.ivExitMap.background =
                ContextCompat.getDrawable(activityContext, R.drawable.gradient_default)
        }

        binding.ibStart.setOnClickListener {
            Toast.makeText(
                activityContext,
                "Mission Start not yet implemented!",
                Toast.LENGTH_SHORT
            ).show()
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

        binding.main.setOnClickListener {
            binding.llSettings.visibility = View.INVISIBLE
        }

        binding.osmmap.overlays.add(MapEventsOverlay(object : MapEventsReceiver {
            override fun singleTapConfirmedHelper(p: GeoPoint): Boolean {
                binding.flOsmmapLarge.visibility = View.VISIBLE
                return false
            }

            override fun longPressHelper(p: GeoPoint): Boolean {
                return false
            }
        }))


        binding.ivExitMap.setOnClickListener {
            binding.flOsmmapLarge.visibility = View.INVISIBLE
        }


        //Location tracker -Task 2
        checkLocationPermission()

        Configuration.getInstance().load(
            activityContext,
            PreferenceManager.getDefaultSharedPreferences(activityContext)
        )

        binding.osmmap.zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER)
        binding.osmmapLarge.zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER)
        binding.osmmap.setMultiTouchControls(true)
        binding.osmmapLarge.setMultiTouchControls(true)

        val prov = GpsMyLocationProvider(activityContext)
        prov.addLocationSource(LocationManager.NETWORK_PROVIDER)

        val prov2 = GpsMyLocationProvider(activityContext)
        prov.addLocationSource(LocationManager.NETWORK_PROVIDER)

        val myLocationOverlay = MyLocationNewOverlay(prov, binding.osmmap)
        myLocationOverlay.enableMyLocation()
        myLocationOverlay.enableFollowLocation()

        val myLocationOverlayLarge = MyLocationNewOverlay(prov2, binding.osmmapLarge)
        myLocationOverlayLarge.enableMyLocation()
        myLocationOverlayLarge.enableFollowLocation()

        val bitmap = BitmapFactory.decodeResource(resources, R.mipmap.ic_navigation_foreground)

        myLocationOverlay.setPersonAnchor(Marker.ANCHOR_RIGHT, Marker.ANCHOR_BOTTOM)
        myLocationOverlay.setDirectionAnchor(Marker.ANCHOR_RIGHT, Marker.ANCHOR_BOTTOM)

        myLocationOverlayLarge.setPersonAnchor(Marker.ANCHOR_RIGHT, Marker.ANCHOR_BOTTOM)
        myLocationOverlayLarge.setDirectionAnchor(Marker.ANCHOR_RIGHT, Marker.ANCHOR_BOTTOM)

        myLocationOverlay.setDirectionIcon(bitmap)
        myLocationOverlay.setPersonIcon(bitmap)

        myLocationOverlayLarge.setDirectionIcon(bitmap)
        myLocationOverlayLarge.setPersonIcon(bitmap)


        binding.osmmap.overlays.add(myLocationOverlay)
        binding.osmmapLarge.overlays.add(myLocationOverlayLarge)
        binding.osmmap.invalidate()
        binding.osmmapLarge.invalidate()


    }

    override fun onResume() {
        super.onResume()

        activityContext.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
        Configuration.getInstance().load(
            activityContext,
            PreferenceManager.getDefaultSharedPreferences(activityContext)
        )
        binding.osmmap.onResume()
    }

    override fun onPause() {
        super.onPause()
        Configuration.getInstance().save(
            activityContext,
            PreferenceManager.getDefaultSharedPreferences(activityContext)
        )
        binding.osmmap.onPause()

    }

    override fun onDestroy() {
        super.onDestroy()
        activityContext.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
        activityContext.unregisterReceiver(broadcastReceiver)
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        locationManager = activityContext.getSystemService(LOCATION_SERVICE) as LocationManager
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5f, this)

    }

    private fun checkLocationPermission() {
        if ((ContextCompat.checkSelfPermission(
                activityContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED)
        ) {
            requestPermission.launch(Manifest.permission.ACCESS_FINE_LOCATION)

//            ActivityCompat.requestPermissions(activityContext, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 2)
        } else {
            getLocation()
        }
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

    override fun onLocationChanged(location: Location) {
        currentLocation = GeoPoint(location.latitude, location.longitude)

        val mapViewController = binding.osmmap.controller
        val mapViewController2 = binding.osmmapLarge.controller

        mapViewController.setZoom(16.0)
        mapViewController.setCenter(currentLocation)

        mapViewController2.setZoom(16.0)
        mapViewController2.setCenter(currentLocation)

        binding.osmmap.invalidate()
        binding.osmmapLarge.invalidate()
    }

}
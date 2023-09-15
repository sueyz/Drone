package com.example.drone.view.ui.splash

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.example.drone.R
import com.example.drone.base.BaseFragment
import com.example.drone.databinding.FragmentSplashPageBinding
import safeNavigate

class SplashPage : BaseFragment<FragmentSplashPageBinding, SplashViewModel>() {

    override val layoutId: Int = R.layout.fragment_splash_page
    //by viewmodel scope
    override val viewModel: SplashViewModel by viewModels()

    override fun init(savedInstanceState: Bundle?) {}

    override fun initView(savedInstanceState: Bundle?) {}

    override fun setupView(view: View) {
        navController = Navigation.findNavController(view)
    }

    override fun onResume() {
        super.onResume()

        Handler(Looper.getMainLooper()).postDelayed({
            navController.safeNavigate(R.id.action_splashPage_to_homePage)
        }, 2500)
    }
}
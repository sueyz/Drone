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

class SplashPage : BaseFragment<FragmentSplashPageBinding, SplashViewModel>() {

    override val layoutId: Int = R.layout.fragment_splash_page
    //by viewmodel scope
    override val viewModel: SplashViewModel by viewModels()

    override fun init(savedInstanceState: Bundle?) {}

    override fun initView(savedInstanceState: Bundle?) {}

    override fun setupView(view: View) {
        navController = Navigation.findNavController(view)

        Handler(Looper.getMainLooper()).postDelayed({
            navController.navigate(R.id.action_splashPage_to_homePage)
        }, 3000)
    }
}
package com.example.drone.view.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.drone.R
import com.example.drone.base.BaseFragment
import com.example.drone.databinding.FragmentHomePageBinding

class HomePage : BaseFragment<FragmentHomePageBinding, HomeViewModel>(){

    override val layoutId: Int = R.layout.fragment_home_page
    //by viewmodel scope
    override val viewModel: HomeViewModel by viewModels()

    override fun init(savedInstanceState: Bundle?) {}

    override fun initView(savedInstanceState: Bundle?) {}

    override fun setupView(view: View) {
        deviceBackButtonGoToHome()
    }
}
package com.example.drone.view.ui.map

import android.os.Bundle
import androidx.fragment.app.viewModels
import com.example.drone.R
import com.example.drone.base.BaseFragment
import com.example.drone.databinding.FragmentMapPageBinding
import com.example.drone.view.ui.home.HomeViewModel

class MapPage : BaseFragment<FragmentMapPageBinding, HomeViewModel>() {

    override val layoutId: Int = R.layout.fragment_map_page
    //by viewmodel scope
    override val viewModel: HomeViewModel by viewModels()

    override fun init(savedInstanceState: Bundle?) {}

    override fun initView(savedInstanceState: Bundle?) {}
}
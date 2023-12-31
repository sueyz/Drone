package com.example.drone.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import com.example.drone.MainActivity

abstract class BaseFragment<TBinding : ViewDataBinding, TViewModel : BaseViewModel>: Fragment() {

    //pakai protected sbb nak kasi subclass dia nmapk bende ni, same func as private + kasi subclass nampak
    protected lateinit var activityContext: MainActivity
    protected lateinit var binding: TBinding
    protected lateinit var navController: NavController
    protected abstract val layoutId: Int
//    protected abstract val viewModelClass: Class<TViewModel>

    protected abstract val viewModel : TViewModel

    //viewmodel does not change so use val better

    //RASA tak nak wat camni sbb mgkin ada fragment yang nak share viewmodels
    // atau scope lain eg: activity scope or navigation scope so tak nak kasi
    // static viewmodel scope sini kat base terus cam meh

//    protected val viewModel by lazy {
//        ViewModelProvider(this)[TViewModel::class.java]
//    }

    private var fragmentView: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init(savedInstanceState)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.activityContext = context as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (fragmentView != null) {
            return fragmentView
        }
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
//        binding.setVariable(BR.viewModel, viewModel)
        initView(savedInstanceState)
        fragmentView = binding.root
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView(view)
        bindViewModelToData()
    }

    /**
     * Method to be overridden and will be triggered in [onCreate]
     */
    abstract fun init(savedInstanceState: Bundle?)

    /**
     * Method to be overridden and will be triggered in [onCreateView]
     */
    abstract fun initView(savedInstanceState: Bundle?)

    /**
     * Method to be overridden and will be triggered in [onViewCreated],
     * this should contain method/logic that initialize any [View] related stuff
     * e.g. listener for [android.widget.EditText]
     */
    protected open fun setupView(view: View) {
        // This should be overriden if got setup view related stuff
    }
    /**
     * Method to be overridden and will be triggered in [onViewCreated], after [setupView],
     * this should contain method/logic that bind any [androidx.lifecycle.LiveData] from the [viewModel]
     */
    protected open fun bindViewModelToData() {
        // this should be overriden
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

}
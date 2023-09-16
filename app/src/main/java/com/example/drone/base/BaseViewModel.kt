package com.example.drone.base

import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable

abstract class BaseViewModel : ViewModel() {

    protected var subscription = CompositeDisposable()

    override fun onCleared() {
        subscription.dispose()
        super.onCleared()
    }

}

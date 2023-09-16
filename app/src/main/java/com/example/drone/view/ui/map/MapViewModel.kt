package com.example.drone.view.ui.map

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.drone.base.BaseViewModel
import com.example.drone.data.model.Response
import com.example.drone.data.network.WeatherAPIService
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.observers.DisposableSingleObserver


class MapViewModel : BaseViewModel() {

    private val weatherApiService = WeatherAPIService()

    val weatherData = MutableLiveData<Response>()
    val weatherError = MutableLiveData<Boolean>()

    fun getDataFromAPI(lat: String, lon: String) {

        subscription.add(
            weatherApiService.getDataService(lat, lon)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<Response>() {

                    override fun onSuccess(t: Response) {
                        weatherData.value = t
                        weatherError.value = false
                        Log.d("Api", "onSuccess: Success")
                    }

                    override fun onError(e: Throwable) {
                        weatherError.value = true
                        Log.e("Api", "onError: $e")
                    }

                })
        )

    }

}
package com.example.drone.data.network

import com.example.drone.data.model.Response
import io.reactivex.rxjava3.core.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import com.example.drone.BuildConfig


class WeatherAPIService {

    val UNITS = "metric"

    private val BASE_URL = "https://api.openweathermap.org/"
    private val API_KEY = BuildConfig.API_KEY

    private val api = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .build()
        .create(WeatherAPI::class.java)

    fun getDataService(lat: String, lon: String): Single<Response> {
        if (API_KEY.isNullOrBlank()) throw Exception("Add api key")
        return api.getData(lat,lon, API_KEY , UNITS)
    }

}
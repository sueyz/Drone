package com.example.drone.data.model

import com.google.gson.annotations.SerializedName

data class Main(
    @SerializedName("temp") var temp: Double? = null,
    @SerializedName("feels_like") var feelsLike: Double? = null,
    @SerializedName("temp_min") var tempMin: Double? = null,
    @SerializedName("temp_max") var tempMax: Double? = null,
    @SerializedName("pressure") var pressure: Double? = null,
    @SerializedName("sea_level") var seaLevel: Double? = null,
    @SerializedName("grnd_level") var grndLevel: Double? = null,
    @SerializedName("humidity") var humidity: Double? = null,
    @SerializedName("temp_kf") var tempKf: Double? = null
)
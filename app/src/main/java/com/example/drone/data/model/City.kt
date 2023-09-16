package com.example.drone.data.model

import com.google.gson.annotations.SerializedName


data class City(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("coord") var coord: Coord? = Coord(),
    @SerializedName("country") var country: String? = null,
    @SerializedName("population") var population: Double? = null,
    @SerializedName("timezone") var timezone: Double? = null,
    @SerializedName("sunrise") var sunrise: Double? = null,
    @SerializedName("sunset") var sunset: Double? = null
)
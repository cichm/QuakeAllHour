package net.usermd.mcichon.quakemodel.API

import net.usermd.mcichon.quakemodel.model.QuakeModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers

interface EarthquakeAPI {

    @get:Headers("Content-Type: application/json")
    @get:GET("all_hour.geojson")
    val data: Call<QuakeModel>

    companion object {

        val BASE_URL = "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/"
    }
}

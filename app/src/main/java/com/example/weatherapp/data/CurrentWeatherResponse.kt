package com.example.weatherapp.data

data class CurrentWeatherResponse(
    val current: Current,
    val location: Location,
    val request: Request
)
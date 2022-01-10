package com.example.weatherapp.data

import com.example.weatherapp.data.api.ApiKeys
import com.example.weatherapp.data.api.WeatherStackAPI
import com.example.weatherapp.data.response.CurrentWeatherResponse
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

val baseUrl : String = WeatherStackAPI().baseUrl
val API_KEY : String = ApiKeys().WEATHER_STACK_API_KEY

interface WeatherStackApiService {

    @GET("current")
    fun getCurrentWeatherFor(
        @Query("query") location: String,
        @Query("lang") languageCode: String = "en"
    ) : Deferred<CurrentWeatherResponse>

    companion object {
        operator fun invoke() : WeatherStackApiService {
            val requestInterceptor = Interceptor { chain ->
                val url = chain.request()
                    .url()
                    .newBuilder()
                    .addQueryParameter("access_key", API_KEY)
                    .build()

                val request = chain.request()
                    .newBuilder()
                    .url(url)
                    .build()

                return@Interceptor chain.proceed(request)
            }

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(requestInterceptor)
                .build()

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(baseUrl)
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(WeatherStackApiService::class.java)
        }
    }
}
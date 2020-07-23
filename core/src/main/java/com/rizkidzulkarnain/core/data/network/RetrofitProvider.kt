package com.rizkidzulkarnain.core.data.network

import com.rizkidzulkarnain.core.util.Global
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


/**
 * Created by Rizki Dzulkarnain on 6/26/2020.
 */
class RetrofitProvider<T>(private val service: Class<T>) {

    fun createRetrofit(): T {
        return Retrofit.Builder()
            .baseUrl(Global.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(createOkHttpClient())
            .build().create(service)
    }

    private fun createOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(object : Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                val url = chain.request().url
                    .newBuilder()
                    .build()
                return chain.proceed(
                    chain.request()
                        .newBuilder()
                        .url(url)
                        .build()
                )
            }
        }).build()
    }
}
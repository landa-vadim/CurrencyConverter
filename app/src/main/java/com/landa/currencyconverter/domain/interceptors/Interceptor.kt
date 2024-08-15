package com.landa.currencyconverter.domain.interceptors

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

class Interceptor {
    private val interceptor = HttpLoggingInterceptor()
    fun getInterceptorClient(): OkHttpClient {
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
        return client
    }
}
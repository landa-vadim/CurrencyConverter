package com.landa.currencyconverter.data.interfaces

import com.landa.currencyconverter.domain.model.Currency
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiCurrency {

    @GET("/currencies")
    suspend fun getCurrenciesMap(): Map<String, String>

    @GET("/{date}?from={fromCurrency}")
    suspend fun getCurrencyForExchange(@Path("date, fromCurrency") date: String, fromCurrency: String): Currency

}
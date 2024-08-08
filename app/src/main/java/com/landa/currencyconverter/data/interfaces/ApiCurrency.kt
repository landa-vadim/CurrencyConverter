package com.landa.currencyconverter.data.interfaces

import com.landa.currencyconverter.domain.model.Currency
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiCurrency {

    @GET("/currencies")
    suspend fun getCurrenciesMap(): Map<String, String>

    @GET("/latest?from={fromCurrency}")
    suspend fun getCurrenciesFrom(@Path("fromCurrency") fromCurrency: String): Currency

}
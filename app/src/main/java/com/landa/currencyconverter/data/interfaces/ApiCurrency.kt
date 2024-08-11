package com.landa.currencyconverter.data.interfaces

import com.landa.currencyconverter.domain.model.Currency
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiCurrency {

    @GET("/currencies")
    suspend fun getCurrenciesMap(): Map<String, String>

    @GET("/{date}")
    suspend fun getCurrencyFromToExchange(
        @Path("date") date: String,
        @Query("amount") amount: String,
        @Query("from") fromCurrency: String,
        @Query("to") toCurrencies: String
    ): Currency

    @GET("/{date}")
    suspend fun getCurrencyFromExchange(
        @Path("date") date: String,
        @Query("amount") amount: String,
        @Query("from") fromCurrency: String,
    ): Currency

}
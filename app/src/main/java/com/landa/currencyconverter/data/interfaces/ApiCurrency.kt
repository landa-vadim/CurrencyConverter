package com.landa.currencyconverter.data.interfaces

import retrofit2.http.GET

interface ApiCurrency {

    @GET("/currencies")
    suspend fun getCurrenciesList(): List<String>




}
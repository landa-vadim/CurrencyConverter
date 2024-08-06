package com.landa.currencyconverter.data

import com.landa.currencyconverter.data.interfaces.ApiCurrency
import com.landa.currencyconverter.domain.interfaces.CurrencyRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiCurrencyRepository : CurrencyRepository {

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://www.frankfurter.app")
        .addConverterFactory(GsonConverterFactory.create()).build()
    private val currencyApi = retrofit.create(ApiCurrency::class.java)

    override suspend fun getCurrenciesList(): List<String> {
        return currencyApi.getCurrenciesList()
    }


}
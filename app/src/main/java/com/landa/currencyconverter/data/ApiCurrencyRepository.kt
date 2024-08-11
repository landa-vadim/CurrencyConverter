package com.landa.currencyconverter.data

import com.landa.currencyconverter.data.interfaces.ApiCurrency
import com.landa.currencyconverter.domain.interceptors.Interceptor
import com.landa.currencyconverter.domain.interfaces.CurrencyRepository
import com.landa.currencyconverter.domain.model.Currency
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiCurrencyRepository : CurrencyRepository {

    private val interceptor = Interceptor()
    private val client = interceptor.getInterceptorClient()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://www.frankfurter.app")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val apiCurrency = retrofit.create(ApiCurrency::class.java)

    override suspend fun getCurrenciesList(): List<String> {
        return apiCurrency.getCurrenciesMap().map {
            "${it.key}=${it.value}"
        }
    }

    override suspend fun getCurrencyFromToExchange(date: String, amount: String, fromCurrency: String, toCurrencies: String): Currency {
        return apiCurrency.getCurrencyFromToExchange(date, amount, fromCurrency, toCurrencies)
    }

    override suspend fun getCurrencyFromExchange(date: String, amount: String, fromCurrency: String): Currency {
        return apiCurrency.getCurrencyFromExchange(date, amount, fromCurrency)
    }

}
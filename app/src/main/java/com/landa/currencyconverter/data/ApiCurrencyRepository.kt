package com.landa.currencyconverter.data

import com.landa.currencyconverter.data.interfaces.ApiCurrency
import com.landa.currencyconverter.di.DaggerDaggerComponent
import com.landa.currencyconverter.domain.interfaces.CurrencyRepository
import com.landa.currencyconverter.domain.model.Currency
import jakarta.inject.Inject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiCurrencyRepository @Inject constructor(): CurrencyRepository {

    private val interceptor = DaggerDaggerComponent.create().getInterceptor()
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
package com.landa.currencyconverter.domain.interfaces

import com.landa.currencyconverter.domain.model.Currency
import retrofit2.http.Path

interface CurrencyRepository {

    suspend fun getCurrenciesList(): List<String>

    suspend fun getCurrencyForExchange(date: String, fromCurrency: String): Currency




}
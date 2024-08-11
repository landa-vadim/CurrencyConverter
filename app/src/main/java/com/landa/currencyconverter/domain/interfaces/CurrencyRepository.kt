package com.landa.currencyconverter.domain.interfaces

import com.landa.currencyconverter.domain.model.Currency

interface CurrencyRepository {

    suspend fun getCurrenciesList(): List<String>

    suspend fun getCurrencyFromToExchange(date: String, amount: String, fromCurrency: String, toCurrencies: String): Currency

    suspend fun getCurrencyFromExchange(date: String, amount: String, fromCurrency: String): Currency


}
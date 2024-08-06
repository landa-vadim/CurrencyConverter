package com.landa.currencyconverter.domain.interfaces

interface CurrencyRepository {

    suspend fun getCurrenciesList(): List<String>





}
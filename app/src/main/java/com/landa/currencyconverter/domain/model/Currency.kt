package com.landa.currencyconverter.domain.model

data class Currency(
    val amount: Int,
    val base: String,
    val date: String,
    val rates: Map<String, Double>
)
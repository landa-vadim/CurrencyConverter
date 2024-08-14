package com.landa.currencyconverter.di

import com.landa.currencyconverter.ConverterApplication
import com.landa.currencyconverter.data.ApiCurrencyRepository
import com.landa.currencyconverter.domain.interceptors.Interceptor
import com.landa.currencyconverter.presentation.viewmodel.MainViewModel
import dagger.Component

@Component
interface DaggerComponent {
    fun getApiCurrencyRepository(): ApiCurrencyRepository
    fun getInterceptor(): Interceptor
    fun getConverterApplication(): ConverterApplication
    fun getMainViewModel(): MainViewModel

}
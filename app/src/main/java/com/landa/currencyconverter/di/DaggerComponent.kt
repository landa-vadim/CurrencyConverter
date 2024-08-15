package com.landa.currencyconverter.di

import com.landa.currencyconverter.ConverterApplication
import com.landa.currencyconverter.data.ApiCurrencyRepository
import com.landa.currencyconverter.domain.interceptors.Interceptor
import com.landa.currencyconverter.presentation.viewmodel.MainViewModel
import dagger.Component

@Component(modules = [AppModule::class, DataModule::class])
interface AppComponent {
    fun getApiCurrencyRepository(): ApiCurrencyRepository
    fun getInterceptor(): Interceptor
    fun getConverterApplication(): ConverterApplication
    fun getMainViewModel(): MainViewModel

}
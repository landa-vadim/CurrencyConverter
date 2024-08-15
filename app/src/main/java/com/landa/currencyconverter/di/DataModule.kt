package com.landa.currencyconverter.di

import com.landa.currencyconverter.data.ApiCurrencyRepository
import com.landa.currencyconverter.domain.interceptors.Interceptor
import dagger.Module
import dagger.Provides

@Module
object DataModule {

    @Provides
    fun provideCurrencyRepository(
        interceptor: Interceptor,
    ): ApiCurrencyRepository {
        return ApiCurrencyRepository(
            interceptor = interceptor,
        )
    }

    @Provides
    fun provideInterceptor(): Interceptor {
        return Interceptor()
    }
}
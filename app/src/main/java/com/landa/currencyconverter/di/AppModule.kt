package com.landa.currencyconverter.di

import com.landa.currencyconverter.ConverterApplication
import com.landa.currencyconverter.data.ApiCurrencyRepository
import com.landa.currencyconverter.presentation.viewmodel.MainViewModel
import dagger.Module
import dagger.Provides

@Module
object AppModule {

    @Provides
    fun provideMainViewModel(
        currencyRepository: ApiCurrencyRepository,
    ): MainViewModel {
        return MainViewModel(
            apiCurrencyRepository = currencyRepository
        )
    }

    @Provides
    fun provideApplication(): ConverterApplication {
        return ConverterApplication()
    }

}
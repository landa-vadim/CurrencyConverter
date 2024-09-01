package com.landa.currencyconverter.di

import android.app.Application
import com.landa.currencyconverter.presentation.ui.MainActivity
import dagger.BindsInstance
import dagger.Component

@Component(modules = [DataModule::class, MainViewModelMultiBinder::class])
interface AppComponent {

    fun inject(activity: MainActivity)

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance application: Application
        ): AppComponent
    }
}
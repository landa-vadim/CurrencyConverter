package com.landa.currencyconverter

import android.app.Application
import com.landa.currencyconverter.di.DaggerAppComponent

class ConverterApplication : Application() {
    val component by lazy {
        DaggerAppComponent.factory().create(this)
    }
}
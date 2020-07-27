package com.rizkidzulkarnain.plnpalu

import androidx.multidex.MultiDexApplication
import com.rizkidzulkarnain.plnpalu.di.appModule
import com.rizkidzulkarnain.plnpalu.di.homeModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

/**
 * Created by Rizki Dzulkarnain on 6/26/2020.
 */
class PLNPaluApplication : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@PLNPaluApplication)
            modules(listOf(appModule, homeModule))
        }
    }
}
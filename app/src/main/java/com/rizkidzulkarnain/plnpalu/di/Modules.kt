package com.rizkidzulkarnain.plnpalu.di

import com.rizkidzulkarnain.core.data.datasource.PLNPaluDataSource
import com.rizkidzulkarnain.core.data.network.RetrofitProvider
import com.rizkidzulkarnain.core.data.remote.PLNPaluService
import com.rizkidzulkarnain.core.data.repository.PLNPaluRepository
import com.rizkidzulkarnain.movieapps.thread.AppSchedulerProvider
import com.rizkidzulkarnain.movieapps.thread.AppSchedulerProviderContract
import org.koin.dsl.module

/**
 * Created by Rizki Dzulkarnain on 7/23/2020.
 */

val appModule = module {
    single<AppSchedulerProviderContract> { AppSchedulerProvider() }
    single { RetrofitProvider(PLNPaluService::class.java).createRetrofit() }
    single<PLNPaluDataSource> { PLNPaluRepository(get()) }
}
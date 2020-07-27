package com.rizkidzulkarnain.plnpalu.di

import com.rizkidzulkarnain.core.data.datasource.AppJardisDataSource
import com.rizkidzulkarnain.core.data.network.RetrofitProvider
import com.rizkidzulkarnain.core.data.remote.AppJardisService
import com.rizkidzulkarnain.core.data.repository.AppJardisRepository
import com.rizkidzulkarnain.core.interactor.HomeInteractor
import com.rizkidzulkarnain.core.interactor.HomeInteractorContract
import com.rizkidzulkarnain.movieapps.thread.AppSchedulerProvider
import com.rizkidzulkarnain.movieapps.thread.AppSchedulerProviderContract
import com.rizkidzulkarnain.plnpalu.activity.navdrawer.home.HomeViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Created by Rizki Dzulkarnain on 7/23/2020.
 */

val appModule = module {
    single<AppSchedulerProviderContract> { AppSchedulerProvider() }
    single { RetrofitProvider(AppJardisService::class.java).createRetrofit() }
    single<AppJardisDataSource> { AppJardisRepository(get()) }
}

val homeModule = module {
    factory<HomeInteractorContract> { HomeInteractor(get()) }
    viewModel { HomeViewModel(get(), get()) }
}
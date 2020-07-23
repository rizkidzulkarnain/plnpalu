package com.rizkidzulkarnain.movieapps.thread

import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

/**
 * Created by Rizki Dzulkarnain on 6/26/2020.
 */
class AppSchedulerProvider : AppSchedulerProviderContract {
    override fun io(): CoroutineContext = Dispatchers.IO

    override fun ui(): CoroutineContext = Dispatchers.Main
}
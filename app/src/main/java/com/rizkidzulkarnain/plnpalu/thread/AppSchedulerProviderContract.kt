package com.rizkidzulkarnain.movieapps.thread

import kotlin.coroutines.CoroutineContext

/**
 * Created by Rizki Dzulkarnain on 6/26/2020.
 */
interface AppSchedulerProviderContract {
    fun io(): CoroutineContext

    fun ui(): CoroutineContext
}
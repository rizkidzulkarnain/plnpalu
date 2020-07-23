package com.rizkidzulkarnain.core.util

/**
 * Created by Rizki Dzulkarnain on 7/23/2020.
 */

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.rizkidzulkarnain.core.domain.BaseApiResponse
import com.rizkidzulkarnain.core.domain.Result
import retrofit2.HttpException
import java.io.IOException
import java.net.NoRouteToHostException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Created by Rizki Dzulkarnain on 6/26/2020.
 */

fun Throwable.getError(): Result.Error {
    return when (this) {
        is SocketTimeoutException -> Result.Error(Throwable(BaseApiResponse.GENERAL_ERROR))
        is NoRouteToHostException -> Result.Error(Throwable(BaseApiResponse.GENERAL_ERROR))
        is IOException -> Result.Error(Throwable(BaseApiResponse.NETWORK_ERROR))
        is UnknownHostException -> Result.Error(Throwable(BaseApiResponse.NETWORK_ERROR))
        is HttpException -> {
            when (this.code()) {
                500, 403, 401 -> Result.Error(Throwable(BaseApiResponse.SERVER_ERROR))
                else -> Result.Error(Throwable(BaseApiResponse.GENERAL_ERROR))
            }
        }
        else -> Result.Error(Throwable(BaseApiResponse.GENERAL_ERROR))
    }
}

fun ImageView.loadImageUrl(imageUrl: String?) {
    if (imageUrl.isNullOrBlank().not()) {
        Glide.with(context)
            .load(imageUrl)
            .centerCrop()
            .into(this)
    } else {
        this.setImageBitmap(null)
    }
}

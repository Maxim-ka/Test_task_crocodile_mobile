package com.reschikov.crocodilemobile.testtask.network

import com.reschikov.crocodilemobile.testtask.ui.viewmodel.Derivable
import okhttp3.*
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class GateWay(private val okHttpClient: OkHttpClient) : Derivable {

    @Throws
    override suspend fun getResponse(url: String): String? {
        return  suspendCoroutine { continuation ->
            okHttpClient.newCall(createRequest(url)).enqueue(object: Callback {
                override fun onFailure(call: Call, e: IOException) {
                    continuation.resumeWithException(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    continuation.resume(response.body()?.string())
                }
            })
        }
    }

    private fun createRequest(url: String) : Request {
        return Request.Builder()
            .url(url)
            .build()
    }
}
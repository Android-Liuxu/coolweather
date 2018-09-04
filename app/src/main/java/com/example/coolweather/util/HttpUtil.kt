package com.example.coolweather.util

import okhttp3.OkHttpClient
import okhttp3.Request

/**
 * Created by Administrator on 2018/7/5/005.
 */

object HttpUtil {
    fun sendOkHttpRequest(address: String, callback: okhttp3.Callback) {
        val client = OkHttpClient()
        val request = Request.Builder().url(address).build()
        client.newCall(request).enqueue(callback)
    }
}

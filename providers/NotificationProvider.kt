package com.carlosvicente.gaugegrafico.providers

import com.carlosvicente.gaugegrafico.api.IFCMApi
import com.carlosvicente.gaugegrafico.api.RetrofitClient
import com.carlosvicente.gaugegrafico.models.FCMBody
import com.carlosvicente.gaugegrafico.models.FCMResponse
import retrofit2.Call

class NotificationProvider {

    private val URL = "https://fcm.googleapis.com"

    fun sendNotification(body: FCMBody): Call<FCMResponse> {
        return RetrofitClient.getClient(URL).create(IFCMApi::class.java).send(body)
    }

}
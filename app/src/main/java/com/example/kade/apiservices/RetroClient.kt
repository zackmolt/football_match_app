package com.example.kade.apiservices

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object RetroClient {
    var InInstance : Retrofit?=null

    val instance:Retrofit
        get() {
        if(InInstance ==null){
            InInstance =Retrofit.Builder()
                .baseUrl("https://www.thesportsdb.com/api/v1/json/1/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        }
        return InInstance!!
    }
}
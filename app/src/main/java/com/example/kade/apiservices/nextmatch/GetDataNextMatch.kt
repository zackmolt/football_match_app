package com.example.kade.apiservices.nextmatch

import com.example.kade.model.ListMatchModel
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface GetDataNextMatch {

    @GET("eventsnextleague.php")
    fun getData(@Query("id") id:String):Observable<ListMatchModel>
}
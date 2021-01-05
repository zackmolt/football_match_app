package com.example.kade.apiservices.prevmatch

import com.example.kade.model.ListMatchModel
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface GetDataPrevMatch {


    @GET("eventspastleague.php")
    fun getData(@Query("id") strId:String): Observable<ListMatchModel>
}
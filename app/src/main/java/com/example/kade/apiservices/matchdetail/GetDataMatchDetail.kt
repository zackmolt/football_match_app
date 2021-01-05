package com.example.kade.apiservices.matchdetail

import com.example.kade.model.DetailMatch
import com.example.kade.model.ListMatchModel
import com.example.kade.model.MatchDetail
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface GetDataMatchDetail {

    @GET("lookupevent.php")
    fun getData(@Query("id")strId:String): Observable<DetailMatch>
}
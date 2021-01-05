package com.example.kade.apiservices.leaguedetail

import com.example.kade.model.LeagueModel
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query


interface GetDataLeagueDetail {
    @GET("lookupleague.php")
    fun getData(@Query("id") url: String):Observable<LeagueModel>
}
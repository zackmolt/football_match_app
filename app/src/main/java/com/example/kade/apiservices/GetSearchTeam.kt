package com.example.kade.apiservices

import com.example.kade.model.AllTeamsResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface GetSearchTeam {
    @GET("searchteams.php")
    fun getData(@Query("t")strQuery:String):Observable<AllTeamsResponse>
}
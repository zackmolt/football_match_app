package com.example.kade.apiservices

import com.example.kade.model.TeamResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface GetDetailTeam {

    @GET("lookupteam.php")
    fun getData(@Query("id") strId:String) : Observable<TeamResponse>
}
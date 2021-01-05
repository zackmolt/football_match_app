package com.example.kade.apiservices

import com.example.kade.model.AllTeamsResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface GetAllTeams {
    @GET("search_all_teams.php")
    fun getData(@Query("l") strId:String) : Observable<AllTeamsResponse>
}
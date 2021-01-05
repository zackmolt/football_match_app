package com.example.kade.apiservices.teamdetail

import com.example.kade.model.Badge
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query


interface GetDataTeamDetail {
    @GET("lookupteam.php")
    fun getData(@Query("id")strId:String):Observable<Badge>
}
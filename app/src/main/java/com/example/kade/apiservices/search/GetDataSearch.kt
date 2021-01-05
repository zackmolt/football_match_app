package com.example.kade.apiservices.search

import com.example.kade.model.SearchModel
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface GetDataSearch {
    @GET("searchevents.php?e")
    fun getData(@Query("e")strQuery: String):Observable<SearchModel>
}
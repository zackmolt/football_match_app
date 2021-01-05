package com.example.kade.apiservices

import com.example.kade.model.Badge
import com.example.kade.model.TableResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface GetClassement {
    @GET("lookuptable.php")
    fun getData(@Query("l")strLeague:String): Observable<TableResponse>
}
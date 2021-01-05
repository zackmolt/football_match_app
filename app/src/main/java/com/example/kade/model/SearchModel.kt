package com.example.kade.model

import com.google.gson.annotations.SerializedName

data class SearchModel(
    @SerializedName("event")
    val event:List<SearchChild>
)

data class SearchChild(
    val idEvent :Int?,
    val strHomeTeam :String?,
    val strAwayTeam :String?,
    val idHomeTeam :Int?,
    val idAwayTeam :Int?,
    val dateEvent :String?,
    val strTime :String?,
    val intHomeScore:Int?,
    val intAwayScore:Int?,
    val strSport:String?
)
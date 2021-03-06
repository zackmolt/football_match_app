package com.example.kade.model

data class ListMatchModel(
    val events:List<MatchDetail>
)
data class MatchDetail(
    val idEvent :Int,
    val strHomeTeam :String,
    val strAwayTeam :String,
    val idHomeTeam :Int,
    val idAwayTeam :Int,
    val dateEvent :String,
    val strTime :String,
    val intHomeScore:Int,
    val intAwayScore:Int,
    val strHomeGoalDetails:String,
    val strAwayGoalDetails:String
)
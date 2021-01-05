package com.example.kade.model

data class AllTeamsResponse(
    val teams:ArrayList<AllTeamsResponseChild>
)

data class AllTeamsResponseChild(
    val idTeam:Int,
    val strTeam:String,
    val strTeamBadge:String,
    val strSport:String
)
package com.example.kade.model

data class TeamResponse(
    val teams:ArrayList<TeamResponseChild>
)

data class TeamResponseChild(
    val strTeam:String?,
    val strDescriptionEN:String?,
    val strTeamBadge:String?,
    val strStadiumThumb:String?,
    val strSport : String
)
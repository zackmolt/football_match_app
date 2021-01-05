package com.example.kade.model

data class Badge(
    val teams:ArrayList<ChildBadge>
)

data class ChildBadge(
    val strTeamBadge:String,
    val idTeam:Int
)
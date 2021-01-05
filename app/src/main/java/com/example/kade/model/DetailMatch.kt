package com.example.kade.model

import com.google.gson.annotations.SerializedName

data class DetailMatch(
    @SerializedName("events")
    val events: List<Childre>
)

data class Childre(
    val idEvent: Int,
    val idLeague: Int,
    val strLeague: String,
    val strHomeTeam: String,
    val strAwayTeam: String,
    val idHomeTeam: Int,
    val idAwayTeam: Int,
    val dateEvent: String,
    val strTime: String,
    val intHomeScore: Int,
    val intAwayScore: Int,
    val strHomeGoalDetails: String,
    val strAwayGoalDetails: String,
    val strHomeFormation: String,
    val strAwayFormation: String,
    val intHomeShots: Int,
    val intAwayShots: Int,
    val strHomeRedCards: String,
    val strHomeYellowCards: String,
    val strHomeLineupGoalkeeper: String,
    val strHomeLineupDefense: String,
    val strHomeLineupMidfield: String,
    val strHomeLineupForward: String,
    val strHomeLineupSubstitutes: String,
    val strAwayRedCards: String,
    val strAwayYellowCards: String,
    val strAwayLineupGoalkeeper: String,
    val strAwayLineupDefense: String,
    val strAwayLineupMidfield: String,
    val strAwayLineupForward: String,
    val strAwayLineupSubstitutes: String
)
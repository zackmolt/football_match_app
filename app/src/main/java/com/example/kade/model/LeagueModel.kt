package com.example.kade.model

import com.google.gson.annotations.SerializedName


data class LeagueModel(
     @SerializedName("leagues")
     val list:List<League>
)
data class League(
     val strLeague:String,
     val strDescriptionEN:String,
     val strBanner:String,
     val strBadge:String
)


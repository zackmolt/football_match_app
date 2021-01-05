package com.example.kade.model

data class TableModel(
    val name :String,
    val teamid:Int,
    val played:Int,
    val goalsfor:Int,
    val goalsagainst:Int,
    val goalsdifference:Int,
    val win:Int,
    val draw:Int,
    val loss:Int,
    val total:Int
)
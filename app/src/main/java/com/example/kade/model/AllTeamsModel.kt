package com.example.kade.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AllTeamsModel(
    val id:Int,
    val idTeam: Int,
    val strName: String,
    val strBadge: String
) : Parcelable

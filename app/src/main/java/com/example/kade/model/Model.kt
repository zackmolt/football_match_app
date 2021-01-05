package com.example.kade.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Model(var title: String, var imgId: Int, var idLeague:Int) :
    Parcelable
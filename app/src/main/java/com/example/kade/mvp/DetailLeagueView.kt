package com.example.kade.mvp

import com.example.kade.model.League


interface DetailLeagueView {
    fun showLoading()
    fun hideLoading()
    fun showData(data: List<League>)
}
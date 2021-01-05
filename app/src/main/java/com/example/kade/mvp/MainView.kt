package com.example.kade.mvp

import com.example.kade.model.MatchDetail

interface MainView {
    fun showLoading()
    fun hideLoading()
    fun showNextMatch(data: List<MatchDetail>)
}
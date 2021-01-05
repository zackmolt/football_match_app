package com.example.kade.mvp

import com.example.kade.model.Childre

interface DetailMatchView {
    fun showLoading()
    fun hideLoading()
    fun showData(data: List<Childre>)
    fun showLeague(it: String)
    fun showHome(it: String)
    fun showAway(it: String)
}
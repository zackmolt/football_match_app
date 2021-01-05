package com.example.kade.mvp.netserv

import com.example.kade.model.AllTeamsResponseChild
import com.example.kade.model.SearchChild

interface SearchQuerView {
    fun showLoading()
    fun hideLoading()
    fun showData(data: List<SearchChild>)
    fun showDataTeam(data: List<AllTeamsResponseChild>)
}
package com.example.kade.mvp

import com.example.kade.model.AllTeamsResponse
import com.example.kade.model.ListMatchModel
import com.example.kade.model.SearchModel
import com.example.kade.mvp.netserv.ApiRepo
import com.example.kade.mvp.netserv.SearchQuerView
import com.example.kade.mvp.netserv.TheSportDBApi
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SearchPresenter(private val view: SearchQuerView,
                    private val apiRepository: ApiRepo,
                    private val gson: Gson, private val context: CoroutineContextProvider = CoroutineContextProvider()) {

    fun getData(q: String?) {
        view.showLoading()
        GlobalScope.launch(context.main) {
            val data = gson.fromJson(
                apiRepository
                    .doRequest(TheSportDBApi.getSearch(q)).await(),
                SearchModel::class.java
            )
            view.showData(data.event)
            view.hideLoading()
        }

    }
    fun getDataTeam(t: String?) {
        view.showLoading()
        GlobalScope.launch(context.main) {
            val data = gson.fromJson(
                apiRepository
                    .doRequest(TheSportDBApi.getSearchTeam(t)).await(),
                AllTeamsResponse::class.java
            )
            view.showDataTeam(data.teams)
            view.hideLoading()
        }

    }
}
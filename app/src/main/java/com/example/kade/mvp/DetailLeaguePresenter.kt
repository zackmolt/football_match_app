package com.example.kade.mvp

import com.example.kade.model.DetailMatch
import com.example.kade.model.LeagueModel
import com.example.kade.mvp.netserv.ApiRepo
import com.example.kade.mvp.netserv.TheSportDBApi
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DetailLeaguePresenter(private val view: DetailLeagueView,
                           private val apiRepository: ApiRepo,
                           private val gson: Gson, private val context: CoroutineContextProvider = CoroutineContextProvider()) {

    fun getDetailMatch(match: String?) {
        view.showLoading()
        GlobalScope.launch(context.main) {
            val data = gson.fromJson(
                apiRepository
                    .doRequest(TheSportDBApi.getDetailLeague(match)).await(),
                LeagueModel::class.java
            )
            view.showData(data.list)
            view.hideLoading()
        }

    }
}
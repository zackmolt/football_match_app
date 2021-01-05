package com.example.kade.mvp

import com.example.kade.model.DetailMatch
import com.example.kade.mvp.netserv.ApiRepo
import com.example.kade.mvp.netserv.TheSportDBApi
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class DetailMatchPresenter(private val view: DetailMatchView,
                           private val apiRepository: ApiRepo,
                           private val gson: Gson, private val context: CoroutineContextProvider = CoroutineContextProvider()) {

    fun getDetailMatch(match: String?) {
        view.showLoading()
        GlobalScope.launch(context.main){
            val data = gson.fromJson(apiRepository
                .doRequest(TheSportDBApi.getDetailMatch(match)).await(),
                DetailMatch::class.java
            )
            view.showData(data.events)
            view.hideLoading()
            setLeaguePict(data.events.get(0).idLeague.toString())
            setHomePict(data.events.get(0).idHomeTeam.toString())
            setAwayPict(data.events.get(0).idAwayTeam.toString())

        }

    }

    fun setLeaguePict(leagueId: String) {
        view.showLeague(leagueId)
    }

    fun setHomePict(leagueId: String) {
        view.showHome(leagueId)
    }

    fun setAwayPict(leagueId: String) {
        view.showAway(leagueId)
    }

}
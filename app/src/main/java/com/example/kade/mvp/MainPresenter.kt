package com.example.kade.mvp

import com.example.kade.model.ListMatchModel
import com.example.kade.mvp.netserv.ApiRepo
import com.example.kade.mvp.netserv.TheSportDBApi
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainPresenter(private val view: MainView,
                    private val apiRepository: ApiRepo,
                    private val gson: Gson,private val context: CoroutineContextProvider = CoroutineContextProvider()) {

    fun getNextMatch(league: String?) {
        view.showLoading()
        GlobalScope.launch(context.main){
            val data = gson.fromJson(apiRepository
                .doRequest(TheSportDBApi.getNextMatch(league)).await(),
                ListMatchModel::class.java
            )
            view.showNextMatch(data.events)
            view.hideLoading()
        }

    }

    fun getPrevMatch(league: String?) {
        view.showLoading()
        GlobalScope.launch(context.main){
            val data = gson.fromJson(apiRepository
                .doRequest(TheSportDBApi.getPrevMatch(league)).await(),
                ListMatchModel::class.java
            )
            view.showNextMatch(data.events)
            view.hideLoading()
        }

    }



}


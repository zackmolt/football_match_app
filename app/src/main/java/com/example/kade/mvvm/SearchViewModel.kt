package com.example.kade.mvvm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kade.apiservices.GetSearchTeam
import com.example.kade.apiservices.RetroClient
import com.example.kade.apiservices.search.GetDataSearch
import com.example.kade.model.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class SearchViewModel : ViewModel(){
    val composSearch : CompositeDisposable?= CompositeDisposable()
    val composSearchTeam : CompositeDisposable?= CompositeDisposable()
    var listData:MutableLiveData<MutableList<MatchDetailModel>> = MutableLiveData()
    fun getData() : LiveData<MutableList<MatchDetailModel>> = listData
    fun setData(query:String){

        var dataApi: GetDataSearch = RetroClient.instance.create(GetDataSearch::class.java)

        composSearch?.addAll(dataApi.getData(query)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe( {
                if (it.event != null && it.event.size != null) {
                    var searchList: MutableList<MatchDetailModel> = mutableListOf()
                    for (i in 0 until it.event.size) {
                        if(it.event[i].strSport.equals("Soccer")){
                            searchList.add(
                                MatchDetailModel(
                                    it.event.get(i).idEvent,
                                    it.event.get(i).strHomeTeam,
                                    it.event.get(i).strAwayTeam,
                                    it.event.get(i).idHomeTeam,
                                    it.event.get(i).idAwayTeam,
                                    it.event.get(i).dateEvent,
                                    it.event.get(i).strTime,
                                    it.event.get(i).intHomeScore,
                                    it.event.get(i).intAwayScore, "", ""
                                )
                            )
                        }
                    }
                    listData.postValue(searchList)
                }
            },{}
            )
        )
    }

    var listDataTeam:MutableLiveData<MutableList<AllTeamsResponseChild>> = MutableLiveData()
    fun getDataTeam():LiveData<MutableList<AllTeamsResponseChild>> = listDataTeam
    fun setDataTeam(query:String){
        val dataApi= RetroClient.instance.create(GetSearchTeam::class.java)
        composSearchTeam?.addAll(dataApi.getData(query)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    if(it!=null){
                        if(it.teams !=null && it.teams.size != 0){
                            listDataTeam.postValue(it.teams)
                        }
                    }
                },{}
            ))
    }

    override fun onCleared() {
        super.onCleared()
        composSearch?.clear()
        composSearchTeam?.clear()
    }
}
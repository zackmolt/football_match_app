package com.example.kade.mvvm

import android.os.AsyncTask
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kade.apiservices.RetroClient
import com.example.kade.apiservices.prevmatch.GetDataPrevMatch
import com.example.kade.apiservices.teamdetail.GetDataTeamDetail
import com.example.kade.model.BadgeModel
import com.example.kade.model.ListMatchModel
import com.example.kade.model.MatchDetailModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class PrevMatchViewModel: ViewModel() {

    private var composite: CompositeDisposable? = null

    internal lateinit var dataApi: GetDataPrevMatch
    private var total: Int = 0

    var listData: MutableLiveData<List<MatchDetailModel>> = MutableLiveData()
    fun getData(): LiveData<List<MatchDetailModel>> = listData
    fun setData(leagueId: String) {

        composite = CompositeDisposable()
        dataApi = RetroClient.instance.create(GetDataPrevMatch::class.java)
        composite?.addAll(dataApi.getData(leagueId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { onRetrievePostListStart() }
            .doOnTerminate { onRetrievePostListFinish() }
            .subscribe(
                { displayData(it) },
                { onRetrievePostListError() }
            )
        )
    }

    private fun displayData(data: ListMatchModel?) {
        if (data != null) {
            var list: MutableList<MatchDetailModel> = mutableListOf()
            if (data.events != null && data.events.size > 0) {
                total = data.events.size
                for (i in 0 until data.events.size) {
                    list.add(
                        MatchDetailModel(
                            data.events.get(i).idEvent,
                            data.events.get(i).strHomeTeam,
                            data.events.get(i).strAwayTeam,
                            data.events.get(i).idHomeTeam,
                            data.events.get(i).idAwayTeam,
                            data.events.get(i).dateEvent,
                            data.events.get(i).strTime,
                            data.events.get(i).intHomeScore,
                            data.events.get(i).intAwayScore
                            , data.events.get(i).strHomeGoalDetails,
                            data.events.get(i).strAwayGoalDetails
                        )
                    )
                }
                listHome.clear()
                listAway.clear()
                doBack(data).execute()
            }
            listData.postValue(list)
        }
    }

    var listUrlHome: MutableLiveData<MutableList<BadgeModel>> = MutableLiveData()
    var listHome: MutableList<BadgeModel> = mutableListOf()
    fun getHomePict(): LiveData<MutableList<BadgeModel>> = listUrlHome
    fun setHomePict(leagueId: String) {
        var homeCompo: CompositeDisposable? = CompositeDisposable()
        var dataApi: GetDataTeamDetail = RetroClient.instance.create(GetDataTeamDetail::class.java)

        homeCompo?.addAll(dataApi.getData(leagueId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { onRetrievePostListStart() }
            .doOnTerminate { onRetrievePostListFinish() }
            .subscribe({
                listHome.add(BadgeModel(it.teams[0].strTeamBadge, it.teams[0].idTeam))
                if (listHome.size == total) {
                    listUrlHome.postValue(listHome)
                }
                homeCompo.clear()
            },
                {
                    onRetrievePostListError()
                })
        )

    }

    var listUrlAway: MutableLiveData<MutableList<BadgeModel>> = MutableLiveData()
    var listAway: MutableList<BadgeModel> = mutableListOf()
    fun getAwayPict(): LiveData<MutableList<BadgeModel>> = listUrlAway
    fun setAwayPict(leagueId: String) {
        var awayCompo: CompositeDisposable? = CompositeDisposable()
        var dataApi: GetDataTeamDetail = RetroClient.instance.create(GetDataTeamDetail::class.java)

        awayCompo?.addAll(dataApi.getData(leagueId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { onRetrievePostListStart() }
            .doOnTerminate { onRetrievePostListFinish() }
            .subscribe({
                listAway.add(BadgeModel(it.teams[0].strTeamBadge, it.teams[0].idTeam))
                if (listAway.size == total) {
                    listUrlAway.postValue(listAway)
                }
                awayCompo.clear()
            }, {
                onRetrievePostListError()
            })
        )
    }

    private fun onRetrievePostListStart() {

    }

    private fun onRetrievePostListFinish() {

    }

    private fun onRetrievePostListError() {

    }

    inner class doBack(val data: ListMatchModel) : AsyncTask<Void, Integer, Void>() {
        override fun doInBackground(vararg p0: Void?): Void? {
            for (i in 0 until data.events.size) {
                setHomePict(data.events.get(i).idHomeTeam.toString())
                setAwayPict(data.events.get(i).idAwayTeam.toString())
            }
            return null
        }

    }

    override fun onCleared() {
        super.onCleared()
        composite?.clear()
    }
}
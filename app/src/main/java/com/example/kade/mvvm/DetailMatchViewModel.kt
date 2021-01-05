package com.example.kade.mvvm


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kade.apiservices.RetroClient
import com.example.kade.apiservices.matchdetail.GetDataMatchDetail
import com.example.kade.model.DetailMatch
import com.example.kade.model.DetailMatchModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class DetailMatchViewModel : ViewModel() {

    var composite: CompositeDisposable? = null
    internal lateinit var dataApi: GetDataMatchDetail

    var llistData: MutableLiveData<DetailMatchModel> = MutableLiveData()
    fun getData(): LiveData<DetailMatchModel> = llistData
    fun setData(eventId: String) {
        composite = CompositeDisposable()
        dataApi = RetroClient.instance.create(GetDataMatchDetail::class.java)

        composite?.addAll(dataApi.getData(eventId)
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

    private fun displayData(data: DetailMatch?) {
        if (data != null) {
            if (data != null) {
                val newData = DetailMatchModel(
                    data.events.get(0).idLeague,
                    data.events.get(0).idEvent,
                    data.events.get(0).strLeague,
                    data.events.get(0).strHomeTeam,
                    data.events.get(0).strAwayTeam,
                    data.events.get(0).idHomeTeam,
                    data.events.get(0).idAwayTeam,
                    data.events.get(0).dateEvent,
                    data.events.get(0).strTime,
                    data.events.get(0).intHomeScore,
                    data.events.get(0).intAwayScore,
                    data.events.get(0).strHomeGoalDetails,
                    data.events.get(0).strAwayGoalDetails,
                    data.events.get(0).strHomeFormation,
                    data.events.get(0).strAwayFormation,
                    data.events.get(0).intHomeShots,
                    data.events.get(0).intAwayShots,
                    data.events.get(0).strHomeRedCards,
                    data.events.get(0).strHomeYellowCards,
                    data.events.get(0).strHomeLineupGoalkeeper,
                    data.events.get(0).strHomeLineupDefense,
                    data.events.get(0).strHomeLineupMidfield,
                    data.events.get(0).strHomeLineupForward,
                    data.events.get(0).strHomeLineupSubstitutes,
                    data.events.get(0).strAwayRedCards,
                    data.events.get(0).strAwayYellowCards,
                    data.events.get(0).strAwayLineupGoalkeeper,
                    data.events.get(0).strAwayLineupDefense,
                    data.events.get(0).strAwayLineupMidfield,
                    data.events.get(0).strAwayLineupForward,
                    data.events.get(0).strAwayLineupSubstitutes
                )
                println(data.events.get(0).idLeague)
                setLeaguePict(data.events.get(0).idLeague.toString())
                setHomePict(data.events.get(0).idHomeTeam.toString())
                setAwayPict(data.events.get(0).idAwayTeam.toString())
                llistData.postValue(newData)
            }
        }
    }

    var urlLeague: MutableLiveData<String> = MutableLiveData()
    fun getUrlLeague(): LiveData<String> = urlLeague
    fun setLeaguePict(leagueId: String) {
        val client = OkHttpClient()

        var url = "https://www.thesportsdb.com/api/v1/json/1/lookupleague.php?id=" + leagueId

        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {

            }

            override fun onResponse(call: Call, response: Response) {
                try {
                    val responseData = response.body()?.string()
                    //PARSING DATA
                    println(responseData)
                    val Jobject = JSONObject(responseData)

                    val overview: JSONArray = Jobject.getJSONArray("leagues")
                    val Jarray = overview.getJSONObject(0)
                    val url = Jarray.getString("strBadge")
                    urlLeague.postValue(url)
                } catch (e: Exception) {

                }
            }

        })
    }

    var urlHome: MutableLiveData<String> = MutableLiveData()
    fun getUrlHome(): LiveData<String> = urlHome
    fun setHomePict(leagueId: String) {
        val client = OkHttpClient()

        var url = "https://www.thesportsdb.com/api/v1/json/1/lookupteam.php?id=" + leagueId

        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {

            }

            override fun onResponse(call: Call, response: Response) {
                try {
                    val responseData = response.body()?.string()
                    //PARSING DATA
                    println(responseData)
                    val Jobject = JSONObject(responseData)

                    val overview: JSONArray = Jobject.getJSONArray("teams")
                    val Jarray = overview.getJSONObject(0)
                    val url = Jarray.getString("strTeamBadge")
                    urlHome.postValue(url)
                } catch (e: Exception) {

                }
            }

        })
    }

    var urlAway: MutableLiveData<String> = MutableLiveData()
    fun getUrlAway(): LiveData<String> = urlAway
    fun setAwayPict(leagueId: String) {
        val client = OkHttpClient()

        var url = "https://www.thesportsdb.com/api/v1/json/1/lookupteam.php?id=" + leagueId

        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {

            }

            override fun onResponse(call: Call, response: Response) {
                try {
                    val responseData = response.body()?.string()
                    //PARSING DATA
                    println(responseData)
                    val Jobject = JSONObject(responseData)

                    val overview: JSONArray = Jobject.getJSONArray("teams")
                    val Jarray = overview.getJSONObject(0)
                    val url = Jarray.getString("strTeamBadge")
                    urlAway.postValue(url)
                } catch (e: Exception) {

                }
            }

        })
    }


    private fun onRetrievePostListStart() {

    }

    private fun onRetrievePostListFinish() {

    }

    private fun onRetrievePostListError() {

    }

    override fun onCleared() {
        super.onCleared()
        composite?.clear()
    }
}
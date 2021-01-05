package com.example.kade.fragments


import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.example.kade.R
import com.example.kade.activities.MainActivity
import com.example.kade.model.Childre
import com.example.kade.model.DetailMatchModel
import com.example.kade.mvp.DetailMatchPresenter
import com.example.kade.mvp.DetailMatchView
import com.example.kade.mvp.netserv.ApiRepo
import com.example.kade.mvvm.DetailMatchViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_detail_match.*
import okhttp3.*
import org.jetbrains.anko.support.v4.runOnUiThread
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class DetailMatchFragment : BottomSheetDialogFragment(), DetailMatchView {

    private lateinit var data: List<Childre>
    private lateinit var cDate: String
    private lateinit var cTime: String
    private lateinit var presenter: DetailMatchPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return inflater.inflate(R.layout.fragment_detail_match, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel = ViewModelProviders.of(this).get(DetailMatchViewModel::class.java)
        viewModel.getData().observe(this, Observer {
            /*if (it != null) {
                data = it
                convertDateTime()
                tvDate.text = cDate
                tvTime.text = cTime

                tvLeague.text = it.strLeague
                tvHomeTeam.text = it.strHomeTeam
                tvHomeScore.text = it.intHomeScore.toString()
                tvHomeGoalsDetail.text = it.strHomeGoalDetails
                tvHomeYellowCards.text = it.strHomeYellowCards
                tvHomeRedCards.text = it.strHomeRedCards
                tvHomeShots.text = it.intHomeShots.toString()

                tvHomeFormation.text = it.strHomeFormation
                tvHomeGoalkeeper.text = it.strHomeLineupGoalkeeper
                tvHomeDefense.text = it.strHomeLineupDefense
                tvHomeMidfield.text = it.strHomeLineupMidfield
                tvHomeForward.text = it.strHomeLineupForward

                tvHomeSubtitutes.text = it.strHomeLineupSubstitutes

                //Away
                tvAwayTeam.text = it.strAwayTeam
                tvAwayScore.text = it.intAwayScore.toString()
                tvAwayGoalsDetail.text = it.strAwayGoalDetails
                tvAwayYellowCards.text = it.strAwayYellowCards
                tvAwayRedCards.text = it.strAwayRedCards
                tvAwayShots.text = it.intAwayShots.toString()

                tvAwayFormation.text = it.strAwayFormation
                tvAwayGoalkeeper.text = it.strAwayLineupGoalkeeper
                tvAwayDefense.text = it.strAwayLineupDefense
                tvAwayMidfield.text = it.strAwayLineupMidfield
                tvAwayForward.text = it.strAwayLineupForward

                tvAwaySubtitutes.text = it.strAwayLineupSubstitutes


            }*/

            viewModel.getUrlLeague().observe(this, Observer {
                Glide.with(this).load(it).into(ivLeagueDetail)
            })

            viewModel.getUrlHome().observe(this, Observer {
                Glide.with(this).load(it).into(ivHome)
            })

            viewModel.getUrlAway().observe(this, Observer {
                Glide.with(this).load(it).into(ivAway)
            })


        })

        var strId = arguments?.getString(MainActivity.EVENT_ID, "441613")
        if (strId == null) {
            strId = "441613"
        }
        //MVVM
        //viewModel.setData(strId)

        //MVP
        //presenter.setData(strId)

        presenter= DetailMatchPresenter(this, ApiRepo(), Gson())
        presenter.getDetailMatch(strId)

    }

    fun convertDateTime() {
        if (data[0].dateEvent != null && !data[0].dateEvent.equals("") && data[0].strTime != null && !data[0].strTime.equals(
                ""
            )
        ) {
            val utcFormat = SimpleDateFormat("yyyy-MM-dd HH:mm")
            utcFormat.timeZone = TimeZone.getTimeZone("UTC")
            val date = utcFormat.parse(data[0].dateEvent + " " + data[0].strTime)

            val pstFormat = SimpleDateFormat("dd-MM-yyyy|HH:mm")
            pstFormat.timeZone = TimeZone.getDefault()
            parseDate(pstFormat.format(date))
        }
    }

    private fun parseDate(convertedDate: String) {
        var year = 0
        var month = 0
        var day = 0

        var rawYear = ""
        var rawMonth = ""
        var rawDay = ""

        var time = ""
        var strDay = ""

        var stripCount = 0
        for (i in 0 until convertedDate.length) {
            if (stripCount == 0) {
                if (convertedDate[i] == '-') {
                    day = rawDay.toInt()
                    stripCount++
                } else
                    rawDay += convertedDate[i].toString()

            } else if (stripCount == 1) {
                if (convertedDate[i] == '-') {
                    month = rawMonth.toInt()
                    stripCount++
                } else
                    rawMonth += convertedDate[i].toString()
            } else if (stripCount == 2) {
                if (convertedDate[i] == '|') {
                    year = rawYear.toInt()
                    stripCount++
                } else
                    rawYear += convertedDate[i].toString()
            } else if (stripCount == 3) {
                time += convertedDate[i].toString()
            }
        }

        var dateString = String.format("%d-%d-%d", year, month, day)
        var date = SimpleDateFormat("yyyy-M-d").parse(dateString)
        strDay = SimpleDateFormat("EEEE", Locale.ENGLISH).format(date)

        cDate = "$strDay, $day-$month-$year"
        cTime = time
    }

    override fun showLoading() {
        //Load
    }

    override fun hideLoading() {
        //Loaded
    }

    override fun showData(it: List<Childre>) {
        if (it != null) {
            data = it
            convertDateTime()
            tvDate.text = cDate
            tvTime.text = cTime

            tvLeague.text = it[0].strLeague
            tvHomeTeam.text = it[0].strHomeTeam
            tvHomeScore.text = it[0].intHomeScore.toString()
            tvHomeGoalsDetail.text = it[0].strHomeGoalDetails
            tvHomeYellowCards.text = it[0].strHomeYellowCards
            tvHomeRedCards.text = it[0].strHomeRedCards
            tvHomeShots.text = it[0].intHomeShots.toString()

            tvHomeFormation.text = it[0].strHomeFormation
            tvHomeGoalkeeper.text = it[0].strHomeLineupGoalkeeper
            tvHomeDefense.text = it[0].strHomeLineupDefense
            tvHomeMidfield.text = it[0].strHomeLineupMidfield
            tvHomeForward.text = it[0].strHomeLineupForward

            tvHomeSubtitutes.text = it[0].strHomeLineupSubstitutes

            //Away
            tvAwayTeam.text = it[0].strAwayTeam
            tvAwayScore.text = it[0].intAwayScore.toString()
            tvAwayGoalsDetail.text = it[0].strAwayGoalDetails
            tvAwayYellowCards.text = it[0].strAwayYellowCards
            tvAwayRedCards.text = it[0].strAwayRedCards
            tvAwayShots.text = it[0].intAwayShots.toString()

            tvAwayFormation.text = it[0].strAwayFormation
            tvAwayGoalkeeper.text = it[0].strAwayLineupGoalkeeper
            tvAwayDefense.text = it[0].strAwayLineupDefense
            tvAwayMidfield.text = it[0].strAwayLineupMidfield
            tvAwayForward.text = it[0].strAwayLineupForward

            tvAwaySubtitutes.text = it[0].strAwayLineupSubstitutes


        }
    }

    override fun showLeague(it: String) {
        val client = OkHttpClient()

        var url = "https://www.thesportsdb.com/api/v1/json/1/lookupleague.php?id=" + it

        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                //idk
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
                    runOnUiThread {
                        Picasso.get().load(url).into(ivLeagueDetail)
                    }
                } catch (e: Exception) {

                }
            }

        })

    }

    override fun showHome(it: String) {
        val client = OkHttpClient()

        var url = "https://www.thesportsdb.com/api/v1/json/1/lookupteam.php?id=" + it

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
                    runOnUiThread {
                        runOnUiThread {
                            Picasso.get().load(url).into(ivHome)
                        }
                    }
                } catch (e: Exception) {

                }
            }

        })

    }

    override fun showAway(it: String) {
        val client = OkHttpClient()

        var url = "https://www.thesportsdb.com/api/v1/json/1/lookupteam.php?id=" + it

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
                    runOnUiThread {
                        Picasso.get().load(url).into(ivAway)
                    }
                } catch (e: Exception) {

                }
            }

        })

    }


}

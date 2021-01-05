package com.example.kade.fragments


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kade.R
import com.example.kade.adapters.AllTeamsAdapter
import com.example.kade.adapters.LoveTeamsAdapter
import com.example.kade.adapters.NextMatchLoveAdapter
import com.example.kade.adapters.PrevMatchLoveAdapter
import com.example.kade.db.database
import com.example.kade.model.*
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_like.*
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.select
import java.lang.Exception

/**
 * A simple [Fragment] subclass.
 */
class LikeFragment : AppCompatActivity(), PrevMatchLoveAdapter.onRemoveItem,
    NextMatchLoveAdapter.onRemoveItem {

    private var list: MutableList<MatchDetailModel> = mutableListOf()
    private var favorites: MutableList<FavoriteMatch> = mutableListOf()
    private var team: MutableList<AllTeamsModel> = mutableListOf()
    private lateinit var rcAdaptFav: PrevMatchLoveAdapter
    private lateinit var rcAdaptAlert: NextMatchLoveAdapter
    private lateinit var rcAdaptTeam: LoveTeamsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_like)

        rcFav.layoutManager = LinearLayoutManager(this)
        listener()
        loadDataFav()

        LocalBroadcastManager.getInstance(this).registerReceiver(bcTeam, IntentFilter("DEL_TEAM_FAV"))
    }

    private fun listener() {
        tabLayoutFav.addOnTabSelectedListener(object :
            TabLayout.BaseOnTabSelectedListener<TabLayout.Tab> {
            override fun onTabReselected(p0: TabLayout.Tab?) {

            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {

            }

            override fun onTabSelected(p0: TabLayout.Tab?) {
                if (p0?.position == 0)
                    loadDataFav()
                else if (p0?.position == 1)
                    loadDataAlert()
                else
                    loadDataTeam()
            }

        })
    }

    private fun loadDataFav() {
        favorites.clear()
        list.clear()
        team.clear()
        applicationContext?.database?.use {
            val result = select(FavoriteMatch.TABLE_FAV)
            val favorite = result.parseList(classParser<FavoriteMatch>())
            favorites.addAll(favorite)
            var uH: MutableList<BadgeModel> = mutableListOf()
            var uA: MutableList<BadgeModel> = mutableListOf()
            for (i in 0 until favorites.size) {
                list.add(
                    MatchDetailModel(
                        favorites[i].eventId,
                        favorites[i].strHome,
                        favorites[i].strAway,
                        favorites[i].idHome,
                        favorites[i].idAway,
                        favorites[i].dateEvent,
                        favorites[i].strTime,
                        favorites[i].intHomeScore,
                        favorites[i].intAwayScore,
                        "",
                        ""
                    )
                )
                uH.add(BadgeModel(favorites[i].strBadgeHome!!, favorites[i].idHome!!))
                uA.add(BadgeModel(favorites[i].strBadgeAway!!, favorites[i].idAway!!))
            }
            rcAdaptFav = PrevMatchLoveAdapter(this@LikeFragment, list, uH, uA)
            rcFav.adapter = rcAdaptFav
        }
    }

    private fun loadDataAlert() {
        favorites.clear()
        list.clear()
        team.clear()
        applicationContext?.database?.use {
            val result = select(FavoriteMatch.TABLE_ALERT)
            val favorite = result.parseList(classParser<FavoriteMatch>())
            favorites.addAll(favorite)
            var uH: MutableList<BadgeModel> = mutableListOf()
            var uA: MutableList<BadgeModel> = mutableListOf()
            for (i in 0 until favorites.size) {
                list.add(
                    MatchDetailModel(
                        favorites[i].eventId,
                        favorites[i].strHome,
                        favorites[i].strAway,
                        favorites[i].idHome,
                        favorites[i].idAway,
                        favorites[i].dateEvent,
                        favorites[i].strTime,
                        favorites[i].intHomeScore,
                        favorites[i].intAwayScore,
                        "",
                        ""
                    )
                )
                uH.add(BadgeModel(favorites[i].strBadgeHome!!, favorites[i].idHome!!))
                uA.add(BadgeModel(favorites[i].strBadgeAway!!, favorites[i].idAway!!))
            }
            rcAdaptAlert = NextMatchLoveAdapter(this@LikeFragment, list, uH, uA)
            rcFav.adapter = rcAdaptAlert
        }
    }

    private fun loadDataTeam() {
        favorites.clear()
        list.clear()
        team.clear()
        val ctx=application as Context

        ctx?.database?.use {
            val result = select(FavoriteMatch.TABLE_TEAM)
            val favorite = result.parseList(classParser<AllTeamsModel>())
            team.addAll(favorite)

            rcAdaptTeam = LoveTeamsAdapter(this@LikeFragment, team)
            rcFav.adapter = rcAdaptTeam
        }
    }

    override fun removeItem(model: MatchDetailModel) {
        list.remove(model)
        rcFav.adapter = rcAdaptFav
    }

    override fun removeItemNext(model: MatchDetailModel) {
        list.remove(model)
        rcFav.adapter = rcAdaptAlert
    }

    val bcTeam = object : BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            team.remove(intent!!.getParcelableExtra("EXTRA"))

            rcAdaptTeam = LoveTeamsAdapter(this@LikeFragment, team)
            rcFav.adapter = rcAdaptTeam
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(bcTeam)
    }
}

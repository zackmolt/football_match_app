package com.example.kade.adapters

import android.app.Activity
import android.database.sqlite.SQLiteConstraintException
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kade.R
import com.example.kade.activities.MainActivity
import com.example.kade.db.database
import com.example.kade.fragments.DetailMatchFragment
import com.example.kade.fragments.LikeFragment
import com.example.kade.model.BadgeModel
import com.example.kade.model.FavoriteMatch
import com.example.kade.model.MatchDetailModel
import kotlinx.android.synthetic.main.layout_prev_match.view.*
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select
import java.text.SimpleDateFormat
import java.util.*


class PrevMatchLoveAdapter(
    val context: Activity,
    val data: MutableList<MatchDetailModel>,
    val urlHome: MutableList<BadgeModel>,
    val urlAway: MutableList<BadgeModel>
) :
    RecyclerView.Adapter<PrevMatchLoveAdapter.CustomVH>() {
    var dateList: MutableList<String> = mutableListOf()
    var timeList: MutableList<String> = mutableListOf()
    var favorites: MutableList<FavoriteMatch> = mutableListOf()
    var callback: onRemoveItem = context as onRemoveItem

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PrevMatchLoveAdapter.CustomVH {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_prev_match, parent, false)
        return CustomVH(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(h: PrevMatchLoveAdapter.CustomVH, i: Int) {

        h.tvHome.text = data.get(i).strHomeTeam
        h.tvAway.text = data.get(i).strAwayTeam

        if (data.get(i).dateEvent != null && !data.get(i).dateEvent.equals("") && data.get(i).strTime != null && !data.get(
                i
            ).strTime.equals("")
        ) {
            val utcFormat = SimpleDateFormat("yyyy-MM-dd HH:mm")
            utcFormat.timeZone = TimeZone.getTimeZone("UTC")
            val date = utcFormat.parse(data.get(i).dateEvent + " " + data.get(i).strTime)

            val pstFormat = SimpleDateFormat("dd-MM-yyyy|HH:mm")
            pstFormat.timeZone = TimeZone.getDefault()
            parseDate(pstFormat.format(date))
            h.tvDate.text = dateList.get(i)
            h.tvTime.text = timeList.get(i)
        } else {
            h.tvDate.text = ""
            h.tvTime.text = ""
        }

        if (urlHome.size > i) {
            for (u in 0 until urlHome.size) {
                if (data.get(i).idHomeTeam == urlHome.get(u).idTeam) {
                    Glide.with(context).load(urlHome.get(u).url).into(h.ivHome)
                }
            }
        }
        if (urlAway.size > i) {
            for (u in 0 until urlAway.size) {
                if (data.get(i).idAwayTeam == urlAway.get(u).idTeam) {
                    Glide.with(context).load(urlAway.get(u).url).into(h.ivAway)
                }
            }
        }

        h.tvHomeScore.text = data.get(i).intHomeScore.toString()
        h.tvAwayScore.text = data.get(i).intAwayScore.toString()

        h.btnMore.setOnClickListener {
            val detailDialog = DetailMatchFragment()
            val bundle = Bundle().apply {
                putString(MainActivity.EVENT_ID, data.get(i).idEvent.toString())
            }

            detailDialog.arguments = bundle

            val act: LikeFragment = context as LikeFragment
            detailDialog.show(
                act.supportFragmentManager,
                DetailMatchFragment::class.java.simpleName
            )
        }

        h.btnAddFav.setOnClickListener {
            listenerFav(h, i)
        }
    }

    private fun listenerFav(h: CustomVH, i: Int) {
        var state = false
        favorites.clear()
        if (i == 0) {
            context.database.use {
                val result = select(FavoriteMatch.TABLE_FAV)
                val favorite = result.parseList(classParser<FavoriteMatch>())
                favorites.addAll(favorite)
            }
        }

        for (u in 0 until favorites.size) {
            if (data[i].idEvent == favorites[u].eventId) {
                state = true
            }
        }

        val popup = PopupMenu(context, h.btnAddFav)
        if (!state) {
            popup.inflate(R.menu.fav_menu)
        } else {
            popup.inflate(R.menu.unfav_menu)
        }
        popup.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.mFav -> {
                    if (!state) {
                        addToDB(i)
                    } else
                        removeFromDB(i)
                }
            }
            false
        }
        popup.show()
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

        dateList.add("$strDay, $day-$month-$year")
        timeList.add(time)
    }

    private fun addToDB(i: Int) {
        var indxHome = -1
        var indxAway = -1
        for (u in 0 until data.size) {
            if (data[i].idHomeTeam == urlHome[u].idTeam) {
                indxHome = u
            }
            if (data[i].idAwayTeam == urlAway[u].idTeam) {
                indxAway = u
            }
        }
        try {
            context.database.use {
                insert(
                    FavoriteMatch.TABLE_FAV,
                    FavoriteMatch.EVENT_ID to data[i].idEvent,
                    FavoriteMatch.STR_HOME to data[i].strHomeTeam,
                    FavoriteMatch.STR_AWAY to data[i].strAwayTeam,
                    FavoriteMatch.INT_HOME_SCORE to 0,
                    FavoriteMatch.INT_AWAY_SCORE to 0,
                    FavoriteMatch.STR_BADGE_HOME to urlHome[indxHome].url,
                    FavoriteMatch.STR_BADGE_AWAY to urlAway[indxAway].url,
                    FavoriteMatch.ID_HOME to data[i].idHomeTeam,
                    FavoriteMatch.ID_AWAY to data[i].idAwayTeam,
                    FavoriteMatch.DATE_EVENT to data[i].dateEvent,
                    FavoriteMatch.STR_TIME to data[i].strTime
                )
            }

        } catch (e: SQLiteConstraintException) {

        }
    }

    private fun removeFromDB(i: Int) {
        try {
            context.database.use {
                delete(
                    FavoriteMatch.TABLE_FAV,
                    ("(EVENT_ID = {id})"),
                    "id" to data[i].idEvent.toString()
                )
            }
            callback.removeItem(data[i])
        } catch (e: Exception) {

        }
    }

    class CustomVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvHome: TextView
        val tvAway: TextView
        val tvDate: TextView
        val tvTime: TextView
        val tvHomeScore: TextView
        val tvAwayScore: TextView
        val btnMore: Button
        val ivHome: ImageView
        val ivAway: ImageView
        val btnAddFav: ImageView

        init {
            tvHome = itemView.tvHomeTeam
            tvAway = itemView.tvAwayTeam
            tvDate = itemView.tvDate
            tvTime = itemView.tvTime
            tvHomeScore = itemView.tvHomeScore
            tvAwayScore = itemView.tvAwayScore
            btnMore = itemView.btnMore
            ivHome = itemView.ivHome
            ivAway = itemView.ivAway
            btnAddFav = itemView.btnAddFav
        }
    }

    interface onRemoveItem {
        fun removeItem(model: MatchDetailModel)
    }
}

package com.example.kade.adapters

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.database.sqlite.SQLiteConstraintException
import android.os.Build
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
import com.example.kade.broadcastreceiver.AlertReceiver
import com.example.kade.db.database
import com.example.kade.fragments.DetailMatchFragment
import com.example.kade.model.BadgeModel
import com.example.kade.model.FavoriteMatch
import com.example.kade.model.MatchDetail
import com.example.kade.model.MatchDetailModel
import kotlinx.android.synthetic.main.layout_next_match.view.*
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select
import java.text.SimpleDateFormat
import java.util.*


class NextMatchAdapter(
    val context: Activity,
    val data: List<MatchDetail>,
    val urlHome: MutableList<BadgeModel>,
    val urlAway: MutableList<BadgeModel>
) :
    RecyclerView.Adapter<NextMatchAdapter.CustomVH>() {

    var dateList: MutableList<String> = mutableListOf()
    var timeList: MutableList<String> = mutableListOf()
    var favorites: MutableList<FavoriteMatch> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NextMatchAdapter.CustomVH {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_next_match, parent, false)
        return CustomVH(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(h: NextMatchAdapter.CustomVH, i: Int) {
        h.tvHome.text = data.get(i).strHomeTeam
        h.tvAway.text = data.get(i).strAwayTeam

        h.btnMore.setOnClickListener {
            val detailDialog = DetailMatchFragment()
            val bundle = Bundle().apply {
                putString(MainActivity.EVENT_ID, data.get(i).idEvent.toString())
            }

            detailDialog.arguments = bundle

            val act: MainActivity = context as MainActivity
            detailDialog.show(
                act.supportFragmentManager,
                DetailMatchFragment::class.java.simpleName
            )
        }

        val utcFormat = SimpleDateFormat("yyyy-MM-dd HH:mm")
        utcFormat.timeZone = TimeZone.getTimeZone("UTC")
        val date = utcFormat.parse(data.get(i).dateEvent + " " + data.get(i).strTime)

        val pstFormat = SimpleDateFormat("dd-MM-yyyy|HH:mm")
        pstFormat.timeZone = TimeZone.getDefault()
        parseDate(pstFormat.format(date))

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

        h.tvDate.text = dateList.get(i)
        h.tvTime.text = timeList.get(i)

        h.btnAddFav.setOnClickListener {
            listenerFav(h, i)
        }
    }

    private fun listenerFav(h: CustomVH, i: Int) {
        var state = false
        favorites.clear()
        if (i == 0) {
            context.database.use {
                val result = select(FavoriteMatch.TABLE_ALERT)
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

    private fun getDay(pos: Int): Int {
        var mday = ""
        var day = 0

        var stripCount = 0
        for (i in 0 until dateList[pos].length) {
            if (dateList[pos][i] == ',') {
                stripCount += 1
            } else if (dateList[pos][i] == '-') {
                stripCount++
            } else {
                if (stripCount == 1) {
                    mday += dateList[pos][i].toString()
                }
            }
        }
        day = mday.trim().toInt()

        return day
    }

    private fun getMonth(pos: Int): Int {
        var mmonth = ""
        var month = 0

        var stripCount = 0
        for (i in 0 until dateList[pos].length) {
            if (dateList[pos][i] == ',') {
                stripCount += 1
            } else if (dateList[pos][i] == '-') {
                stripCount++
            } else {
                if (stripCount == 2) {
                    mmonth += dateList[pos][i].toString()
                }
            }
        }
        month = ((mmonth.trim().toInt()).toInt() - 1)

        return month
    }

    private fun getYear(pos: Int): Int {
        var myear = ""
        var year = 0

        var stripCount = 0
        for (i in 0 until dateList[pos].length) {
            if (dateList[pos][i] == ',') {
                stripCount += 1
            } else if (dateList[pos][i] == '-') {
                stripCount++
            } else {
                if (stripCount == 3) {
                    myear += dateList[pos][i].toString()
                }
            }
        }
        year = myear.trim().toInt()

        return year
    }

    private fun addToDB(i: Int) {
        var indxHome = -1
        var indxAway = -1
        /*for (u in 0 until data.size) {
            if (data[i].idHomeTeam == urlHome[u].idTeam) {
                indxHome = u
            }
            if (data[i].idAwayTeam == urlAway[u].idTeam) {
                indxAway = u
            }
        }*/
        try {
            context.database.use {
                insert(
                    FavoriteMatch.TABLE_ALERT,
                    FavoriteMatch.EVENT_ID to data[i].idEvent,
                    FavoriteMatch.STR_HOME to data[i].strHomeTeam,
                    FavoriteMatch.STR_AWAY to data[i].strAwayTeam,
                    FavoriteMatch.INT_HOME_SCORE to 0,
                    FavoriteMatch.INT_AWAY_SCORE to 0,
                    FavoriteMatch.STR_BADGE_HOME to "",
                    FavoriteMatch.STR_BADGE_AWAY to "",
                    FavoriteMatch.ID_HOME to data[i].idHomeTeam,
                    FavoriteMatch.ID_AWAY to data[i].idAwayTeam,
                    FavoriteMatch.DATE_EVENT to data[i].dateEvent,
                    FavoriteMatch.STR_TIME to data[i].strTime
                )
            }
            alert(i)

        } catch (e: SQLiteConstraintException) {

        }
    }

    private fun removeFromDB(i: Int) {
        try {
            context.database.use {
                delete(
                    FavoriteMatch.TABLE_ALERT,
                    ("(EVENT_ID = {id})"),
                    "id" to data[i].idEvent.toString()
                )
            }

            val alarmManager =
                context.getSystemService(ALARM_SERVICE) as AlarmManager?
            val intent = Intent(context, AlertReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
            alarmManager!!.cancel(pendingIntent)

            val alarmManager2 =
                context.getSystemService(ALARM_SERVICE) as AlarmManager?
            val intent2 = Intent(context, AlertReceiver::class.java)
            val pendingIntent2 = PendingIntent.getBroadcast(
                context,
                1,
                intent2,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
            alarmManager2!!.cancel(pendingIntent2)
        } catch (e: Exception) {

        }
    }

    private fun alert(pos: Int) {
        //START NOTIF
        var mhour = ""
        var mminute = ""
        var hour = 0
        var minute = 0

        var state = false
        for (i in 0 until timeList[pos].length) {
            if (data[pos].strTime!![i] == ':') {
                state = true

            } else if (state) {
                mminute += timeList[pos][i].toString()
            } else if (!state) {
                mhour += timeList[pos][i].toString()
            }
        }
        hour = mhour.toInt()
        minute = mminute.toInt()

        val i = Intent(context, AlertReceiver::class.java)
        i.putExtra(FavoriteMatch.EVENT_ID, data[pos].idEvent)
        val pi =
            PendingIntent.getBroadcast(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT)
        val alarmManager = context.getSystemService(ALARM_SERVICE) as AlarmManager

        val triggerhCal = Calendar.getInstance()
        val currentCal = Calendar.getInstance()

        triggerhCal[Calendar.DAY_OF_MONTH] = getYear(pos)
        triggerhCal[Calendar.DAY_OF_MONTH] = getMonth(pos)
        triggerhCal[Calendar.DAY_OF_MONTH] = getDay(pos)
        triggerhCal[Calendar.HOUR_OF_DAY] = hour
        triggerhCal[Calendar.MINUTE] = minute
        triggerhCal[Calendar.SECOND] = 0

        var intendedTime = triggerhCal.timeInMillis
        val currentTime = currentCal.timeInMillis

        if (intendedTime >= currentTime) {
            alarmManager.setRepeating(AlarmManager.RTC, intendedTime, AlarmManager.INTERVAL_DAY, pi)
        } else {
            triggerhCal.add(Calendar.DAY_OF_MONTH, 1)
            intendedTime = triggerhCal.timeInMillis
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, intendedTime, pi)
            else
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, intendedTime, pi)
        }
    }

    class CustomVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvHome: TextView
        val tvAway: TextView
        val tvDate: TextView
        val tvTime: TextView
        val btnMore: Button
        val ivHome: ImageView
        val ivAway: ImageView
        val btnAddFav: ImageView

        init {
            tvHome = itemView.tvHomeTeam
            tvAway = itemView.tvAwayTeam
            tvDate = itemView.tvDate
            tvTime = itemView.tvTime
            btnMore = itemView.btnMore
            ivHome = itemView.ivHome
            ivAway = itemView.ivAway
            btnAddFav = itemView.btnAddFav
        }

    }
}
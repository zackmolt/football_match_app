package com.example.kade.fragments


import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteConstraintException
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.localbroadcastmanager.content.LocalBroadcastManager

import com.example.kade.R
import com.example.kade.broadcastreceiver.AlertReceiver
import com.example.kade.db.database
import com.example.kade.model.AllTeamsModel
import com.example.kade.model.AllTeamsResponseChild
import com.example.kade.model.FavoriteMatch
import com.example.kade.model.TeamResponseChild
import com.example.kade.mvvm.DetailTeamViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_detail_team.*
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select

/**
 * A simple [Fragment] subclass.
 */
class DetailTeamFragment : BottomSheetDialogFragment() {

    companion object{
        const val TEAM_ID="TEAMID"
    }
    private var state=false
    private
    lateinit var id : String
    private var dbId : Int = 0
    private lateinit var url : String
    var favorites: MutableList<AllTeamsModel> = mutableListOf()
    private lateinit var contextA:Activity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return inflater.inflate(R.layout.fragment_detail_team, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        id=arguments?.getString(TEAM_ID,"133604")!!
        dbId=arguments?.getInt("ID",0)!!
        if(id==null)
            id="133604"

        loadData(id!!)
        listener()
    }

    fun loadData(id:String){

        val viewModel= ViewModelProviders.of(this).get(DetailTeamViewModel::class.java)
        viewModel.getData().observe(this, Observer {
            if(it[0].strSport.equals("Soccer")){
                tvName.text=it[0].strTeam
                tvDesc.text=it[0].strDescriptionEN
                url=it[0].strTeamBadge!!
                Picasso.get().load(it[0].strTeamBadge).into(ivTeamBadge)
                Picasso.get().load(it[0].strStadiumThumb).into(app_bar_image)

                loadFav(it)
            }
        })
        viewModel.setData(id)
    }

    fun listener(){
        btnFavTeam.setOnClickListener{
            if(!state)
                addToDb()
            else
                removeFromDB()
        }
    }

    fun addToDb(){
        if(tvName.text.isNotEmpty()){
            try {
                context?.database?.use {
                    insert(FavoriteMatch.TABLE_TEAM,
                        FavoriteMatch.STR_TEAM_ID to id,
                        FavoriteMatch.STR_TEAM_NAME to tvName.text,
                        FavoriteMatch.STR_TEAM_URL to url)
                }
            } catch (e: SQLiteConstraintException){

            }
            state=true
            btnFavTeam.setImageDrawable(resources.getDrawable(R.drawable.ic_favorite_black_24dp,contextA.theme))
        }
    }

    fun loadFav(it:MutableList<TeamResponseChild>){
        favorites.clear()
        context?.database?.use {
            val result = select(FavoriteMatch.TABLE_TEAM)
            val favorite = result.parseList(classParser<AllTeamsModel>())
            favorites.addAll(favorite)
        }

        for (u in 0 until favorites.size) {
            if (it[u].strTeam == tvName.text) {
                state = true
            }
        }

        if(state){
            btnFavTeam.setImageDrawable(resources.getDrawable(R.drawable.ic_favorite_black_24dp,contextA.theme))
        }
    }

    private fun removeFromDB() {
        state=false
        try {
            context?.database?.use {
                delete(
                    FavoriteMatch.TABLE_TEAM,
                    ("(TEAM_ID = {id})"),
                    "id" to id
                )
            }

        } catch (e: Exception) {

        }
        btnFavTeam.setImageDrawable(resources.getDrawable(R.drawable.ic_favorite_border_black_24dp,contextA.theme))

        val inte=Intent("DEL_TEAM_FAV")
        inte.putExtra("EXTRA",AllTeamsModel(dbId,id.toInt(),tvName.text.toString(),url))
        LocalBroadcastManager.getInstance(contextA).sendBroadcast(inte)
    }

    override fun onAttach(activity: Activity?) {
        super.onAttach(activity)
        contextA=activity!!
    }
}

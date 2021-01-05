package com.example.kade.adapters

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kade.R
import com.example.kade.activities.AllTeamsActivity
import com.example.kade.activities.MainActivity
import com.example.kade.fragments.DetailTeamFragment
import com.example.kade.model.AllTeamsResponseChild
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.teams_layout.view.*

class SearchTeamsAdapter (val context:MainActivity, val data:MutableList<AllTeamsResponseChild>): RecyclerView.Adapter<SearchTeamsAdapter.CustomVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchTeamsAdapter.CustomVH {
        val view = LayoutInflater.from(context).inflate(R.layout.teams_layout, parent, false)
        return CustomVH(view)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(h: SearchTeamsAdapter.CustomVH, i: Int) {
        if(data[i].strSport.equals("Soccer")){
            h.tvTeam.text= data[i].strTeam
            Picasso.get().load(data[i].strTeamBadge).into(h.ivTeam)
            h.tvTeam.setOnClickListener {
                val bs= DetailTeamFragment()
                bs.arguments= Bundle().apply {
                    putString(DetailTeamFragment.TEAM_ID,data[i].idTeam.toString())
                }
                bs.show(context.supportFragmentManager,DetailTeamFragment.javaClass.simpleName)
            }
            h.ivTeam.setOnClickListener {
                val bs= DetailTeamFragment()
                bs.arguments= Bundle().apply {
                    putString(DetailTeamFragment.TEAM_ID,data[i].idTeam.toString())
                }
                bs.show(context.supportFragmentManager,DetailTeamFragment.javaClass.simpleName) }
        }
    }

    class CustomVH(itemView: View):RecyclerView.ViewHolder(itemView) {
        val tvTeam = itemView.tvTeam
        val ivTeam = itemView.ivTeam
    }
}
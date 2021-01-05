package com.example.kade.adapters

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.kade.R
import com.example.kade.activities.AllTeamsActivity
import com.example.kade.fragments.DetailTeamFragment
import com.example.kade.fragments.LikeFragment
import com.example.kade.model.AllTeamsModel
import com.example.kade.model.AllTeamsResponseChild
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.teams_layout.view.*

class LoveTeamsAdapter (val contextSent:Activity, val data:MutableList<AllTeamsModel>): RecyclerView.Adapter<LoveTeamsAdapter.CustomVH>() {

    val context = contextSent as LikeFragment
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LoveTeamsAdapter.CustomVH {
        val view = LayoutInflater.from(context).inflate(R.layout.teams_layout, parent, false)
        return CustomVH(view)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(h: LoveTeamsAdapter.CustomVH, i: Int) {
        h.tvTeam.text= data[i].strName
        Picasso.get().load(data[i].strBadge).into(h.ivTeam)
        h.tvTeam.setOnClickListener {
            val bs= DetailTeamFragment()
            bs.arguments= Bundle().apply {
                putString(DetailTeamFragment.TEAM_ID,data[i].idTeam.toString())
                putInt("ID",data[i].id)
            }
            bs.show(context.supportFragmentManager,DetailTeamFragment.javaClass.simpleName)
        }
        h.ivTeam.setOnClickListener {
            val bs= DetailTeamFragment()
            bs.arguments= Bundle().apply {
                putString(DetailTeamFragment.TEAM_ID,data[i].idTeam.toString())
                putInt("ID",data[i].id)
            }
            bs.show(context.supportFragmentManager,DetailTeamFragment.javaClass.simpleName) }
    }

    class CustomVH(itemView: View):RecyclerView.ViewHolder(itemView) {
        val tvTeam = itemView.tvTeam
        val ivTeam = itemView.ivTeam
    }
}
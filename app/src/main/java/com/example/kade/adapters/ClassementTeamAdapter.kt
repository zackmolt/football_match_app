package com.example.kade.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kade.R
import com.example.kade.model.TableModel
import kotlinx.android.synthetic.main.classement_rc_layout.view.*

class ClassementTeamAdapter (val context:Activity, var data:MutableList<TableModel>) :
    RecyclerView.Adapter<ClassementTeamAdapter.CustomVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomVH {
        val view=LayoutInflater.from(context).inflate(R.layout.classement_rc_layout,parent,false)
        return CustomVH(view)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(h: CustomVH, position: Int) {
        h.tvName.text=data[position].name
        h.tvRank.text=(position+1).toString()
    }

    class CustomVH(itemView : View): RecyclerView.ViewHolder(itemView) {
        val tvName : TextView = itemView.tvTeamName
        val tvRank =itemView.tvRank
    }
}
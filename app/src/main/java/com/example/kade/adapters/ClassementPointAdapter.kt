package com.example.kade.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kade.R
import com.example.kade.model.TableModel
import kotlinx.android.synthetic.main.classement_point_rc_layout.view.*
import kotlinx.android.synthetic.main.classement_rc_layout.view.*

class ClassementPointAdapter (val context: Activity, var data:MutableList<TableModel>) :
    RecyclerView.Adapter<ClassementPointAdapter.CustomVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomVH {
        val view= LayoutInflater.from(context).inflate(R.layout.classement_point_rc_layout,parent,false)
        return CustomVH(view)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(h: CustomVH, position: Int) {
        h.tvPoint.text=data[position].total.toString()
        h.tvGF.text=data[position].goalsfor.toString()
        h.tvGA.text=data[position].goalsagainst.toString()
        h.tvGD.text=data[position].goalsdifference.toString()
        h.tvWin.text=data[position].win.toString()
        h.tvLoss.text=data[position].loss.toString()
        h.tvDraw.text=data[position].draw.toString()
        h.tvPlayed.text=data[position].played.toString()
    }

    class CustomVH(itemView : View): RecyclerView.ViewHolder(itemView) {
        val tvPoint : TextView = itemView.tvPoints
        val tvPlayed : TextView = itemView.tvPlayed
        val tvGF : TextView = itemView.tvGF
        val tvGA : TextView = itemView.tvGA
        val tvGD : TextView = itemView.tvGD
        val tvWin : TextView = itemView.tvWin
        val tvDraw : TextView = itemView.tvDraw
        val tvLoss : TextView = itemView.tvLoss
    }

}
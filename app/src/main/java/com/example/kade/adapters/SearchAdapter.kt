package com.example.kade.adapters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kade.R
import com.example.kade.activities.MainActivity
import com.example.kade.fragments.DetailMatchFragment
import com.example.kade.model.MatchDetailModel
import com.example.kade.model.SearchChild
import kotlinx.android.synthetic.main.layout_prev_match.view.*


class SearchAdapter(val context: MainActivity, val data: List<SearchChild>) :
    RecyclerView.Adapter<SearchAdapter.CustomVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchAdapter.CustomVH {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_prev_match, parent, false)
        return CustomVH(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(h: SearchAdapter.CustomVH, i: Int) {
        h.tvHome.text = data.get(i).strHomeTeam
        h.tvAway.text = data.get(i).strAwayTeam

        h.tvHomeScore.text = data.get(i).intHomeScore.toString()
        h.tvAwayScore.text = data.get(i).intAwayScore.toString()

        h.btnMore.setOnClickListener {
            val detailDialog = DetailMatchFragment()
            val bundle = Bundle().apply {
                putString(MainActivity.EVENT_ID, data.get(i).idEvent.toString())
            }

            detailDialog.arguments = bundle

            val act: MainActivity = context
            detailDialog.show(
                act.supportFragmentManager,
                DetailMatchFragment::class.java.simpleName
            )
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

        init {
            tvHome = itemView.tvHomeTeam
            tvAway = itemView.tvAwayTeam
            tvDate = itemView.tvDate
            tvTime = itemView.tvTime
            tvHomeScore = itemView.tvHomeScore
            tvAwayScore = itemView.tvAwayScore
            btnMore = itemView.btnMore
            tvDate.visibility = View.INVISIBLE
            tvTime.visibility = View.INVISIBLE
        }
    }
}
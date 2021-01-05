package com.example.kade.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kade.R
import com.example.kade.model.Model

class RecyclerAdapter(var context: Context, var data: MutableList<Model>) :
    RecyclerView.Adapter<RecyclerAdapter.CustomVH>() {

    private var callback: OnClickUnselected = context as OnClickUnselected

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomVH {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_league, parent, false)
        return CustomVH(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(h: CustomVH, i: Int) {
        h.iv.setImageDrawable(context.resources.getDrawable(data.get(i).imgId, context.theme))
        h.tes.text = data.get(i).title

        h.iv.setOnClickListener { v: View? ->
            callback.changeSelected(i)
        }
    }

    class CustomVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val iv: ImageView
        val tes: TextView

        init {
            iv = itemView.findViewById(R.id.ivLL)
            tes = itemView.findViewById(R.id.tvTes)
        }
    }

    interface OnClickUnselected {
        fun changeSelected(pos: Int)
    }
}
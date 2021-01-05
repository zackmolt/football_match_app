package com.example.kade.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.example.kade.R
import com.example.kade.model.League
import com.example.kade.model.LeagueModel
import com.example.kade.mvp.DetailLeaguePresenter
import com.example.kade.mvp.DetailLeagueView
import com.example.kade.mvp.netserv.ApiRepo


import com.example.kade.mvvm.LeagueDetailViewModel
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_detail.*


class DetailActivity : AppCompatActivity(),DetailLeagueView {

    companion object {
        val KEY_EXTRA = "KEY_NAMEEXTS"
    }

    private lateinit var name: String
    private lateinit var desc: String
    private lateinit var presenter: DetailLeaguePresenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        presenter = DetailLeaguePresenter(this, ApiRepo(), Gson())

        toolbar.navigationIcon = resources.getDrawable(R.drawable.ic_arrow_back_black_24dp, theme)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        var viewModel = ViewModelProviders.of(this).get(LeagueDetailViewModel::class.java)
        viewModel.getData().observe(this, Observer {
            println(it.list.get(0).strBadge)
            Glide.with(this).load(it.list.get(0).strBadge).into(ivDetail)

            name = it.list.get(0).strLeague
            desc = it.list.get(0).strDescriptionEN
            tvNameDetail.text = name
            tvDescDetail.text = desc
        })

        //MVVM
        //viewModel.setData(intent.getIntExtra(KEY_EXTRA, 4328).toString())

        //MVP
        presenter.getDetailMatch(intent.getIntExtra(KEY_EXTRA, 4328).toString())

    }

    override fun showLoading() {

    }

    override fun hideLoading() {

    }

    override fun showData(data: List<League>) {
        println(data[0].strBadge)
        Glide.with(this).load(data.get(0).strBadge).into(ivDetail)

        name = data.get(0).strLeague
        desc = data.get(0).strDescriptionEN
        tvNameDetail.text = name
        tvDescDetail.text = desc
    }


}

package com.example.kade.fragments


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kade.R
import com.example.kade.activities.MainActivity
import com.example.kade.adapters.PrevMatchAdapter
import com.example.kade.model.BadgeModel
import com.example.kade.model.MatchDetail

import com.example.kade.mvp.MainPresenter
import com.example.kade.mvp.MainView
import com.example.kade.mvp.netserv.ApiRepo

import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_prev_match.*

/**
 * A simple [Fragment] subclass.
 */
class PrevMatchFragment(val mainActivity: MainActivity) : Fragment(),MainView {

    private lateinit var rcAdapt: PrevMatchAdapter
    private var list: MutableList<MatchDetail> = mutableListOf()
    private var listHomeUrl: MutableList<BadgeModel> = mutableListOf()
    private var listAwayUrl: MutableList<BadgeModel> = mutableListOf()
    private lateinit var presenter : MainPresenter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_prev_match, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        LocalBroadcastManager.getInstance(context!!)
            .registerReceiver(bcId, IntentFilter(MainActivity.CHANGE_ID))

        rcPrevMatch.layoutManager = LinearLayoutManager(context)

       // presenter = PrevMatchPresenter(this)

        var strId = arguments?.getString(MainActivity.KEY_ID, "4328")
        if (strId == null) {
            strId = "4328"
        }

        //MVP
        //presenter.setData(strId)

        //MVP COROU
        val request= ApiRepo()
        val gson = Gson()

        presenter= MainPresenter(this,request,gson)
        presenter.getPrevMatch(strId)
        //MVVM
        //vmObserver(strId)
    }

    /*private fun vmObserver(leagueId: String) {
        val viewModel = ViewModelProviders.of(this).get(PrevMatchViewModel::class.java)
        viewModel.getData().observe(this, Observer {
            list = it.toMutableList()
            listHomeUrl.clear()
            listAwayUrl.clear()
            rcAdapt = PrevMatchAdapter(mainActivity, list, listHomeUrl, listAwayUrl)
            rcPrevMatch.adapter = rcAdapt

        })

        viewModel.getHomePict().observe(this, Observer {
            listHomeUrl = it
            rcAdapt = PrevMatchAdapter(mainActivity, list, listHomeUrl, listAwayUrl)
            rcPrevMatch.adapter = rcAdapt

        })

        viewModel.getAwayPict().observe(this, Observer {
            listAwayUrl = it
            rcAdapt = PrevMatchAdapter(mainActivity, list, listHomeUrl, listAwayUrl)
            rcPrevMatch.adapter = rcAdapt

        })

        viewModel.setData(leagueId)
    }*/

    private val bcId: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            if (p1 != null) {
                //vmObserver(p1.getStringExtra(MainActivity.CHANGE_ID))
                presenter.getPrevMatch(p1.getStringExtra(MainActivity.CHANGE_ID))
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        LocalBroadcastManager.getInstance(context!!).unregisterReceiver(bcId)
    }

    override fun showLoading() {
        //Load
    }

    override fun hideLoading() {
        //Loaded
    }

    override fun showNextMatch(data: List<MatchDetail>) {
        list = data.toMutableList()
        listHomeUrl.clear()
        listAwayUrl.clear()
        rcAdapt = PrevMatchAdapter(mainActivity, list, listHomeUrl, listAwayUrl)
        rcPrevMatch.adapter = rcAdapt
    }


}

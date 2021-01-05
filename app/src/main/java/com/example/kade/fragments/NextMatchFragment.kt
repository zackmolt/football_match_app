package com.example.kade.fragments


import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kade.R
import com.example.kade.activities.MainActivity
import com.example.kade.adapters.NextMatchAdapter
import com.example.kade.model.BadgeModel
import com.example.kade.model.MatchDetail
import com.example.kade.model.MatchDetailModel
import com.example.kade.mvp.CoroutineContextProvider
import com.example.kade.mvp.MainPresenter
import com.example.kade.mvp.MainView
import com.example.kade.mvp.netserv.ApiRepo
import com.example.kade.mvvm.NextMatchViewModel
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_next_match.*


class NextMatchFragment(val ma: MainActivity) : Fragment(), MainView/*NextMatchView*/ {


    private lateinit var rcAdapt: NextMatchAdapter
    private var list: List<MatchDetail> = listOf()
    private var listHomeUrl: MutableList<BadgeModel> = mutableListOf()
    private var listAwayUrl: MutableList<BadgeModel> = mutableListOf()
    //private lateinit var presenter: NextMatchPresenter
    private lateinit var presenter: MainPresenter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_next_match, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        LocalBroadcastManager.getInstance(context!!)
            .registerReceiver(bcId, IntentFilter(MainActivity.CHANGE_ID))

        //presenter= NextMatchPresenter(this)

        var strId = arguments?.getString(MainActivity.KEY_ID, "4328")
        if (strId == null) {
            strId = "4328"
        }
        settingUpRecycler()

        //MVP-1
        //presenter.getData(strId)

        //MVP-2
        val request = ApiRepo()
        val gson = Gson()
        presenter = MainPresenter(this, request, gson)
        presenter.getNextMatch(strId)

        //MVVM
       // vmObserver(strId)
    }

    private val bcId = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.getStringExtra(MainActivity.CHANGE_ID) != null) {
                //vmObserver(intent.getStringExtra(MainActivity.CHANGE_ID))
                presenter.getNextMatch(intent.getStringExtra(MainActivity.CHANGE_ID))
            }
        }
    }

    /*private fun vmObserver(leagueId: String) {
        val viewModel = ViewModelProviders.of(this).get(NextMatchViewModel::class.java)
        viewModel.getData().observe(this, Observer {
            list = it
            rcAdapt = NextMatchAdapter(ma, list, listHomeUrl, listAwayUrl)
            rcNextMatch.adapter = rcAdapt
        })

        viewModel.getHomePict().observe(this, Observer {
            listHomeUrl = it
            rcAdapt = NextMatchAdapter(ma, list, listHomeUrl, listAwayUrl)
            rcNextMatch.adapter = rcAdapt
        })

        viewModel.getAwayPict().observe(this, Observer {
            listAwayUrl = it
            rcAdapt = NextMatchAdapter(ma, list, listHomeUrl, listAwayUrl)
            rcNextMatch.adapter = rcAdapt
        })

        viewModel.setData(leagueId)
    }*/

    private fun settingUpRecycler() {
        rcNextMatch.layoutManager = LinearLayoutManager(context)
    }

    override fun onDetach() {
        super.onDetach()
        LocalBroadcastManager.getInstance(context!!)
            .unregisterReceiver(bcId)
    }

    override fun showLoading() {
        //LoadData
    }

    override fun hideLoading() {
        //LoadData
    }

    override fun showNextMatch(data: List<MatchDetail>) {
        list = data
        rcAdapt = NextMatchAdapter(ma, list, listHomeUrl, listAwayUrl)
        rcNextMatch.adapter = rcAdapt
    }

    //Overriders

    /*
    override fun showData(listData: MutableList<MatchDetailModel>) {
        list = listData
        rcAdapt = NextMatchAdapter(ma, list, listHomeUrl, listAwayUrl)
        rcNextMatch.adapter = rcAdapt
    }

    override fun showHomePict(listHome: MutableList<BadgeModel>) {
        listHomeUrl = listHome
        rcAdapt = NextMatchAdapter(ma, list, listHomeUrl, listAwayUrl)
        rcNextMatch.adapter = rcAdapt
    }

    override fun showAwayPict(listAway: MutableList<BadgeModel>) {
        listAwayUrl = listAway
        rcAdapt = NextMatchAdapter(ma, list, listHomeUrl, listAwayUrl)
        rcNextMatch.adapter = rcAdapt
    }
    */
    //Overriders


}

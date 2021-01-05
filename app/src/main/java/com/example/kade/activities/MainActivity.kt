package com.example.kade.activities


import android.app.SearchManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.setPadding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.example.kade.R
import com.example.kade.adapters.*
import com.example.kade.fragments.ClassementFragment
import com.example.kade.fragments.LikeFragment
import com.example.kade.model.AllTeamsResponseChild
import com.example.kade.model.MatchDetailModel
import com.example.kade.model.Model
import com.example.kade.model.SearchChild
import com.example.kade.mvp.SearchPresenter
import com.example.kade.mvp.netserv.ApiRepo
import com.example.kade.mvp.netserv.SearchQuerView
import com.example.kade.mvvm.DataViewModel
import com.example.kade.mvvm.SearchViewModel
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson
import com.yarolegovich.discretescrollview.DiscreteScrollView
import com.yarolegovich.discretescrollview.transform.Pivot
import com.yarolegovich.discretescrollview.transform.ScaleTransformer
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.backgroundDrawable
import org.jetbrains.anko.dip


class MainActivity : AppCompatActivity(), RecyclerAdapter.OnClickUnselected, SearchQuerView {

    companion object {
        val EVENT_ID = "KEYEVENT_ID"
        val KEY_ID = "KEY_ID"
        val CHANGE_ID = "CHANGE_ID"
    }

    private var hd: Handler? = null
    private var rn: Runnable? = null
    var composSearch: CompositeDisposable? = null
    var centerPos: Int = -1
    var list: MutableList<Model> = mutableListOf()
    lateinit var rcAdapt: RecyclerAdapter
    private lateinit var vpAdapter: ViewPagerAdapter
    private var searchStatus = "Match"
    private lateinit var presenter : SearchPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        presenter = SearchPresenter(this,ApiRepo(),Gson())

        settingUpRecycler()

        vpAdapter = ViewPagerAdapter(supportFragmentManager, this@MainActivity)
        viewPager.adapter = vpAdapter

        val viewModel = ViewModelProviders.of(this).get(DataViewModel::class.java)
        viewModel.getList().observe(this, Observer {
            list = it
            rcAdapt = RecyclerAdapter(this@MainActivity, list)
            rc.adapter = rcAdapt
        })

        viewModel.setList()
        listener()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_tab, menu)

        val searchManager = getSystemService(SEARCH_SERVICE) as SearchManager
        if (searchManager != null) {
            val searchView = menu?.findItem(R.id.m_search)?.actionView as SearchView
            searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
            searchView.setIconifiedByDefault(false)
            searchView.queryHint = "Cari Pertandingan..."
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

                override fun onQueryTextChange(newText: String): Boolean {

                    return false
                }

                override fun onQueryTextSubmit(query: String): Boolean {
                    searchFor(query)
                    return false
                }

            })


            searchView.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
                override fun onViewAttachedToWindow(v: View) {
                    searchLayout.visibility = View.VISIBLE
                }

                override fun onViewDetachedFromWindow(v: View) {
                    searchLayout.visibility = View.GONE
                }
            })
        }
        return true
    }


    fun searchFor(query: String) {
        var afterParsed = ""
        for (i in 0 until query.length) {
            afterParsed += query[i].toString()
        }

        rcSearch.layoutManager = LinearLayoutManager(this)
        lateinit var searchAdap: SearchAdapter
        lateinit var searchAdapTeam: SearchTeamsAdapter

        val vmSearch=ViewModelProviders.of(this).get(SearchViewModel::class.java)
        /*vmSearch.getData().observe(this, Observer {
            searchAdap = SearchAdapter(this@MainActivity, it)
            rcSearch.adapter = searchAdap
        })
        vmSearch.getDataTeam().observe(this, Observer {
            searchAdapTeam = SearchTeamsAdapter(this@MainActivity, it)
            rcSearch.adapter = searchAdapTeam
        })*/

        if(searchStatus.equals("Match")) {
            //MVVM
            //vmSearch.setData(afterParsed)

            //MVP
            presenter.getData(afterParsed)

        }
        else {
            //MVVM
            //vmSearch.setDataTeam(afterParsed)

            //MVP
            presenter.getDataTeam(afterParsed)

        }
    }

    private fun listener() {

        fun listenerSearchOptions(){
            opMatch.setOnClickListener{
                opMatch.backgroundDrawable=resources.getDrawable(R.drawable.round_selected,theme)
                opTeam.backgroundDrawable=resources.getDrawable(R.drawable.round_unselected,theme)
                opMatch.setPadding(dip(10))
                opTeam.setPadding(dip(10))
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    opMatch.setTextColor(resources.getColor(android.R.color.white,theme))
                    opTeam.setTextColor(resources.getColor(android.R.color.black,theme))
                }


                searchStatus="Match"
            }

            opTeam.setOnClickListener {
                opMatch.backgroundDrawable=resources.getDrawable(R.drawable.round_unselected,theme)
                opTeam.backgroundDrawable=resources.getDrawable(R.drawable.round_selected,theme)
                opMatch.setPadding(dip(10))
                opTeam.setPadding(dip(10))
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    opMatch.setTextColor(resources.getColor(android.R.color.black,theme))
                    opTeam.setTextColor(resources.getColor(android.R.color.white,theme))
                }


                searchStatus="Team"
            }
        }
        listenerSearchOptions()

        btnTeams.setOnClickListener {
            val inte = Intent(this,AllTeamsActivity::class.java)
            inte.putExtra(AllTeamsActivity.LEAGUE_ID,list[centerPos].title)
            startActivity(inte)
        }

        btnClassement.setOnClickListener{
            val inte = Intent(this, ClassementFragment::class.java)
            inte.putExtra(ClassementFragment.LEAGUE_ID, list[centerPos].idLeague.toString())
            startActivity(inte)
        }

        tabLayout.addOnTabSelectedListener(object :
            TabLayout.BaseOnTabSelectedListener<TabLayout.Tab> {
            override fun onTabReselected(p0: TabLayout.Tab?) {

            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {

            }

            override fun onTabSelected(p0: TabLayout.Tab?) {
                if (p0 != null) {
                    viewPager.setCurrentItem(p0.position, true)
                }
            }

        })

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageSelected(position: Int) {
                val tab = tabLayout.getTabAt(position)
                if (tab != null) {
                    tab.select()
                }
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                tabLayout.setScrollPosition(position, positionOffset, false)
            }

        })

        btnDetailLeague.setOnClickListener {
            val inte = Intent(this, DetailActivity::class.java)
            inte.putExtra(DetailActivity.KEY_EXTRA, list.get(centerPos).idLeague)
            startActivity(inte)
        }

        rc.addOnItemChangedListener(DiscreteScrollView.OnItemChangedListener { t: RecyclerView.ViewHolder?, i ->
            centerPos = i
            var inte = Intent(CHANGE_ID)
            inte.putExtra(CHANGE_ID, list.get(i).idLeague.toString())
            LocalBroadcastManager.getInstance(this@MainActivity).sendBroadcast(inte)
        })
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.m_fav -> {
                startActivity(Intent(this, LikeFragment::class.java))
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun settingUpRecycler() {
        rcAdapt = RecyclerAdapter(this@MainActivity, list)
        rc.setSlideOnFling(true)
        rc.adapter = rcAdapt
        rc.setItemTransformer(
            ScaleTransformer.Builder()
                .setMaxScale(1.05f)
                .setMinScale(0.8f)
                .setPivotX(Pivot.X.CENTER)
                .setPivotY(Pivot.Y.BOTTOM).build()
        )
    }

    override fun changeSelected(pos: Int) {
        rc.smoothScrollToPosition(pos)
    }

    override fun onDestroy() {
        super.onDestroy()
        composSearch?.clear()
    }


    //Overriders

    override fun showLoading() {

    }

    override fun hideLoading() {

    }

    override fun showData(data: List<SearchChild>) {
        var searchAdap = SearchAdapter(this@MainActivity, data)
        rcSearch.adapter = searchAdap
    }

    override fun showDataTeam(data: List<AllTeamsResponseChild>) {
        var searchAdapTeam = SearchTeamsAdapter(this@MainActivity, data.toMutableList())
        rcSearch.adapter = searchAdapTeam
    }
}

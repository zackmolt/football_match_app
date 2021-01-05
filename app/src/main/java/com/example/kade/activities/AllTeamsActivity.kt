package com.example.kade.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kade.R
import com.example.kade.adapters.AllTeamsAdapter
import com.example.kade.mvvm.AllTeamsViewModel
import kotlinx.android.synthetic.main.activity_all_teams.*

class AllTeamsActivity : AppCompatActivity() {

    companion object{
        const val LEAGUE_ID="LGID"
    }

    private lateinit var adapter:AllTeamsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_teams)

        supportActionBar?.title=""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        rcAllTeams.layoutManager = LinearLayoutManager(this)
        var idLeague= intent.extras.getString(LEAGUE_ID,"4328")
        loadData(idLeague)

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when(item?.itemId){
            android.R.id.home ->{
                this.finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }

    fun loadData(id:String){
        val viewModel=ViewModelProviders.of(this).get(AllTeamsViewModel::class.java)
        viewModel.getData().observe(this, Observer {
            println("IIINNN ON STEP 4 HHHHHHHHHEEEEEEERRRREEEE")
            adapter = AllTeamsAdapter(this, it)
            rcAllTeams.adapter = adapter
        })
        viewModel.setData(id)
    }
}

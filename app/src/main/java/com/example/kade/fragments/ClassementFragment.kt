package com.example.kade.fragments


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kade.R
import com.example.kade.adapters.ClassementPointAdapter
import com.example.kade.adapters.ClassementTeamAdapter
import com.example.kade.mvvm.ClassementViewModel
import kotlinx.android.synthetic.main.fragment_classement.*


class ClassementFragment : AppCompatActivity() {

    companion object{
        const val LEAGUE_ID="LEAGUE_ID"
    }

    private lateinit var teamAdapter:ClassementTeamAdapter
    private lateinit var pointAdapter:ClassementPointAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_classement)
        rcClassementTeam.layoutManager = LinearLayoutManager(this)
        rcClassementPoint.layoutManager = LinearLayoutManager(this)

        var lgId=intent.extras.getString(LEAGUE_ID,"4328")

        if (lgId != null) {
            loadData(lgId)
        }

        btnClose.setOnClickListener{
            this.finish()
        }
    }

    private fun loadData(id:String){
        val viewModel=ViewModelProviders.of(this).get(ClassementViewModel::class.java)
        viewModel.getData().observe(this, Observer {
            teamAdapter= ClassementTeamAdapter(this,it)
            rcClassementTeam.adapter=teamAdapter

            pointAdapter= ClassementPointAdapter(this,it)
            rcClassementPoint.adapter=pointAdapter
        })
        viewModel.setData(id)
    }


}

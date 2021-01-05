package com.example.kade.mvvm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kade.R
import com.example.kade.model.Model

class DataViewModel : ViewModel() {
    var listData: MutableLiveData<MutableList<Model>> = MutableLiveData()

    fun getList(): LiveData<MutableList<Model>> {
        return listData
    }

    fun setList() {
        var list: MutableList<Model> = mutableListOf()
        list.add(
            Model(
                "English Premier League",
                R.drawable.english_premier_league,
                4328
            )
        )
        list.add(
            Model(
                "English League 1",
                R.drawable.english_league_1,
                4396
            )
        )
        list.add(
            Model(
                "French Ligue 1",
                R.drawable.french_ligue_1,
                4334
            )
        )
        list.add(
            Model(
                "German Bundesliga",
                R.drawable.german_bundesliga,
                4331
            )
        )
        list.add(
            Model(
                "Italian Serie A",
                R.drawable.italian_serie_a,
                4332
            )
        )
        list.add(
            Model(
                "Spanish La Liga",
                R.drawable.spanish_la_liga,
                4335
            )
        )
        list.add(
            Model(
                "American Mayor League",
                R.drawable.american_mayor_league,
                4346
            )
        )
        list.add(
            Model(
                "Protugeuese Premiera Liga",
                R.drawable.portugeuese_premiera_liga,
                4344
            )
        )
        list.add(
            Model(
                "Scotish Premier League",
                R.drawable.scotish_premier_league,
                4330
            )
        )
        list.add(
            Model(
                "Greek Superleague Greece",
                R.drawable.scotish_premier_league,
                4336
            )
        )
        list.add(
            Model(
                "Dutch Erdivisie",
                R.drawable.scotish_premier_league,
                4337
            )
        )
        list.add(
            Model(
                "Belgian Jupiler League",
                R.drawable.scotish_premier_league,
                4338
            )
        )
        list.add(
            Model(
                "Turkish Super Lig",
                R.drawable.scotish_premier_league,
                4339
            )
        )
        list.add(
            Model(
                "Danish Superliga",
                R.drawable.scotish_premier_league,
                4340
            )
        )
        list.add(
            Model(
                "Swedish Allsvenskan",
                R.drawable.scotish_premier_league,
                4347
            )
        )
        list.add(
            Model(
                "Mexican Primera League",
                R.drawable.scotish_premier_league,
                4350
            )
        )
        list.add(
            Model(
                "Brazilian Brasileirao",
                R.drawable.scotish_premier_league,
                4351
            )
        )
        list.add(
            Model(
                "Ukrainian Premier League",
                R.drawable.scotish_premier_league,
                4354
            )
        )
        list.add(
            Model(
                "Russian Football Premier League",
                R.drawable.scotish_premier_league,
                4355
            )
        )
        list.add(
            Model(
                "Australian A-League",
                R.drawable.australian_a_league,
                4356
            )
        )
        list.add(
            Model(
                "Eliteserien",
                R.drawable.scotish_premier_league,
                4358
            )
        )
        list.add(
            Model(
                "Chinese Super League",
                R.drawable.scotish_premier_league,
                4359
            )
        )

        listData.postValue(list)
    }


}
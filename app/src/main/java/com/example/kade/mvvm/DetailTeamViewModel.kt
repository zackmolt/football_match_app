package com.example.kade.mvvm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kade.apiservices.GetDetailTeam
import com.example.kade.apiservices.RetroClient
import com.example.kade.model.TeamResponse
import com.example.kade.model.TeamResponseChild
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class DetailTeamViewModel : ViewModel(){

    val compos:CompositeDisposable? = CompositeDisposable()
    val dataApi = RetroClient.instance.create(GetDetailTeam::class.java)

    var listData:MutableLiveData<MutableList<TeamResponseChild>> = MutableLiveData()
    fun getData() : LiveData<MutableList<TeamResponseChild>> = listData
    fun setData(id:String){
        compos?.addAll(dataApi.getData(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({
                displayData(it)
            },{}))
    }

    private fun displayData(it: TeamResponse?) {
        if(it!=null){
            if(it.teams!=null && it.teams.size!=0){
                listData.postValue(it.teams)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        compos?.clear()
    }
}
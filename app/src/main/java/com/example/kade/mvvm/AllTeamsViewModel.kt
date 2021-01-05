package com.example.kade.mvvm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kade.apiservices.GetAllTeams
import com.example.kade.apiservices.RetroClient
import com.example.kade.model.AllTeamsResponse
import com.example.kade.model.AllTeamsResponseChild
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.lang.Exception

class AllTeamsViewModel : ViewModel(){

    val compos: CompositeDisposable?=CompositeDisposable()
    val dataApi=RetroClient.instance.create(GetAllTeams::class.java)

    var listData: MutableLiveData<MutableList<AllTeamsResponseChild>> = MutableLiveData()
    fun getData():LiveData<MutableList<AllTeamsResponseChild>> = listData
    fun setData(id:String){

        compos?.addAll(dataApi.getData(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                displayData(it)
                try {
                    println(it.teams[0].strTeam+"CGDTZ")
                }catch (e:Exception){
                    println("CGDTZ : FAILED")
                }
            },{})
        )
    }

    private fun displayData(it: AllTeamsResponse?) {
        if(it!=null){
            if(it.teams !=null && it.teams.size != 0){
                println("IIINNN ON STEP 3 HHHHHHHHHEEEEEEERRRREEEE")
                listData.postValue(it.teams)
            }
        }
    }


    override fun onCleared() {
        super.onCleared()
        compos?.clear()
    }
}
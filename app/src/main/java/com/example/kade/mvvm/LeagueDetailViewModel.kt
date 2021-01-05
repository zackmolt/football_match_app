package com.example.kade.mvvm


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kade.apiservices.RetroClient
import com.example.kade.apiservices.leaguedetail.GetDataLeagueDetail
import com.example.kade.model.LeagueModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class LeagueDetailViewModel: ViewModel() {
    var compos: CompositeDisposable? = CompositeDisposable()
    var listData: MutableLiveData<LeagueModel> = MutableLiveData()
    fun getData(): LiveData<LeagueModel> = listData
    fun setData(leagueId: String) {
        var dataApi: GetDataLeagueDetail =
            RetroClient.instance.create(GetDataLeagueDetail::class.java)

        compos?.addAll(dataApi.getData(leagueId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { onRetrievePostListStart() }
            .doOnTerminate { onRetrievePostListFinish() }
            .subscribe(
                { listData.postValue(it) },
                { onRetrievePostListError() }
            )
        )
    }


    private fun onRetrievePostListStart() {

    }

    private fun onRetrievePostListFinish() {

    }

    private fun onRetrievePostListError() {

    }

    override fun onCleared() {
        super.onCleared()
        compos?.clear()
    }
}
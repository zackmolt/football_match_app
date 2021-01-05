package com.example.kade.mvvm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kade.apiservices.GetClassement
import com.example.kade.apiservices.RetroClient
import com.example.kade.model.TableModel
import com.example.kade.model.TableResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ClassementViewModel : ViewModel() {

    var compos: CompositeDisposable? = CompositeDisposable()
    var dataApi: GetClassement = RetroClient.instance.create(GetClassement::class.java)

    var listData: MutableLiveData<MutableList<TableModel>> = MutableLiveData()
    fun getData(): LiveData<MutableList<TableModel>> = listData
    fun setData(id: String) {


        compos?.addAll(dataApi.getData(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                display(it)
            }, {

            })
        )

    }

    private fun display(it: TableResponse?) {
        if (it != null) {
            if (it.table != null && it.table.size != 0) {
                var rawData: MutableList<TableModel> = mutableListOf()
                for (i in 0 until it.table.size) {
                    rawData.add(
                        TableModel(
                            it.table[i].name!!,
                            it.table[i].teamid!!,
                            it.table[i].played!!,
                            it.table[i].goalsfor!!,
                            it.table[i].goalsagainst!!,
                            it.table[i].goalsdifference!!,
                            it.table[i].win!!,
                            it.table[i].draw!!,
                            it.table[i].loss!!,
                            it.table[i].total!!
                        )
                    )
                }
                listData.postValue(rawData)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        compos?.clear()
    }
}
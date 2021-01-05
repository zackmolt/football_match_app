package com.example.kade.mvp

import com.example.kade.model.ListMatchModel
import com.example.kade.model.MatchDetail
import com.example.kade.model.SearchChild
import com.example.kade.model.SearchModel
import com.example.kade.mvp.netserv.ApiRepo
import com.example.kade.mvp.netserv.SearchQuerView
import com.google.gson.Gson
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class SearchPresenterTest{
    @Mock
    private lateinit var view: SearchQuerView

    @Mock
    private lateinit var gson: Gson

    @Mock
    private lateinit var apiRepository: ApiRepo

    @Mock
    private lateinit var apiResponse: Deferred<String>

    private lateinit var presenter: SearchPresenter

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        presenter =  SearchPresenter(view, apiRepository, gson, TestContextProvider())
    }

    @Test
    fun testGetSearchList() {
        val list: MutableList<SearchChild> = mutableListOf()
        val response = SearchModel(list.toList())
        val league = "4328"

        runBlocking {
            Mockito.`when`(apiRepository.doRequest(ArgumentMatchers.anyString()))
                .thenReturn(apiResponse)

            Mockito.`when`(apiResponse.await()).thenReturn("")

            Mockito.`when`(
                gson.fromJson(
                    "",
                    SearchModel::class.java
                )
            ).thenReturn(response)

            presenter.getData(league)

            Mockito.verify(view).showLoading()
            Mockito.verify(view).showData(list)
            Mockito.verify(view).hideLoading()
        }
    }}
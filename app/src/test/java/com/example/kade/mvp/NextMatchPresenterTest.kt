package com.example.kade.mvp

import com.example.kade.model.ListMatchModel
import com.example.kade.model.MatchDetail
import com.example.kade.mvp.netserv.ApiRepo
import com.google.gson.Gson
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations


class NextMatchPresenterTest {
    @Mock
    private lateinit var view: MainView

    @Mock
    private lateinit var gson: Gson

    @Mock
    private lateinit var apiRepository: ApiRepo

    @Mock
    private lateinit var apiResponse: Deferred<String>

    private lateinit var presenter: MainPresenter

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        presenter =  MainPresenter(view, apiRepository, gson, TestContextProvider())
    }

   @Test
    fun testGetNextMatchList() {
        val teams: MutableList<MatchDetail> = mutableListOf()
        val response = ListMatchModel(teams)
        val league = "4328"

        runBlocking {
            Mockito.`when`(apiRepository.doRequest(ArgumentMatchers.anyString()))
                .thenReturn(apiResponse)

            Mockito.`when`(apiResponse.await()).thenReturn("")

            Mockito.`when`(
                gson.fromJson(
                    "",
                    ListMatchModel::class.java
                )
            ).thenReturn(response)

            presenter.getNextMatch(league)

            Mockito.verify(view).showLoading()
            Mockito.verify(view).showNextMatch(teams)
            Mockito.verify(view).hideLoading()
        }
    }

}
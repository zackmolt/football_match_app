package com.example.kade.mvp

import com.example.kade.model.Childre
import com.example.kade.model.DetailMatch
import com.example.kade.model.League
import com.example.kade.model.LeagueModel
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

class DetailLeaguePresenterTest {
    @Mock
    private lateinit var view: DetailLeagueView

    @Mock
    private lateinit var gson: Gson

    @Mock
    private lateinit var apiRepository: ApiRepo

    @Mock
    private lateinit var apiResponse: Deferred<String>

    private lateinit var presenter: DetailLeaguePresenter


    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        presenter =  DetailLeaguePresenter(view, apiRepository, gson, TestContextProvider())
    }
    @Test
    fun testGetDetailMatch() {
        val list: MutableList<League> = mutableListOf()
        val response = LeagueModel(list.toList())
        val league = "4328"

        runBlocking {
            Mockito.`when`(apiRepository.doRequest(ArgumentMatchers.anyString()))
                .thenReturn(apiResponse)

            Mockito.`when`(apiResponse.await()).thenReturn("")

            Mockito.`when`(
                gson.fromJson(
                    "",
                    LeagueModel::class.java
                )
            ).thenReturn(response)

            presenter.getDetailMatch(league)

            Mockito.verify(view).showLoading()
            Mockito.verify(view).showData(list)
            Mockito.verify(view).hideLoading()
        }
    }
}
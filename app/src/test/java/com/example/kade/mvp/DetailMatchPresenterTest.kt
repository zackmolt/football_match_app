package com.example.kade.mvp

import com.example.kade.model.*
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

class DetailMatchPresenterTest {
    @Mock
    private lateinit var view: DetailMatchView

    @Mock
    private lateinit var gson: Gson

    @Mock
    private lateinit var apiRepository: ApiRepo

    @Mock
    private lateinit var apiResponse: Deferred<String>

    private lateinit var presenter: DetailMatchPresenter


    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        presenter =  DetailMatchPresenter(view, apiRepository, gson, TestContextProvider())
    }
    @Test
    fun testGetDetailMatch() {
        val list: MutableList<Childre> = mutableListOf()
        val response = DetailMatch(list.toList())
        val league = "441613"

        runBlocking {
            Mockito.`when`(apiRepository.doRequest(ArgumentMatchers.anyString()))
                .thenReturn(apiResponse)

            Mockito.`when`(apiResponse.await()).thenReturn("")

            Mockito.`when`(
                gson.fromJson(
                    "",
                    DetailMatch::class.java
                )
            ).thenReturn(response)

            presenter.getDetailMatch(league)

            Mockito.verify(view).showLoading()
            Mockito.verify(view).showData(list)
            Mockito.verify(view).hideLoading()
        }
    }
}
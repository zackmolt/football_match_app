package com.example.kade.mvp

import com.example.kade.mvp.netserv.ApiRepo
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

class ApiRepoTest {
    @Test
    fun testDoRequest() {
        val apiRepository = mock(ApiRepo::class.java)
        val url = "https://www.thesportsdb.com/api/v1/json/1/lookupleague.php?id=4328"
        apiRepository.doRequest(url)
        verify(apiRepository).doRequest(url)
    }
}
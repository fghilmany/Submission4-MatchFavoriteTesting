package com.matchfavorite.ai.sub3_matchfavorite.match

import com.google.gson.Gson
import com.matchfavorite.ai.sub3_matchfavorite.TestContextProvider
import com.matchfavorite.ai.sub3_matchfavorite.api.ApiRepository
import com.matchfavorite.ai.sub3_matchfavorite.api.TheSportDBApi
import com.matchfavorite.ai.sub3_matchfavorite.model.Match
import com.matchfavorite.ai.sub3_matchfavorite.model.MatchResponse
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class MatchPresenterTest {
    @Mock
    private
    lateinit var view: MatchView

    @Mock
    private
    lateinit var gson: Gson

    @Mock
    private
    lateinit var apiRepository: ApiRepository

    private lateinit var presenter: MatchPresenter

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        presenter = MatchPresenter(view, apiRepository, gson, TestContextProvider())
    }

    @Test
    fun testGetMatchesList() {
        val matches: MutableList<Match> = mutableListOf()
        val response = MatchResponse(matches)
        val past = true

        Mockito.`when`(gson.fromJson(apiRepository
                .doRequest(TheSportDBApi.getMatchList(past)),
                MatchResponse::class.java
        )).thenReturn(response)

        presenter.getMatchesList(past)

        Mockito.verify(view).showLoading()
        Mockito.verify(view).showMatchList(matches)
        Mockito.verify(view).hideLoading()
    }

}
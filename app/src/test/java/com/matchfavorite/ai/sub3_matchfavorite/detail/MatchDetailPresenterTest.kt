package com.matchfavorite.ai.sub3_matchfavorite.detail


import com.google.gson.Gson
import com.matchfavorite.ai.sub3_matchfavorite.TestContextProvider
import com.matchfavorite.ai.sub3_matchfavorite.api.ApiRepository
import com.matchfavorite.ai.sub3_matchfavorite.api.TheSportDBApi
import com.matchfavorite.ai.sub3_matchfavorite.model.Match
import com.matchfavorite.ai.sub3_matchfavorite.model.MatchResponse
import com.matchfavorite.ai.sub3_matchfavorite.model.Team
import com.matchfavorite.ai.sub3_matchfavorite.model.TeamResponse
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class MatchDetailPresenterTest {
    @Mock
    private
    lateinit var view: MatchDetailView

    @Mock
    private
    lateinit var gson: Gson

    @Mock
    private
    lateinit var apiRepository: ApiRepository

    private lateinit var presenter: MatchDetailPresenter

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        presenter = MatchDetailPresenter(view, apiRepository, gson, TestContextProvider())
    }

    @Test
    fun testGetTeamDetail() {
        val match: MutableList<Match> = mutableListOf()
        val response = MatchResponse(match)
        val home: MutableList<Team> = mutableListOf()
        val homeResponse = TeamResponse(home)
        val away: MutableList<Team> = mutableListOf()
        val awayResponse = TeamResponse(away)
        val id = "579198"
        val homeId = "133658"
        val awayId = "133653"

        Mockito.`when`(gson.fromJson(apiRepository
                .doRequest(TheSportDBApi.getMatchDetail(id)),
                MatchResponse::class.java
        )).thenReturn(response)
        Mockito.`when`(gson.fromJson(apiRepository
                .doRequest(TheSportDBApi.getTeamDetail(homeId)),
                TeamResponse::class.java
        )).thenReturn(homeResponse)
        Mockito.`when`(gson.fromJson(apiRepository
                .doRequest(TheSportDBApi.getTeamDetail(awayId)),
                TeamResponse::class.java
        )).thenReturn(awayResponse)

        presenter.getMatchDetail(id)

        if (home.isNotEmpty()) {
            Mockito.verify(view).showLoading()
            Mockito.verify(view).showMatchDetail(match)
            Mockito.verify(view).showTeamBadge(home, away)
            Mockito.verify(view).hideLoading()
        }
    }

}
package com.matchfavorite.ai.sub3_matchfavorite.detail

import com.google.gson.Gson
import com.matchfavorite.ai.sub3_matchfavorite.api.ApiRepository
import com.matchfavorite.ai.sub3_matchfavorite.api.TheSportDBApi
import com.matchfavorite.ai.sub3_matchfavorite.model.MatchResponse
import com.matchfavorite.ai.sub3_matchfavorite.model.TeamResponse
import com.matchfavorite.ai.sub3_matchfavorite.util.CoroutineContextProvider
import com.matchfavorite.ai.sub3_matchfavorite.util.EspressoIdlingResource
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg

class MatchDetailPresenter(private val view: MatchDetailView,
                           private val apiRepository: ApiRepository,
                           private val gson: Gson, private val contextDetail: CoroutineContextProvider = CoroutineContextProvider()) {

    var apiRequest : Deferred<Any> = async(CommonPool){}

    fun detach() {
        if (apiRequest.isActive) {
            apiRequest.cancel()
        }
    }

    fun getMatchDetail(matchId: String) {
        view.showLoading()
        EspressoIdlingResource.increment()
        async(contextDetail.main) {
            val data = bg {
                gson.fromJson(apiRepository
                        .doRequest(TheSportDBApi.getMatchDetail(matchId)),
                        MatchResponse::class.java
                )
            }.await()

            val home = bg {
                gson.fromJson(apiRepository
                        .doRequest(TheSportDBApi.getTeamDetail(data.events[0].teamHomeID)),
                        TeamResponse::class.java
                )
            }
            val away = bg {
                gson.fromJson(apiRepository
                        .doRequest(TheSportDBApi.getTeamDetail(data.events[0].teamAwayID)),
                        TeamResponse::class.java
                )
            }

            if (home.await().teams.isNotEmpty()) {
                view.hideLoading()
                view.showMatchDetail(data.events)
                view.showTeamBadge(home.await().teams, away.await().teams)
                EspressoIdlingResource.decrement()
            }
        }
    }
}
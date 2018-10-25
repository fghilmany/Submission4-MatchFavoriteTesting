package com.matchfavorite.ai.sub3_matchfavorite.match

import com.google.gson.Gson
import com.matchfavorite.ai.sub3_matchfavorite.api.ApiRepository
import com.matchfavorite.ai.sub3_matchfavorite.api.TheSportDBApi
import com.matchfavorite.ai.sub3_matchfavorite.model.MatchResponse
import com.matchfavorite.ai.sub3_matchfavorite.util.CoroutineContextProvider
import com.matchfavorite.ai.sub3_matchfavorite.util.EspressoIdlingResource
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class MatchPresenter(private val view: MatchView,
                     private val apiRepository: ApiRepository,
                     private val gson: Gson, private val context: CoroutineContextProvider = CoroutineContextProvider()) {

    fun getMatchesList(past: Boolean) {
        view.showLoading()
        EspressoIdlingResource.increment()
        async(context.main){
            val data = bg {
                gson.fromJson(apiRepository
                        .doRequest(TheSportDBApi.getMatchList(past)),
                        MatchResponse::class.java
                )
            }
            if (data.await().events.isNotEmpty()) EspressoIdlingResource.decrement()
            view.hideLoading()
            view.showMatchList(data.await().events)
        }
    }
}
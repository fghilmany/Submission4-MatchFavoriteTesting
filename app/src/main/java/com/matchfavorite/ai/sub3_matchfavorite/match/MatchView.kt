package com.matchfavorite.ai.sub3_matchfavorite.match

import com.matchfavorite.ai.sub3_matchfavorite.model.Match

interface MatchView {
    fun showLoading()
    fun hideLoading()
    fun showMatchList(data: List<Match> = emptyList())
}

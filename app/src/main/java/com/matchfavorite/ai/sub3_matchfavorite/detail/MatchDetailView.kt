package com.matchfavorite.ai.sub3_matchfavorite.detail

import com.matchfavorite.ai.sub3_matchfavorite.model.Match
import com.matchfavorite.ai.sub3_matchfavorite.model.Team

interface MatchDetailView {
    fun showLoading()
    fun hideLoading()
    fun showMatchDetail(data: List<Match>)
    fun showTeamBadge(homeBadge: List<Team>, awayBadge: List<Team>)
}
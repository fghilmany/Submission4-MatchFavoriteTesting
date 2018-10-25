package com.matchfavorite.ai.sub3_matchfavorite.api

import com.matchfavorite.ai.sub3_matchfavorite.BuildConfig

object TheSportDBApi {
    fun getMatchList(past: Boolean = true): String {
        var url = "eventspastleague.php"
        val idLeague = "4331"
        if (!past) url = "eventsnextleague.php"

        return BuildConfig.BASE_URL + "/api/v1/json/${BuildConfig.TSDB_API_KEY}" + "/$url?id=" + idLeague
    }

    fun getMatchDetail(matchId: String?): String{
        val url = "lookupevent.php"
        return BuildConfig.BASE_URL + "/api/v1/json/${BuildConfig.TSDB_API_KEY}" + "/$url?id=" + matchId
    }

    fun getTeamDetail(teamId: String?): String{
        val url = "lookupteam.php"
        return BuildConfig.BASE_URL + "/api/v1/json/${BuildConfig.TSDB_API_KEY}" + "/$url?id=" + teamId
    }
}
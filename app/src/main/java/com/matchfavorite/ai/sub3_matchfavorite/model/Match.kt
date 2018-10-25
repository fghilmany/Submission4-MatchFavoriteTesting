package com.matchfavorite.ai.sub3_matchfavorite.model

import com.google.gson.annotations.SerializedName

data class Match(
        @SerializedName("idEvent")
        var matchId: String? = null,

        @SerializedName("strDate")
        var matchDate: String? = null,

        @SerializedName("strTime")
        var matchTime: String? = null,

        @SerializedName("idHomeTeam")
        var teamHomeID: String? = null,

        @SerializedName("idAwayTeam")
        var teamAwayID: String? = null,

        @SerializedName("strHomeTeam")
        var teamHomeName: String? = null,

        @SerializedName("strAwayTeam")
        var teamAwayName: String? = null,

        @SerializedName("intHomeScore")
        var teamHomeScore: String? = null,

        @SerializedName("intAwayScore")
        var teamAwayScore: String? = null,

        @SerializedName("strHomeGoalDetails")
        var teamHomeGoal: String? = null,

        @SerializedName("strAwayGoalDetails")
        var teamAwayGoal: String? = null,

        @SerializedName("intHomeShots")
        var teamHomeShots: String? = null,

        @SerializedName("intAwayShots")
        var teamAwayShots: String? = null,

        @SerializedName("strHomeLineupGoalkeeper")
        var teamHomeGK: String? = null,

        @SerializedName("strHomeLineupDefense")
        var teamHomeDF: String? = null,

        @SerializedName("strHomeLineupMidfield")
        var teamHomeMF: String? = null,

        @SerializedName("strHomeLineupForward")
        var teamHomeFW: String? = null,

        @SerializedName("strHomeLineupSubstitutes")
        var teamHomeSub: String? = null,

        @SerializedName("strAwayLineupGoalkeeper")
        var teamAwayGK: String? = null,

        @SerializedName("strAwayLineupDefense")
        var teamAwayDF: String? = null,

        @SerializedName("strAwayLineupMidfield")
        var teamAwayMF: String? = null,

        @SerializedName("strAwayLineupForward")
        var teamAwayFW: String? = null,

        @SerializedName("strAwayLineupSubstitutes")
        var teamAwaySub: String? = null

)

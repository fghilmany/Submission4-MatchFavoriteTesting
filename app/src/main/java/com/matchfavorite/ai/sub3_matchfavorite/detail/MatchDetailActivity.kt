package com.matchfavorite.ai.sub3_matchfavorite.detail
import android.database.sqlite.SQLiteConstraintException
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.app.AppCompatDelegate
import android.text.InputType
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.matchfavorite.ai.sub3_matchfavorite.R
import com.matchfavorite.ai.sub3_matchfavorite.R.drawable.ic_add_to_favorites
import com.matchfavorite.ai.sub3_matchfavorite.R.drawable.ic_added_to_favorites
import com.matchfavorite.ai.sub3_matchfavorite.R.id.add_to_favorite
import com.matchfavorite.ai.sub3_matchfavorite.R.menu.menu_detail
import com.matchfavorite.ai.sub3_matchfavorite.api.ApiRepository
import com.matchfavorite.ai.sub3_matchfavorite.db.Favorite
import com.matchfavorite.ai.sub3_matchfavorite.db.database
import com.matchfavorite.ai.sub3_matchfavorite.model.Match
import com.matchfavorite.ai.sub3_matchfavorite.model.Team
import com.matchfavorite.ai.sub3_matchfavorite.util.*
import org.jetbrains.anko.*
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select
import org.jetbrains.anko.design.snackbar
import org.jetbrains.anko.support.v4.onRefresh
import org.jetbrains.anko.support.v4.swipeRefreshLayout


class MatchDetailActivity : AppCompatActivity(), MatchDetailView {

    private lateinit var presenter: MatchDetailPresenter
    private lateinit var progressBar: ProgressBar
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var matchId: String
    private var matches: MutableList<Match> = mutableListOf()
    private var menuItem: Menu? = null
    private var isFavorite: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        matchId = intent.getStringExtra("id")

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        supportActionBar?.title = getText(R.string.title_detail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        MatchDetailUI().setContentView(this)

        progressBar = find(MatchDetailUI.progressBar)
        swipeRefresh = find(MatchDetailUI.swipeRefresh)

        favoriteState()
        val request = ApiRepository()
        val gson = Gson()
        presenter = MatchDetailPresenter(this, request, gson)
        presenter.getMatchDetail(matchId)

        swipeRefresh.onRefresh {
            presenter.getMatchDetail(matchId)
        }

    }

    override fun onDestroy() {
        presenter.detach()
        Log.i("MatchDetailActivity", "Activity is destroyed.")
        super.onDestroy()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(menu_detail, menu)
        menuItem = menu
        setFavorite()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            add_to_favorite -> {
                if (isFavorite) removeFromFavorite() else addToFavorite()

                isFavorite = !isFavorite
                setFavorite()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun favoriteState(){
        database.use {
            val result = select(Favorite.TABLE_FAVORITE)
                    .whereArgs("(MATCH_ID = {id})",
                            "id" to matchId)
            val favorite = result.parseList(classParser<Favorite>())
            if (!favorite.isEmpty()) isFavorite = true
        }
    }

    private fun addToFavorite(){
        try {
            database.use {
                insert(Favorite.TABLE_FAVORITE,
                        Favorite.MATCH_ID to matches[0].matchId,
                        Favorite.MATCH_DATE to parseDate(matches[0].matchDate, matches[0].matchTime),
                        Favorite.TEAM_HOME_NAME to matches[0].teamHomeName,
                        Favorite.TEAM_HOME_SCORE to matches[0].teamHomeScore,
                        Favorite.TEAM_AWAY_NAME to matches[0].teamAwayName,
                        Favorite.TEAM_AWAY_SCORE to matches[0].teamAwayScore
                )
            }
            snackbar(swipeRefresh, getString(R.string.added_to_fav)).show()
        } catch (e: SQLiteConstraintException){
            snackbar(swipeRefresh, e.localizedMessage).show()
        }
    }

    private fun removeFromFavorite(){
        try {
            database.use {
                delete(Favorite.TABLE_FAVORITE, "(MATCH_ID = {id})",
                        "id" to matchId)
            }
            snackbar(swipeRefresh, getString(R.string.removed_from_fav)).show()
        } catch (e: SQLiteConstraintException){
            snackbar(swipeRefresh, e.localizedMessage).show()
        }
    }

    private fun setFavorite() {
        if (isFavorite)
            menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(this, ic_added_to_favorites)
        else
            menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(this, ic_add_to_favorites)
    }

    override fun showLoading() {
        progressBar.visible()
    }

    override fun hideLoading() {
        progressBar.invisible()
    }

    override fun showMatchDetail(data: List<Match>) {
        val matchDate = find<TextView>(MatchDetailUI.matchDate)
        val homeName = find<TextView>(MatchDetailUI.teamHomeName)
        val awayName = find<TextView>(MatchDetailUI.teamAwayName)
        val homeScore = find<TextView>(MatchDetailUI.teamHomeScore)
        val awayScore = find<TextView>(MatchDetailUI.teamAwayScore)
        val homeGoals = find<TextView>(MatchDetailUI.teamHomeGoals)
        val awayGoals = find<TextView>(MatchDetailUI.teamAwayGoals)
        val homeShots = find<TextView>(MatchDetailUI.teamHomeShots)
        val awayShots = find<TextView>(MatchDetailUI.teamAwayShots)
        val homeGK = find<TextView>(MatchDetailUI.teamHomeGK)
        val awayGK = find<TextView>(MatchDetailUI.teamAwayGK)
        val homeDF = find<TextView>(MatchDetailUI.teamHomeDF)
        val awayDF = find<TextView>(MatchDetailUI.teamAwayDF)
        val homeMF = find<TextView>(MatchDetailUI.teamHomeMF)
        val awayMF = find<TextView>(MatchDetailUI.teamAwayMF)
        val homeFW = find<TextView>(MatchDetailUI.teamHomeFW)
        val awayFW = find<TextView>(MatchDetailUI.teamAwayFW)
        val homeSub = find<TextView>(MatchDetailUI.teamHomeSub)
        val awaySub = find<TextView>(MatchDetailUI.teamAwaySub)

        swipeRefresh.isRefreshing = false
        matches.addAll(data)

        matchDate.text = parseDate(data[0].matchDate, data[0].matchTime)
        homeName.text = data[0].teamHomeName
        awayName.text = data[0].teamAwayName
        homeScore.text = if ( data[0].teamHomeScore != null) data[0].teamHomeScore else null
        awayScore.text = if ( data[0].teamAwayScore != null) data[0].teamAwayScore else null
        homeGoals.text = parseGoals(data[0].teamHomeGoal)
        awayGoals.text = parseGoals(data[0].teamAwayGoal)
        homeShots.text = data[0].teamHomeShots
        awayShots.text = data[0].teamAwayShots
        homeGK.text = parseSemicolons(data[0].teamHomeGK)
        awayGK.text = parseSemicolons(data[0].teamAwayGK)
        homeDF.text = parseSemicolons(data[0].teamHomeDF)
        awayDF.text = parseSemicolons(data[0].teamAwayDF)
        homeMF.text = parseSemicolons(data[0].teamHomeMF)
        awayMF.text = parseSemicolons(data[0].teamAwayMF)
        homeFW.text = parseSemicolons(data[0].teamHomeFW)
        awayFW.text = parseSemicolons(data[0].teamAwayFW)
        homeSub.text = parseSemicolons(data[0].teamHomeSub)
        awaySub.text = parseSemicolons(data[0].teamAwaySub)

        homeGoals.setLineSpacing(1F, 1.5F)
        awayGoals.setLineSpacing(1F, 1.5F)
        homeGK.setLineSpacing(1F, 1.5F)
        awayGK.setLineSpacing(1F, 1.5F)
        homeDF.setLineSpacing(1F, 1.5F)
        awayDF.setLineSpacing(1F, 1.5F)
        homeMF.setLineSpacing(1F, 1.5F)
        awayMF.setLineSpacing(1F, 1.5F)
        homeFW.setLineSpacing(1F, 1.5F)
        awayFW.setLineSpacing(1F, 1.5F)
        homeSub.setLineSpacing(1F, 1.5F)
        awaySub.setLineSpacing(1F, 1.5F)
    }

    override fun showTeamBadge(homeBadge: List<Team>, awayBadge: List<Team>) {
        val homeLogo = find<ImageView>(MatchDetailUI.teamHomeBadge)
        val awayLogo = find<ImageView>(MatchDetailUI.teamAwayBadge)

        if (homeBadge. isNotEmpty() && awayBadge.isNotEmpty()) {
            Glide.with(this).load(homeBadge[0].teamBadge).into(homeLogo)
            Glide.with(this).load(awayBadge[0].teamBadge).into(awayLogo)
        }
        else {
            snackbar(swipeRefresh, getString(R.string.request_fail))
        }
    }

    class MatchDetailUI : AnkoComponent<MatchDetailActivity>{
        companion object {
            val swipeRefresh = 1
            val progressBar = 2
            val matchDate = 3
            val teamHomeBadge = 4
            val teamAwayBadge = 5
            val teamHomeName = 6
            val teamAwayName = 7
            val teamHomeScore = 8
            val teamAwayScore = 9
            val teamHomeGoals = 10
            val teamAwayGoals = 11
            val teamHomeShots = 12
            val teamAwayShots = 13
            val teamHomeGK = 14
            val teamAwayGK = 15
            val teamHomeDF = 16
            val teamAwayDF = 17
            val teamHomeMF = 18
            val teamAwayMF = 19
            val teamHomeFW = 20
            val teamAwayFW = 21
            val teamHomeSub = 22
            val teamAwaySub = 23
        }

        override fun createView(ui: AnkoContext<MatchDetailActivity>) = with(ui) {
            linearLayout {
                lparams(width = matchParent, height = wrapContent)
                padding = dip(20)
                orientation = LinearLayout.VERTICAL
                backgroundColor = Color.WHITE

                swipeRefreshLayout {
                    id = swipeRefresh
                    setColorSchemeResources(R.color.colorAccent,
                            android.R.color.holo_green_light,
                            android.R.color.holo_orange_light,
                            android.R.color.holo_red_light)

                    scrollView {
                        isVerticalScrollBarEnabled = false
                        verticalLayout {
                            lparams(width = matchParent, height = wrapContent)

                            verticalLayout {
                                lparams(width = matchParent, height = matchParent)
                                padding = dip(10)
                                gravity = Gravity.CENTER_HORIZONTAL

                                textView {
                                    id = matchDate
                                    verticalPadding = dip(4)
                                    textSize = 18f
                                    typeface = Typeface.DEFAULT_BOLD
                                }.lparams(width = wrapContent, height = wrapContent) {
                                    gravity = Gravity.CENTER
                                }

                                linearLayout {
                                    orientation = LinearLayout.HORIZONTAL
                                    gravity = Gravity.CENTER
                                    lparams(width = matchParent, height = wrapContent) {
                                        topMargin = dip(4)
                                        bottomMargin = dip(12)
                                    }

                                    verticalLayout {
                                        gravity = Gravity.CENTER
                                        imageView {
                                            id = teamHomeBadge
                                            verticalPadding = dip(10)
                                        }.lparams(width = dip(125), height = dip(125))
                                        textView {
                                            id = teamHomeName
                                            textSize = 22f
                                            gravity = Gravity.CENTER
                                            textColor = R.color.colorPrimaryDark
                                            typeface = Typeface.DEFAULT_BOLD
                                            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE
                                        }.lparams(width = dip(125), height = wrapContent)
                                    }.lparams(width = matchParent, height = wrapContent) {
                                        weight = 1F
                                    }

                                    linearLayout {
                                        orientation = LinearLayout.HORIZONTAL
                                        gravity = Gravity.CENTER
                                        horizontalPadding = dip(16)
                                        textView {
                                            id = teamHomeScore
                                            textSize = 40f
                                            typeface = Typeface.DEFAULT_BOLD
                                            gravity = Gravity.CENTER
                                        }.lparams(width = wrapContent, height = wrapContent)
                                        textView {
                                            text = context.getString(R.string.strip)
                                            horizontalPadding = dip(3)
                                            textSize = 36f
                                            typeface = Typeface.DEFAULT_BOLD
                                            gravity = Gravity.CENTER
                                        }.lparams(width = wrapContent, height = wrapContent)
                                        textView {
                                            id = teamAwayScore
                                            textSize = 40f
                                            typeface = Typeface.DEFAULT_BOLD
                                            gravity = Gravity.CENTER
                                        }.lparams(width = wrapContent, height = wrapContent)
                                    }.lparams(width = wrapContent, height = matchParent){
                                        weight = 0F
                                    }

                                    verticalLayout {
                                        gravity = Gravity.CENTER
                                        imageView {
                                            id = teamAwayBadge
                                            verticalPadding = dip(10)
                                        }.lparams(width = dip(125), height = dip(125))
                                        textView {
                                            id = teamAwayName
                                            textSize = 24f
                                            gravity = Gravity.CENTER
                                            textColor = R.color.colorPrimaryDark
                                            typeface = Typeface.DEFAULT_BOLD
                                            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE
                                        }.lparams(width = dip(125), height = wrapContent)
                                    }.lparams(width = matchParent, height = wrapContent) {
                                        weight = 1F
                                    }

                                }

                                view {
                                    background = ContextCompat.getDrawable(ctx, R.color.colorSecondary)
                                }.lparams(width = matchParent, height = dip(2))

                                linearLayout {
                                    orientation = LinearLayout.HORIZONTAL
                                    gravity = Gravity.CENTER_HORIZONTAL or Gravity.TOP
                                    lparams(width = matchParent, height = wrapContent) {
                                        verticalPadding = dip(6)
                                    }
                                    textView {
                                        id = teamHomeGoals
                                        textSize = 16f
                                        typeface = Typeface.DEFAULT_BOLD
                                        gravity = Gravity.CENTER_HORIZONTAL or Gravity.START
                                    }.lparams(width = matchParent, height = wrapContent){
                                        weight = 1F
                                    }
                                    textView {
                                        text = context.getString(R.string.textGoals)
                                        horizontalPadding = dip(6)
                                        textSize = 16f
                                        typeface = Typeface.DEFAULT_BOLD
                                        gravity = Gravity.CENTER_HORIZONTAL or Gravity.TOP
                                    }.lparams(width = matchParent, height = wrapContent){
                                        weight = 1F
                                    }
                                    textView {
                                        id = teamAwayGoals
                                        textSize = 16f
                                        typeface = Typeface.DEFAULT_BOLD
                                        gravity = Gravity.CENTER_HORIZONTAL or Gravity.END
                                    }.lparams(width = matchParent, height = wrapContent){
                                        weight = 1F
                                    }
                                }

                                linearLayout {
                                    orientation = LinearLayout.HORIZONTAL
                                    gravity = Gravity.CENTER_HORIZONTAL or Gravity.TOP
                                    lparams(width = matchParent, height = wrapContent) {
                                        bottomPadding = dip(16)
                                    }
                                    textView {
                                        id = teamHomeShots
                                        textSize = 18f
                                        typeface = Typeface.DEFAULT_BOLD
                                        gravity = Gravity.CENTER_HORIZONTAL or Gravity.START
                                    }.lparams(width = matchParent, height = wrapContent){
                                        weight = 1F
                                    }
                                    textView {
                                        text = context.getString(R.string.textShots)
                                        horizontalPadding = dip(6)
                                        textSize = 16f
                                        typeface = Typeface.DEFAULT_BOLD
                                        gravity = Gravity.CENTER_HORIZONTAL or Gravity.TOP
                                    }.lparams(width = matchParent, height = wrapContent){
                                        weight = 1F
                                    }
                                    textView {
                                        id = teamAwayShots
                                        textSize = 18f
                                        typeface = Typeface.DEFAULT_BOLD
                                        gravity = Gravity.CENTER_HORIZONTAL or Gravity.END
                                    }.lparams(width = matchParent, height = wrapContent){
                                        weight = 1F
                                    }
                                }

                                view {
                                    background = ContextCompat.getDrawable(ctx, R.color.colorSecondary)
                                }.lparams(width = matchParent, height = dip(2))

                                textView {
                                    text = context.getString(R.string.lineups)
                                    verticalPadding = dip(8)
                                    textSize = 18f
                                    typeface = Typeface.DEFAULT_BOLD
                                    gravity = Gravity.CENTER
                                }.lparams(width = matchParent, height = wrapContent)

                                linearLayout {
                                    orientation = LinearLayout.HORIZONTAL
                                    gravity = Gravity.CENTER_HORIZONTAL or Gravity.TOP
                                    lparams(width = matchParent, height = wrapContent) {
                                        verticalPadding = dip(4)
                                    }
                                    textView {
                                        id = teamHomeGK
                                        textSize = 16f
                                        typeface = Typeface.DEFAULT_BOLD
                                        gravity = Gravity.CENTER_HORIZONTAL or Gravity.START
                                    }.lparams(width = matchParent, height = wrapContent){
                                        weight = 1F
                                    }
                                    textView {
                                        text = context.getString(R.string.textGK)
                                        horizontalPadding = dip(6)
                                        textSize = 16f
                                        typeface = Typeface.DEFAULT_BOLD
                                        gravity = Gravity.CENTER_HORIZONTAL or Gravity.TOP
                                    }.lparams(width = matchParent, height = wrapContent){
                                        weight = 1F
                                    }
                                    textView {
                                        id = teamAwayGK
                                        textSize = 16f
                                        typeface = Typeface.DEFAULT_BOLD
                                        gravity = Gravity.CENTER_HORIZONTAL or Gravity.END
                                    }.lparams(width = matchParent, height = wrapContent){
                                        weight = 1F
                                    }
                                }

                                linearLayout {
                                    orientation = LinearLayout.HORIZONTAL
                                    gravity = Gravity.CENTER_HORIZONTAL or Gravity.TOP
                                    lparams(width = matchParent, height = wrapContent) {
                                        verticalPadding = dip(4)
                                    }
                                    textView {
                                        id = teamHomeDF
                                        textSize = 16f
                                        typeface = Typeface.DEFAULT_BOLD
                                        gravity = Gravity.CENTER_HORIZONTAL or Gravity.START
                                    }.lparams(width = matchParent, height = wrapContent){
                                        weight = 1F
                                    }
                                    textView {
                                        text = context.getString(R.string.textDF)
                                        horizontalPadding = dip(6)
                                        textSize = 16f
                                        typeface = Typeface.DEFAULT_BOLD
                                        gravity = Gravity.CENTER_HORIZONTAL or Gravity.TOP
                                    }.lparams(width = matchParent, height = wrapContent){
                                        weight = 1F
                                    }
                                    textView {
                                        id = teamAwayDF
                                        textSize = 16f
                                        typeface = Typeface.DEFAULT_BOLD
                                        gravity = Gravity.CENTER_HORIZONTAL or Gravity.END
                                    }.lparams(width = matchParent, height = wrapContent){
                                        weight = 1F
                                    }
                                }

                                linearLayout {
                                    orientation = LinearLayout.HORIZONTAL
                                    gravity = Gravity.CENTER_HORIZONTAL or Gravity.TOP
                                    lparams(width = matchParent, height = wrapContent) {
                                        verticalPadding = dip(4)
                                    }
                                    textView {
                                        id = teamHomeMF
                                        textSize = 16f
                                        typeface = Typeface.DEFAULT_BOLD
                                        gravity = Gravity.CENTER_HORIZONTAL or Gravity.START
                                    }.lparams(width = matchParent, height = wrapContent){
                                        weight = 1F
                                    }
                                    textView {
                                        text = context.getString(R.string.textMF)
                                        horizontalPadding = dip(6)
                                        textSize = 16f
                                        typeface = Typeface.DEFAULT_BOLD
                                        gravity = Gravity.CENTER_HORIZONTAL or Gravity.TOP
                                    }.lparams(width = matchParent, height = wrapContent){
                                        weight = 1F
                                    }
                                    textView {
                                        id = teamAwayMF
                                        textSize = 16f
                                        typeface = Typeface.DEFAULT_BOLD
                                        gravity = Gravity.CENTER_HORIZONTAL or Gravity.END
                                    }.lparams(width = matchParent, height = wrapContent){
                                        weight = 1F
                                    }
                                }

                                linearLayout {
                                    orientation = LinearLayout.HORIZONTAL
                                    gravity = Gravity.CENTER_HORIZONTAL or Gravity.TOP
                                    lparams(width = matchParent, height = wrapContent) {
                                        verticalPadding = dip(4)
                                    }
                                    textView {
                                        id = teamHomeFW
                                        textSize = 16f
                                        typeface = Typeface.DEFAULT_BOLD
                                        gravity = Gravity.CENTER_HORIZONTAL or Gravity.START
                                    }.lparams(width = matchParent, height = wrapContent){
                                        weight = 1F
                                    }
                                    textView {
                                        text = context.getString(R.string.textFW)
                                        horizontalPadding = dip(6)
                                        textSize = 16f
                                        typeface = Typeface.DEFAULT_BOLD
                                        gravity = Gravity.CENTER_HORIZONTAL or Gravity.TOP
                                    }.lparams(width = matchParent, height = wrapContent){
                                        weight = 1F
                                    }
                                    textView {
                                        id = teamAwayFW
                                        textSize = 16f
                                        typeface = Typeface.DEFAULT_BOLD
                                        gravity = Gravity.CENTER_HORIZONTAL or Gravity.END
                                    }.lparams(width = matchParent, height = wrapContent){
                                        weight = 1F
                                    }
                                }

                                linearLayout {
                                    orientation = LinearLayout.HORIZONTAL
                                    gravity = Gravity.CENTER_HORIZONTAL or Gravity.TOP
                                    lparams(width = matchParent, height = wrapContent) {
                                        verticalPadding = dip(4)
                                    }
                                    textView {
                                        id = teamHomeSub
                                        textSize = 16f
                                        typeface = Typeface.DEFAULT_BOLD
                                        gravity = Gravity.CENTER_HORIZONTAL or Gravity.START
                                    }.lparams(width = matchParent, height = wrapContent){
                                        weight = 1F
                                    }
                                    textView {
                                        text = context.getString(R.string.textSubs)
                                        horizontalPadding = dip(6)
                                        textSize = 16f
                                        typeface = Typeface.DEFAULT_BOLD
                                        gravity = Gravity.CENTER_HORIZONTAL or Gravity.TOP
                                    }.lparams(width = matchParent, height = wrapContent){
                                        weight = 1F
                                    }
                                    textView {
                                        id = teamAwaySub
                                        textSize = 16f
                                        typeface = Typeface.DEFAULT_BOLD
                                        gravity = Gravity.CENTER_HORIZONTAL or Gravity.END
                                    }.lparams(width = matchParent, height = wrapContent){
                                        weight = 1F
                                    }
                                }
                            }

                            progressBar {
                                id = progressBar
                            }
                        }
                    }
                }
            }
        }
    }
}
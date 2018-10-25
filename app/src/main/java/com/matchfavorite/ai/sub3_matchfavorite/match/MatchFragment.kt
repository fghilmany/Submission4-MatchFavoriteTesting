package com.matchfavorite.ai.sub3_matchfavorite.match

import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.google.gson.Gson
import com.matchfavorite.ai.sub3_matchfavorite.R
import com.matchfavorite.ai.sub3_matchfavorite.api.ApiRepository
import com.matchfavorite.ai.sub3_matchfavorite.detail.MatchDetailActivity
import com.matchfavorite.ai.sub3_matchfavorite.model.Match
import com.matchfavorite.ai.sub3_matchfavorite.util.invisible
import com.matchfavorite.ai.sub3_matchfavorite.util.visible
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.support.v4.ctx
import org.jetbrains.anko.support.v4.onRefresh
import org.jetbrains.anko.support.v4.swipeRefreshLayout


class MatchFragment() : Fragment(), AnkoComponent<Context>, MatchView {

    private var matches: MutableList<Match> = mutableListOf()
    private var past: Boolean = true
    private lateinit var presenter: MatchPresenter
    private lateinit var adapterMatch: MatchAdapter
    private lateinit var listMatches: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var title: TextView

    companion object{
        val listMatch = 201
        val idSwipeRefresh = 202
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        past = this.arguments?.getBoolean("past") ?: true
        adapterMatch = MatchAdapter(matches) {
            ctx.startActivity<MatchDetailActivity>("id" to it.matchId)
        }
        listMatches.adapter = adapterMatch

        val request = ApiRepository()
        val gson = Gson()
        presenter = MatchPresenter(this, request, gson)
        presenter.getMatchesList(past)

        if (past) {
            title.setText(R.string.title_past)
        }
        else if (!past) {
            title.setText(R.string.title_next)
        }

        swipeRefresh.onRefresh {
            presenter.getMatchesList(past)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return createView(AnkoContext.create(ctx))
    }

    override fun createView(ui: AnkoContext<Context>): View = with(ui){
        linearLayout {
            lparams (width = matchParent, height = wrapContent)
            orientation = LinearLayout.VERTICAL
            topPadding = dip(12)
            leftPadding = dip(16)
            rightPadding = dip(16)
            title = textView{
                verticalPadding = dip(12)
                textSize = 18F
                typeface = Typeface.DEFAULT_BOLD
                gravity = Gravity.CENTER
            }.lparams(width = matchParent, height = wrapContent)

            swipeRefresh = swipeRefreshLayout {
                id = idSwipeRefresh
                setColorSchemeResources(R.color.colorAccent,
                        android.R.color.holo_green_light,
                        android.R.color.holo_orange_light,
                        android.R.color.holo_red_light)

                relativeLayout{
                    lparams (width = matchParent, height = wrapContent)
                    listMatches = recyclerView {
                        id = listMatch
                        lparams (width = matchParent, height = wrapContent)
                        layoutManager = LinearLayoutManager(ctx)
                    }

                    progressBar = progressBar {
                    }.lparams{
                        centerHorizontally()
                    }
                }
            }.lparams(width = matchParent, height = matchParent)
        }
    }

    override fun showLoading() {
        progressBar.visible()
    }

    override fun hideLoading() {
        progressBar.invisible()
    }

    override fun showMatchList(data: List<Match>) {
        swipeRefresh.isRefreshing = false
        matches.clear()
        matches.addAll(data)
        adapterMatch.notifyDataSetChanged()
    }

}

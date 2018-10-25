package com.matchfavorite.ai.sub3_matchfavorite.favorite

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
import com.matchfavorite.ai.sub3_matchfavorite.R
import com.matchfavorite.ai.sub3_matchfavorite.db.Favorite
import com.matchfavorite.ai.sub3_matchfavorite.db.database
import com.matchfavorite.ai.sub3_matchfavorite.detail.MatchDetailActivity
import com.matchfavorite.ai.sub3_matchfavorite.util.EspressoIdlingResource
import org.jetbrains.anko.*
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.select
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.support.v4.ctx
import org.jetbrains.anko.support.v4.onRefresh
import org.jetbrains.anko.support.v4.swipeRefreshLayout

class FavoriteMatchesFragment : Fragment(), AnkoComponent<Context> {
    private var favorites: MutableList<Favorite> = mutableListOf()
    private lateinit var adapterFavorite: FavoriteMatchesAdapter
    private lateinit var listEvent: RecyclerView
    private lateinit var swipeRefresh: SwipeRefreshLayout

    companion object{
        val listMatch = 201
        val idSwipeRefresh = 202
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        adapterFavorite = FavoriteMatchesAdapter(favorites){
            ctx.startActivity<MatchDetailActivity>("id" to it.matchId)
        }

        EspressoIdlingResource.increment()
        listEvent.adapter = adapterFavorite
        showFavorite()
        EspressoIdlingResource.decrement()
        swipeRefresh.onRefresh {
            favorites.clear()
            showFavorite()
        }
    }

    private fun showFavorite(){
        context?.database?.use {
            swipeRefresh.isRefreshing = false
            val result = select(Favorite.TABLE_FAVORITE)
            val favorite = result.parseList(classParser<Favorite>())
            favorites.addAll(favorite)
            adapterFavorite.notifyDataSetChanged()
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

            textView{
                verticalPadding = dip(12)
                textSize = 18F
                text = context.getString(R.string.titleFavorite)
                typeface = Typeface.DEFAULT_BOLD
                gravity = Gravity.CENTER
            }.lparams(width = matchParent, height = wrapContent)

            swipeRefresh = swipeRefreshLayout {
                id = idSwipeRefresh
                setColorSchemeResources(R.color.colorAccent,
                        android.R.color.holo_green_light,
                        android.R.color.holo_orange_light,
                        android.R.color.holo_red_light)

                listEvent = recyclerView {
                    id = listMatch
                    lparams (width = matchParent, height = wrapContent)
                    layoutManager = LinearLayoutManager(ctx)
                }
            }
        }
    }
}

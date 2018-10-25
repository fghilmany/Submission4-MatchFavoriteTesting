package com.matchfavorite.ai.sub3_matchfavorite.home

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.widget.LinearLayout
import com.matchfavorite.ai.sub3_matchfavorite.R
import com.matchfavorite.ai.sub3_matchfavorite.favorite.FavoriteMatchesFragment
import com.matchfavorite.ai.sub3_matchfavorite.match.MatchFragment
import org.jetbrains.anko.*
import org.jetbrains.anko.design.bottomNavigationView

class MatchListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MatchListActivityUI().setContentView(this)
        val navBar = find<BottomNavigationView>(MatchListActivityUI.navigationBar)
        navBar.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_past -> {
                    loadMatchFragment(savedInstanceState)
                }
                R.id.navigation_next -> {
                    loadMatchFragment(savedInstanceState, false)
                }
                R.id.favorites -> {
                    loadFavoriteFragment(savedInstanceState)
                }
            }
            true
        }
        navBar.selectedItemId = R.id.navigation_past
    }

    private fun loadMatchFragment(savedInstanceState: Bundle?, past: Boolean = true){
        if (savedInstanceState == null){
            val bundle = Bundle()
            bundle.putBoolean("past", past)
            val fragMatch = MatchFragment()
            fragMatch.setArguments(bundle)
            supportFragmentManager
                    .beginTransaction()
                    .replace(MatchListActivityUI.container, fragMatch, MatchFragment::class.java.simpleName)
                    .commit()
        }
    }

    private fun loadFavoriteFragment(savedInstanceState: Bundle?){
        if (savedInstanceState == null){
            supportFragmentManager
                    .beginTransaction()
                    .replace(MatchListActivityUI.container, FavoriteMatchesFragment(), FavoriteMatchesFragment::class.java.simpleName)
                    .commit()
        }
    }

}

class MatchListActivityUI() : AnkoComponent<MatchListActivity> {
    companion object {
        val container = 101
        val navigation = 102
        val navigationBar = 103
    }

    override fun createView(ui: AnkoContext<MatchListActivity>) = with(ui) {
        relativeLayout {
            lparams(width=matchParent, height= wrapContent){
                margin = dip(8)
            }

            frameLayout {
                id = container
                leftPadding = dip(10)
                rightPadding = dip(10)
            }.lparams(width = matchParent, height = matchParent) {
                above(navigation)
            }

            linearLayout {
                id = navigation
                orientation = LinearLayout.VERTICAL

                view {
                    background = ContextCompat.getDrawable(ctx, R.drawable.shadow)
                }.lparams(width = matchParent, height = dip(4))

                bottomNavigationView {
                    id = navigationBar
                    backgroundColorResource = R.color.colorWhite
                    itemBackgroundResource = R.color.colorWhite
                    inflateMenu(R.menu.navigation)
                }.lparams(width = matchParent, height = wrapContent)
            }.lparams(width=matchParent, height= wrapContent){
                alignParentBottom()
            }
        }
    }
}



package com.matchfavorite.ai.sub3_matchfavorite.home

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.Espresso.pressBack
import android.support.test.espresso.IdlingRegistry
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.action.ViewActions.swipeDown
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.support.v7.widget.RecyclerView
import com.matchfavorite.ai.sub3_matchfavorite.R.id.*
import com.matchfavorite.ai.sub3_matchfavorite.detail.MatchDetailActivity.MatchDetailUI.Companion.matchDate
import com.matchfavorite.ai.sub3_matchfavorite.detail.MatchDetailActivity.MatchDetailUI.Companion.swipeRefresh
import com.matchfavorite.ai.sub3_matchfavorite.detail.MatchDetailActivity.MatchDetailUI.Companion.teamAwayBadge
import com.matchfavorite.ai.sub3_matchfavorite.detail.MatchDetailActivity.MatchDetailUI.Companion.teamHomeBadge
import com.matchfavorite.ai.sub3_matchfavorite.home.MatchListActivityUI.Companion.navigationBar
import com.matchfavorite.ai.sub3_matchfavorite.match.MatchFragment.Companion.idSwipeRefresh
import com.matchfavorite.ai.sub3_matchfavorite.match.MatchFragment.Companion.listMatch
import com.matchfavorite.ai.sub3_matchfavorite.util.EspressoIdlingResource
import org.junit.Before

import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MatchListActivityUITest {
    @Rule
    @JvmField var activityRule = ActivityTestRule(MatchListActivity::class.java)

    @Before
    fun setUp() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.getIdlingResource())
    }

    @Test
    fun testAppBehaviour() {
        //cek list lalu klik next
        onView(withId(navigationBar))
                .check(matches(isDisplayed()))
        onView(withId(navigation_next)).perform(click())

        //cek list lalu scroll ke pos 13 dan klik. lalu back
        onView(withId(listMatch))
                .check(matches(isDisplayed()))
        onView(withId(listMatch)).perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(13))
        onView(withId(listMatch)).perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(13, click()))

        onView(withId(add_to_favorite))
                .check(matches(isDisplayed()))
        onView(withId(add_to_favorite)).perform(click())
        onView(withText("Ditambahkan ke Daftar Favorit"))
                .check(matches(isDisplayed()))

        pressBack()

        //cek swipe refresh function
        onView(withId(listMatch)).perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(0))
        onView(withId(idSwipeRefresh)).perform(swipeDown())

        //klik past
        onView(withId(navigationBar))
                .check(matches(isDisplayed()))
        onView(withId(navigation_past)).perform(click())

        //cek list lalu scroll ke pos 13 lalu klik. kemudian back
        onView(withId(listMatch))
                .check(matches(isDisplayed()))
        onView(withId(listMatch)).perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(13))
        onView(withId(listMatch)).perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(13, click()))

        onView(withId(add_to_favorite))
                .check(matches(isDisplayed()))
        onView(withId(add_to_favorite)).perform(click())
        onView(withText("Ditambahkan ke Daftar Favorit"))
                .check(matches(isDisplayed()))

        pressBack()

        //cek swipe refreshnya
        onView(withId(listMatch)).perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(0))
        onView(withId(idSwipeRefresh)).perform(swipeDown())

        //go to favorites
        onView(withId(navigationBar))
                .check(matches(isDisplayed()))
        onView(withId(favorites)).perform(click())

        //klik yang pertama lalu hapus
        onView(withId(listMatch))
                .check(matches(isDisplayed()))
        onView(withId(listMatch)).perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))
        onView(withId(add_to_favorite))
                .check(matches(isDisplayed()))

        onView(withId(add_to_favorite)).perform(click())
        onView(withText("Dihapus dari Daftar Favorit"))
                .check(matches(isDisplayed()))
        pressBack()

        //klik yang kedua lalu hapus
        onView(withId(listMatch))
                .check(matches(isDisplayed()))
        onView(withId(listMatch)).perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(1, click()))
        onView(withId(add_to_favorite))
                .check(matches(isDisplayed()))

        onView(withId(add_to_favorite)).perform(click())
        onView(withText("Dihapus dari Daftar Favorit"))
                .check(matches(isDisplayed()))
        pressBack()

        //swipe to refresh
        onView(withId(listMatch)).perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(0))
        onView(withId(idSwipeRefresh)).perform(swipeDown())
    }


}


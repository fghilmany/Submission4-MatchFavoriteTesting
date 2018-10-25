package com.matchfavorite.ai.sub3_matchfavorite.util

import android.support.test.espresso.IdlingResource
import java.util.concurrent.atomic.AtomicInteger

object EspressoIdlingResource {

    private val RESOURCE = "GLOBAL"
    private val counter = AtomicInteger(0)

    // written from main thread, read from any thread.
    private val resourceCallback: IdlingResource.ResourceCallback? = null

    private val mCountingIdlingResource = SimpleIdlingResource(RESOURCE)

    fun getIdlingResource(): IdlingResource {
        return mCountingIdlingResource
    }

    fun increment() {
        mCountingIdlingResource.increment()

    }

    fun decrement() {
        mCountingIdlingResource.decrement()
    }
}
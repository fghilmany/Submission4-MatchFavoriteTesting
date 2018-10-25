package com.matchfavorite.ai.sub3_matchfavorite

import com.matchfavorite.ai.sub3_matchfavorite.util.CoroutineContextProvider
import kotlinx.coroutines.experimental.Unconfined
import kotlin.coroutines.experimental.CoroutineContext

class TestContextProvider : CoroutineContextProvider() {
    override val main: CoroutineContext = Unconfined
}
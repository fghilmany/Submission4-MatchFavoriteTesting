package com.matchfavorite.ai.sub3_matchfavorite.api

import java.net.URL

class ApiRepository {

    fun doRequest(url: String): String {
        return URL(url).readText()
    }

}
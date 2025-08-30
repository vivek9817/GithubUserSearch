package com.example.githubusersearch.data.apiEndPoints

import com.example.githubusersearch.BuildConfig


class ApiEndPoints {
    companion object {
        const val BASE_URL = "https://api.github.com/"

        val TOKEN = BuildConfig.GITHUB_ACCESS_TOKEN

    }
}
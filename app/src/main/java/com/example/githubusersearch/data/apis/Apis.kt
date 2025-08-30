package com.example.githubusersearch.data.apis

import com.example.githubusersearch.data.models.RepoDetails
import com.example.githubusersearch.data.models.UserResponses
import com.example.githubusersearch.data.models.UsersDetails
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Apis {
    @GET("search/users")
    suspend fun getUsers(@Query("q") username: String): UserResponses?

    @GET("users/{username}")
    suspend fun getUserProfile(@Path("username") username: String): UsersDetails?

    @GET("users/{username}/repos")
    suspend fun getUserRepos(
        @Path("username") username: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int = 30
    ): List<RepoDetails>
}
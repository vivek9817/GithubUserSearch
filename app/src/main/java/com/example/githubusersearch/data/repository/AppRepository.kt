package com.example.githubusersearch.data.repository

import com.example.githubusersearch.data.apis.Apis
import com.example.githubusersearch.data.apis.RetrofitInstance
import jakarta.inject.Inject
import jakarta.inject.Singleton

@Singleton
class AppRepository @Inject constructor() {

    private val commonApiServices: Apis? by lazy { RetrofitInstance.commonApiServices }

    suspend fun getUsers(endPoint: String) = commonApiServices?.getUsers(endPoint)

    suspend fun getUserProfile(endPoint: String) = commonApiServices?.getUserProfile(endPoint)

    suspend fun getUserRepo(endPoint: String, page: Int = 1, perPage: Int = 30) =
        commonApiServices?.getUserRepos(endPoint, page, perPage)
}
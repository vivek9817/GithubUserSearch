package com.example.githubusersearch.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserResponses(
    var total_count: Int? = 0,
    val incomplete_results: Boolean? = false,
    val items: ArrayList<UsersDetails>? = null
) : Parcelable

@Parcelize
data class UsersDetails(
    val login: String? = null,
    val valid: Int? = 0,
    val node_id: String? = null,
    val avatar_url: String? = null,
    val gravatar_id: String? = null,
    val url: String? = null,
    val html_url: String? = null,
    val followers_url: String? = null,
    val following_url: String? = null,
    val gists_url: String? = null,
    val starred_url: String? = null,
    val subscriptions_url: String? = null,
    val organizations_url: String? = null,
    val repos_url: String? = null,
    val events_url: String? = null,
    val received_events_url: String? = null,
    val type: String? = null,
    val user_view_type: String? = null,
    val site_admin: Boolean? = false,
    var name: String? = null,
    var company: String? = null,
    var blog: String? = null,
    var location: String? = null,
    var email: String? = null,
    var hireable: String? = null,
    var bio: String? = null,
    var twitter_username: String? = null,
    var public_repos: Int? = 0,
    var public_gists: Int? = 0,
    var followers: Int? = 0,
    var following: Int? = 0,
    var created_at: String? = null,
    var updated_at: String? = null
) : Parcelable
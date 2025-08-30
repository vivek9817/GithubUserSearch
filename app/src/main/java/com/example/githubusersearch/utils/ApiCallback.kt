package com.example.githubusersearch.utils

data class ApiCallback<out T>(
    val status: CommonUtils.ApiStatus,
    val data: T?,
    val message: String?
) {
    companion object {
        fun <T> onLoading(data: T): ApiCallback<T> =
            ApiCallback<T>(status = CommonUtils.ApiStatus.LOADING, data = data, message = null)

        fun <T> onSuccess(data: T): ApiCallback<T> =
            ApiCallback<T>(status = CommonUtils.ApiStatus.SUCCESS, data = data, message = null)

        fun <T> onFailure(data: T?, message: String? = "Something went wrong"): ApiCallback<T> =
            ApiCallback<T>(status = CommonUtils.ApiStatus.FAILURE, data = data, message = message)
    }
}

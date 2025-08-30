package com.example.githubusersearch.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.githubusersearch.data.models.RepoDetails
import com.example.githubusersearch.data.repository.AppRepository
import com.example.githubusersearch.utils.ApiCallback
import com.example.githubusersearch.utils.NetworkUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserRepoViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val _userRepoDetails = MutableLiveData<ApiCallback<List<RepoDetails>>>()
    val userRepoDetails: LiveData<ApiCallback<List<RepoDetails>>> = _userRepoDetails

    private val _isLoadingMore = MutableLiveData(false)
    val isLoadingMore: LiveData<Boolean> = _isLoadingMore

    private var currentPage = 1
    private var currentQuery: String = ""
    private var isLoading = false
    private var hasMoreData = true

    fun getUserRepoDetails(userName: String?, loadMore: Boolean = false) {
        if (!NetworkUtils.isNetworkAvailable(getApplication())) {
            _userRepoDetails.postValue(ApiCallback.onFailure(null, "No internet connection"))
            return
        }

        if (isLoading || (!hasMoreData && loadMore)) return
        isLoading = true

        if (!loadMore) {
            currentPage = 1
            currentQuery = userName ?: ""
            _isLoadingMore.postValue(false)
            hasMoreData = true
        } else {
            _isLoadingMore.postValue(true)
        }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = AppRepository().getUserRepo(currentQuery, currentPage)
                val newRepos = response ?: emptyList()

                if (newRepos.isEmpty()) hasMoreData = false

                val currentList =
                    if (loadMore) _userRepoDetails.value?.data ?: emptyList() else emptyList()
                val combined = currentList + newRepos

                _userRepoDetails.postValue(ApiCallback.onSuccess(combined))
                currentPage++

            } catch (err: Throwable) {
                _userRepoDetails.postValue(ApiCallback.onFailure(null, err.localizedMessage))
            } finally {
                isLoading = false
                _isLoadingMore.postValue(false)
            }
        }
    }
}
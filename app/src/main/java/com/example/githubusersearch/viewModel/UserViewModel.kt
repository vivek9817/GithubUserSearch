package com.example.githubusersearch.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.githubusersearch.data.repository.AppRepository
import com.example.githubusersearch.utils.ApiCallback
import com.example.githubusersearch.utils.NetworkUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application) {

    private val _userDetails = MutableLiveData<ApiCallback<Any>>()
    val userDetails: LiveData<ApiCallback<Any>> = _userDetails

    private val _userProfileData = MutableLiveData<ApiCallback<Any>>()
    val userProfileData: LiveData<ApiCallback<Any>> = _userProfileData

    /*Search By UserName*/
    fun getUserDetails(userName: String?) {
        // check network first
        if (!NetworkUtils.isNetworkAvailable(getApplication())) {
            _userDetails.postValue(
                ApiCallback.onFailure(
                    data = null,
                    message = "No internet connection"
                )
            )
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            _userDetails.postValue(ApiCallback.onLoading(data = ""))
            try {
                val response = AppRepository().getUsers(userName ?: "")
                _userDetails.postValue(ApiCallback.onSuccess(response) as ApiCallback<Any>)
            } catch (err: Throwable) {
                _userDetails.postValue(ApiCallback.onFailure(data = null, err.localizedMessage))
            }
        }
    }

    /*Get User Profile Data*/
    fun getUserProfileData(userName: String?) {
        // check network first
        if (!NetworkUtils.isNetworkAvailable(getApplication())) {
            _userProfileData.postValue(
                ApiCallback.onFailure(
                    data = null,
                    message = "No internet connection"
                )
            )
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            _userProfileData.postValue(ApiCallback.onLoading(data = ""))
            try {
                val response = AppRepository().getUserProfile(userName ?: "")
                _userProfileData.postValue(ApiCallback.onSuccess(response) as ApiCallback<Any>)
            } catch (err: Throwable) {
                _userProfileData.postValue(ApiCallback.onFailure(data = null, err.localizedMessage))
            }
        }
    }

}
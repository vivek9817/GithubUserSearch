package com.example.githubusersearch.utils

import android.app.Activity
import android.view.inputmethod.InputMethodManager

object CommonUtils {

    /**
     * dismisses soft keyboard
     * @param mActivity the activity on which the keyboard needs to be dismissed
     */
    fun dismissKeyboard(mActivity: Activity) {
        val inputMethodManager: InputMethodManager =
            mActivity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(mActivity.currentFocus?.windowToken, 0)
    }

    /*Check The Status while calling the server*/
    enum class ApiStatus {
        LOADING, SUCCESS, FAILURE
    }
}
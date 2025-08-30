package com.example.githubusersearch.screens

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.githubusersearch.Component.EmptyStateView
import com.example.githubusersearch.Component.FullScreenLoading
import com.example.githubusersearch.Component.SearchBarComponent
import com.example.githubusersearch.Component.TextComponent
import com.example.githubusersearch.Component.UserItem
import com.example.githubusersearch.R
import com.example.githubusersearch.data.models.UserResponses
import com.example.githubusersearch.data.models.UsersDetails
import com.example.githubusersearch.utils.CommonUtils
import com.example.githubusersearch.utils.CommonUtils.dismissKeyboard
import com.example.githubusersearch.viewModel.UserViewModel
import java.util.Date

@Composable
fun SearchComponent(navController: NavController, commonViewModel: UserViewModel = viewModel()) {
    val context = LocalContext.current
    val result = commonViewModel.userDetails.observeAsState()

    var searchQuery by rememberSaveable { mutableStateOf("") }

    // Get navigation bar height dynamically
    val view = LocalView.current
    val navBarHeightDp = with(LocalDensity.current) {
        ViewCompat.getRootWindowInsets(view)
            ?.getInsets(WindowInsetsCompat.Type.navigationBars())?.bottom?.toDp() ?: 0.dp
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(10.dp),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 30.dp, bottom = navBarHeightDp)
        ) {
            TextComponent(value = stringResource(id = R.string.search_by_user))

            SearchBarComponent(
                hint = stringResource(id = R.string.search_by_user_name),
                value = searchQuery,
                onValueChange = { searchQuery = it },
                onSearchClicked = {
                    dismissKeyboard(context as Activity)
                    if (searchQuery.isBlank()) {
                        Toast.makeText(
                            context,
                            "Username required.",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@SearchBarComponent
                    }
                    // Save query to SavedStateHandle so it persists
                    navController.currentBackStackEntry?.savedStateHandle?.set(
                        "searchQuery",
                        searchQuery
                    )
                    commonViewModel.getUserDetails(searchQuery)
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            when (result.value?.status) {
                CommonUtils.ApiStatus.LOADING ->
                    FullScreenLoading()

                CommonUtils.ApiStatus.SUCCESS -> {
                    GetViewOfAllMatchedUsers(result.value?.data) {
                        navController.currentBackStackEntry?.savedStateHandle?.set(
                            "userDetails", it
                        )
                        navController.navigate("profile")
                    }
                }

                CommonUtils.ApiStatus.FAILURE -> {
                    EmptyStateView(
                        "${result.value?.message}",
                        modifier = Modifier.padding(16.dp)
                    )
                }

                else -> {}
            }
        }
    }

}

@Composable
fun GetViewOfAllMatchedUsers(result: Any?, onClick: (UsersDetails?) -> Unit) {
    when (result) {
        is UserResponses -> {
            val users = result.items
            if (users.isNullOrEmpty()) {
                EmptyStateView("No users found", modifier = Modifier.padding(16.dp))
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(users) { user ->
                        UserItem(
                            username = user.login ?: "--",
                            avatarUrl = user.avatar_url ?: ""
                        ) {
                            onClick(user)
                        }
                    }
                }
            }
        }

        else -> {
            // Handle unknown type
            EmptyStateView("Unexpected data type", modifier = Modifier.padding(16.dp))
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreviewOfSearchComponent() {
    val navController = rememberNavController()
    SearchComponent(navController)
}
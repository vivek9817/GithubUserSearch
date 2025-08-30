package com.example.githubusersearch.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.githubusersearch.Component.EmptyStateView
import com.example.githubusersearch.Component.FollowersSection
import com.example.githubusersearch.Component.FullScreenLoading
import com.example.githubusersearch.Component.ProfileAvatar
import com.example.githubusersearch.Component.RepoItem
import com.example.githubusersearch.Component.RepoListContainer
import com.example.githubusersearch.Component.TextComponent
import com.example.githubusersearch.data.models.RepoDetails
import com.example.githubusersearch.data.models.UsersDetails
import com.example.githubusersearch.utils.CommonUtils
import com.example.githubusersearch.viewModel.UserRepoViewModel
import com.example.githubusersearch.viewModel.UserViewModel
import java.util.Locale

@Composable
fun ProfileComponent(
    navController: NavController,
    userData: UsersDetails?,
    commonViewModel: UserViewModel = viewModel(),
    userRepoViewModel: UserRepoViewModel = viewModel()
) {
    val context = LocalContext.current
    LaunchedEffect(userData?.login) {
        userData?.login?.let {
            commonViewModel.getUserProfileData(it)
            userRepoViewModel.getUserRepoDetails(it)
        }
    }

    val result = commonViewModel.userProfileData.observeAsState()
    val repoResult = userRepoViewModel.userRepoDetails.observeAsState()
    val isLoadingMore = userRepoViewModel.isLoadingMore.observeAsState(false)

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
                .padding(top = 30.dp, bottom = navBarHeightDp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            /*User Profile Details*/
            when (result.value?.status) {
                CommonUtils.ApiStatus.LOADING ->
                    FullScreenLoading()

                CommonUtils.ApiStatus.SUCCESS -> {
                    GetUserProfileDetails(result.value?.data) { userProfileData ->
                        ViewAllComposableFunctionOfAUsers(userProfileData)
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

            /*User Repo Details*/
            when (repoResult.value?.status) {
                CommonUtils.ApiStatus.LOADING -> {}

                CommonUtils.ApiStatus.SUCCESS -> {
                    GetUserRepoDetails(
                        repoResult.value?.data,
                        onLoadMore = {
                            userRepoViewModel.getUserRepoDetails(
                                userData?.login,
                                loadMore = true
                            )
                        },
                        isLoadingMore = isLoadingMore.value
                    )
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
fun GetUserProfileDetails(result: Any?, onDone: @Composable (UsersDetails) -> Unit) {
    when (result) {
        is UsersDetails -> {
            onDone(result)
        }

        else -> {
            EmptyStateView(
                "Unexpected data type",
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
fun GetUserRepoDetails(
    result: Any?,
    onLoadMore: () -> Unit = {},
    isLoadingMore: Boolean = false
) {
    val usersRepo = (result as? List<*>)?.filterIsInstance<RepoDetails>() ?: emptyList()

    if (usersRepo.isEmpty() && !isLoadingMore) {
        EmptyStateView("No repositories found", modifier = Modifier.padding(16.dp))
    } else {
        val listState = rememberLazyListState()

        // Trigger load more when user scrolls near the bottom
        LaunchedEffect(listState, usersRepo.size, isLoadingMore) {
            snapshotFlow { listState.firstVisibleItemIndex to listState.layoutInfo.totalItemsCount }
                .collect { (firstVisible, totalItems) ->
                    val lastVisibleIndex = firstVisible + listState.layoutInfo.visibleItemsInfo.size
                    if (lastVisibleIndex >= totalItems && !isLoadingMore && usersRepo.isNotEmpty()) {
                        onLoadMore()
                    }
                }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(usersRepo) { repo ->
                RepoItem(
                    name = repo.name ?: "--",
                    description = repo.description ?: "--",
                    stars = repo.stargazers_count ?: 0,
                    forks = repo.forks_count ?: 0
                )
            }

            //Loader item at the bottom
            if (isLoadingMore) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }

    }
}

@Composable
fun ViewAllComposableFunctionOfAUsers(userData: UsersDetails?) {
    /*Profile Image*/
    ProfileAvatar(userData?.avatar_url)

    Spacer(modifier = Modifier.height(8.dp))

    /*User Name*/
    TextComponent(userData?.login?.capitalize(Locale.ROOT) ?: "--")

    // Bio
    userData?.bio?.let {
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = it,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }

    Spacer(modifier = Modifier.height(12.dp))

    /* Followers / Repos */
    FollowersSection(
        followers = userData?.followers ?: 0,
        repos = userData?.public_repos ?: 0
    )

    Spacer(modifier = Modifier.height(12.dp))

    /*Repo List*/
    RepoListContainer(title = "Repository Details")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreviewOfProfileComponent() {
    val navController = rememberNavController()
    ProfileComponent(navController, userData = null)
}
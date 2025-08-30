package com.example.githubusersearch.app

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.githubusersearch.data.models.UsersDetails
import com.example.githubusersearch.screens.ProfileComponent
import com.example.githubusersearch.screens.SearchComponent

@Composable
fun GitHubUserSearch() {
    val navController = rememberNavController()

    Surface(Modifier.fillMaxSize(), color = Color.White) {
        NavHost(navController = navController, startDestination = "search") {

            /*Search Screen*/
            composable("search") {
                SearchComponent(navController)
            }

            /*Profile Screen*/
            composable("profile") {
                val userDetails = remember {
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.get<UsersDetails>("userDetails")
                }

                ProfileComponent(navController, userDetails)
            }

        }
    }

}
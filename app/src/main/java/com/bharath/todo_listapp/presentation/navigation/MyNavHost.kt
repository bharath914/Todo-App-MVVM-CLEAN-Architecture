package com.bharath.todo_listapp.presentation.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.spring
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.bharath.todo_listapp.presentation.homescreen.HomeScreen
import com.bharath.todo_listapp.presentation.newtodoscreen.CreateTodoScreen


@Composable
fun MyNavHost(
    navHostController: NavHostController,
    startDestination: String,
    paddingValues: PaddingValues,
) {
    NavHost(navController = navHostController, startDestination = startDestination, builder = {

        composable(NavConst.homeScreen) {
            HomeScreen(navHostController, paddingValues)
        }
        composable(NavConst.editScreen + "/{Id}", enterTransition = {
            slideIntoContainer(towards = AnimatedContentTransitionScope.SlideDirection.Up)
        }, popExitTransition = {
            slideOutOfContainer(towards = AnimatedContentTransitionScope.SlideDirection.Down)
        }
            ) {
            CreateTodoScreen(navHostController = navHostController, padd = paddingValues)
        }

        composable(NavConst.editScreen, enterTransition = {
            slideIntoContainer(towards = AnimatedContentTransitionScope.SlideDirection.Up)
        }, popExitTransition = {
            slideOutOfContainer(towards = AnimatedContentTransitionScope.SlideDirection.Down)
        }) {
            CreateTodoScreen(navHostController = navHostController, padd = paddingValues)
        }

    })

}


object NavConst {
    const val homeScreen = "HomeScreen"
    const val editScreen = "EditScreen"
}
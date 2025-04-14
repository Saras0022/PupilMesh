package com.saras.pupilmesh.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.saras.pupilmesh.ui.screen.detail.DetailScreen
import com.saras.pupilmesh.ui.screen.facedetection.FaceDetectionScreen
import com.saras.pupilmesh.ui.screen.home.HomeScreen
import com.saras.pupilmesh.ui.screen.login.LoginScreen
import com.saras.pupilmesh.utils.PreferenceManager

@Composable
fun AppNavGraph(modifier: Modifier = Modifier, navController: NavHostController) {

    val context = LocalContext.current

    NavHost(
        navController = navController,
        startDestination = if (PreferenceManager.isUserSignedIn(context)) Screen.HOME.route else Screen.LOGIN.route,
        modifier = modifier
    ) {
        composable(Screen.LOGIN.route) {
            LoginScreen {
                PreferenceManager.setUserSignedIn(context, true)
                navController.navigate(Screen.HOME.route)
            }
        }
        composable(Screen.HOME.route) {
            HomeScreen { mangaId ->
                navController.navigate(Screen.DETAIL.withArgs(mangaId))
            }
        }
        composable(Screen.DETAIL.route) { backStackEntry ->
            val mangaId = backStackEntry.arguments?.getString("mangaId") ?: ""
            DetailScreen(mangaId = mangaId)
        }
        composable(Screen.FACEDETECTION.route) {
            FaceDetectionScreen()
        }
    }

}
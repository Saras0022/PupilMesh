package com.saras.pupilmesh.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.saras.pupilmesh.navigation.Screen

@Composable
fun BottomBar(navController: NavHostController) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()

    if (navBackStackEntry?.destination?.route != Screen.LOGIN.route) {
        BottomAppBar {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Button({
                        if (navBackStackEntry?.destination?.route != Screen.HOME.route) {
                            navController.navigate(Screen.HOME.route) { popUpTo(0) }
                        }
                    }) {
                        Icon(Icons.AutoMirrored.Filled.MenuBook, "")
                    }
                    Text("Manga")
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Button({

                        if (navBackStackEntry?.destination?.route != Screen.FACEDETECTION.route) {
                            navController.navigate(Screen.FACEDETECTION.route) { popUpTo(0) }
                        }
                    }) {
                        Icon(Icons.Default.Face, "")
                    }
                    Text("Face")
                }
            }
        }
    }
}
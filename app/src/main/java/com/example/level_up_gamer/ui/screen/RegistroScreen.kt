package com.example.level_up_gamer.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.level_up_gamer.ui.components.RegistroContent
import com.example.level_up_gamer.viewmodel.LoginViewModel

@Composable
fun RegistroScreen(navController: NavController) {
    //val viewModel: LoginViewModel = viewModel()

    //val errorApi by viewModel.error.observeAsState()

    RegistroContent(
        navController = navController
    )

}
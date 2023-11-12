package com.example.minibus.screens

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.minibus.dataStoreManager.DataStoreManager
import com.example.minibus.ui.theme.MinibusTheme
import com.example.minibus.vm.UserViewModel
import com.example.minibus.vm.UserViewModelFactory

@Composable
fun RegistrationScreen(userViewModel: UserViewModel) {

}

@Composable
@Preview
fun RegistrationScreenDarkPreview() {
    MinibusTheme(useDarkTheme = true) {
        val dataStoreManager = DataStoreManager(LocalContext.current)
        val userViewModel: UserViewModel = viewModel(factory = UserViewModelFactory(dataStoreManager))
        var name by remember {
            mutableStateOf("йоу")
        }
        LaunchedEffect(key1 = true){
            dataStoreManager.getUserData().collect{ user->
               // name = user.firstName
            }
        }
        //val user = User("+375333733413","Алтsefай","Хорош")

        //userViewModel.saveUser(user)


        Log.d("RegistrationScreenDarkPreview", name)
        RegistrationScreen(userViewModel)
    }
}


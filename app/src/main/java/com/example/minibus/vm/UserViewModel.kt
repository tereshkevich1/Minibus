package com.example.minibus.vm

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.minibus.dataStoreManager.DataStoreManager
import com.example.minibus.models.User
import kotlinx.coroutines.launch

class UserViewModel(private val dataStoreManager: DataStoreManager) : ViewModel() {

    val userFlow = dataStoreManager.getUserData()
    var firstName by mutableStateOf("")
    var lastName by mutableStateOf("")
    var phone by mutableStateOf("")

    init {
        getUserData()
    }
    private fun getUserData(){
        viewModelScope.launch {
            dataStoreManager.getUserData().collect{ user->
                firstName = user.firstName
                lastName = user.lastName
                phone = user.phoneNumber
            }
        }
    }

    fun updateFirstName(input:String){
        firstName = input
    }
    fun updateLastName(input:String){
        lastName = input
    }
    fun updatePhone(input:String){
        phone = input
    }

    fun saveUserName(user: User){
        viewModelScope.launch {
            dataStoreManager.saveUserData(user)
        }
    }
}

class UserViewModelFactory(private val dataStoreManager: DataStoreManager) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        UserViewModel(dataStoreManager) as T

}
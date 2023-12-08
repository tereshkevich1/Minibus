package com.example.minibus.vm

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.minibus.R
import com.example.minibus.dataStoreManager.DataStoreManager
import com.example.minibus.models.User
import com.example.minibus.network.MinibusApi
import com.example.minibus.state_models.ButtonUiState
import com.example.minibus.state_models.UserUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserViewModel(private val dataStoreManager: DataStoreManager) : ViewModel() {

    private val _userUiState = MutableStateFlow(UserUiState())
    val userUiState = _userUiState.asStateFlow()

    private var fieldIsUpdated = false
    var showNotification by mutableStateOf(false)
    var errorPhone by mutableIntStateOf(0)
    var errorFirstName by mutableIntStateOf(0)
    var errorLastName by mutableIntStateOf(0)


    var errorPassword by mutableIntStateOf(0)
    var errorConfirmationPassword by mutableIntStateOf(0)
    var buttonState: ButtonUiState by mutableStateOf(ButtonUiState.Defolt)

    init {
        getUserData()
    }

    fun getUserData() {
        viewModelScope.launch {
            dataStoreManager.getUserData().collect { user ->
                _userUiState.update { current ->
                    current.copy(
                        userId = user.id,
                        firstName = user.firstName,
                        lastName = user.lastName,
                        phone = if (user.phoneNumber.contains("+")) user.phoneNumber
                        else "+" + user.phoneNumber
                    )
                }
            }
        }
    }

    fun getUserId() =
        dataStoreManager.check()


    // Make checkUpdate a suspend function and return a Boolean
    private suspend fun checkUpdate(): Boolean {
        return MinibusApi.retrofitService.updateUser(
            userUiState.value.userId,
            userUiState.value.firstName,
            userUiState.value.lastName,
            userUiState.value.phone
        ).isSuccessful
    }

    fun updateUser() {
        Log.d("fieldIsUpdated", "$fieldIsUpdated")
        if (checkFieldsForUpdates() && fieldIsUpdated) {

            viewModelScope.launch {
                // Call checkUpdate and wait for the result
                val updateSuccess = checkUpdate()

                // Only proceed if the update was successful
                if (updateSuccess) {
                    withContext(Dispatchers.Main) {
                        changeNotification(true)
                        Log.d("UpdateUser", "changeNotification called")
                        fieldIsUpdated = false
                    }
                    dataStoreManager.updateUserData(
                        userUiState.value.firstName,
                        userUiState.value.lastName,
                        userUiState.value.phone
                    )
                }
            }

        }
    }

    fun addUser() {
        if (checkFieldsForAdd()) {
            buttonState = ButtonUiState.Loading
            val call = MinibusApi.retrofitService.signUp(
                userUiState.value.firstName,
                userUiState.value.lastName,
                userUiState.value.phone,
                userUiState.value.password
            )

            call.enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if (response.isSuccessful) {
                        val user = response.body()
                        if (user != null) {
                            buttonState = ButtonUiState.Success
                            viewModelScope.launch { dataStoreManager.saveUserData(user) }
                        } else {
                            ButtonUiState.Error
                        }
                    } else {
                        Log.d("response code ", response.code().toString());
                        ButtonUiState.Error
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    Log.d("Failure", t.toString())
                    buttonState = ButtonUiState.Error
                }

            })
        }
    }


    fun logInUser() {
        if (checkFieldsForLogIn()) {

            buttonState = ButtonUiState.Loading

            val call = MinibusApi.retrofitService.logIn(
                userUiState.value.password,
                userUiState.value.phone
            )

            call.enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {

                    buttonState = if (response.isSuccessful) {
                        val user = response.body()
                        if (user != null) {
                            viewModelScope.launch { dataStoreManager.saveUserData(user) }
                            Log.d("logInUser",user.toString())
                            ButtonUiState.Success
                        } else {
                            ButtonUiState.Error
                        }
                    } else {
                        Log.d("response code ", response.code().toString());
                        ButtonUiState.Error
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    Log.d("Failure", t.toString())
                    buttonState = ButtonUiState.Error
                }
            })
        }
    }

    fun authorizationCheck(): Boolean {
        return dataStoreManager.check()
    }


    fun changeNotification(turn: Boolean) {
        showNotification = turn
    }

    fun updatePassword(input: String) {
        _userUiState.update { current ->
            current.copy(
                password = input
            )
        }
    }

    fun updateConfirmationPasswordField(input: String) {
        _userUiState.update { current ->
            current.copy(
                confirmationPasswordField = input
            )
        }
    }

    fun updateFirstName(input: String) {
        _userUiState.update { current ->
            current.copy(
                firstName = input
            )
        }
        fieldIsUpdated = true
    }

    fun updateLastName(input: String) {
        _userUiState.update { current ->
            current.copy(
                lastName = input
            )
        }
        fieldIsUpdated = true
    }

    fun updatePhone(input: String) {
        _userUiState.update { current ->
            current.copy(
                phone = input
            )
        }
        fieldIsUpdated = true
    }

    private fun phoneIsEmpty(): Boolean {
        val phone = userUiState.value.phone
        val phoneValid = phone.matches(Regex("^[+]?[0-9]{12}\$"))

        errorPhone = when {
            phone.isEmpty() -> R.string.error_format_empty
            !phoneValid -> R.string.error_format_phone
            else -> 0
        }

        val phoneIsEmpty = phone.isEmpty() || !phoneValid

        _userUiState.update { current ->
            current.copy(phoneIsEmpty = phoneIsEmpty)
        }

        return phoneIsEmpty
    }

    private fun passwordIsEmpty(): Boolean {
        val password = userUiState.value.password
        val passwordValid = password.length in 6..15

        errorPassword = when {
            password.isEmpty() -> R.string.error_format_empty
            !passwordValid -> R.string.password_error
            else -> 0

        }

        val passwordIsEmpty = password.isEmpty() || !passwordValid

        _userUiState.update { current ->
            current.copy(passwordIsEmpty = passwordIsEmpty)
        }

        return passwordIsEmpty
    }

    private fun confirmPasswordIsEmpty(): Boolean {
        val confirmationPassword = userUiState.value.confirmationPasswordField
        val password = userUiState.value.password
        val confirmationPasswordValid = confirmationPassword == password

        errorConfirmationPassword = when {
            confirmationPassword.isEmpty() -> R.string.error_format_empty
            !confirmationPasswordValid -> R.string.password_mismatch_error
            else -> 0

        }

        val confirmationPasswordIsEmpty = password.isEmpty() || !confirmationPasswordValid

        _userUiState.update { current ->
            current.copy(confirmationPasswordFieldIsEmpty = confirmationPasswordIsEmpty)
        }

        return confirmationPasswordIsEmpty
    }

    private fun firstNameIsEmpty(): Boolean {
        val firstName = userUiState.value.firstName
        val firstNameValid = firstName.length in 2..30

        errorFirstName = when {
            firstName.isEmpty() -> R.string.error_format_empty
            !firstNameValid -> R.string.error_format_first_name
            else -> 0

        }
        Log.d("ResourcesTagUserViewModel", "is $errorFirstName")
        val firstNameIsEmpty = firstName.isEmpty() || !firstNameValid

        _userUiState.update { current ->
            current.copy(firstNameIsEmpty = firstNameIsEmpty)
        }

        return firstNameIsEmpty
    }

    private fun lastNameIsEmpty(): Boolean {
        val lastName = userUiState.value.lastName
        val lastNameValid = lastName.length in 2..30

        errorLastName = when {
            lastName.isEmpty() -> R.string.error_format_empty
            !lastNameValid -> R.string.error_format_last_name
            else -> 0
        }

        val lastNameIsEmpty = lastName.isEmpty() || !lastNameValid

        _userUiState.update { current ->
            current.copy(lastNameIsEmpty = lastNameIsEmpty)
        }

        return lastNameIsEmpty
    }

    private fun checkFieldsForUpdates(): Boolean {
        val lastNameIsEmpty = lastNameIsEmpty()
        val firstNameIsEmpty = firstNameIsEmpty()
        val phoneIsEmpty = phoneIsEmpty()
        return !lastNameIsEmpty && !firstNameIsEmpty && !phoneIsEmpty
    }

    private fun checkFieldsForLogIn(): Boolean {
        val phoneIsEmpty = phoneIsEmpty()
        val passwordIsEmpty = passwordIsEmpty()
        return !phoneIsEmpty && !passwordIsEmpty
    }


    private fun checkFieldsForAdd(): Boolean {
        val lastNameIsEmpty = lastNameIsEmpty()
        val firstNameIsEmpty = firstNameIsEmpty()
        val phoneIsEmpty = phoneIsEmpty()
        val passwordIsEmpty = passwordIsEmpty()
        val confirmPasswordIsEmpty = confirmPasswordIsEmpty()
        return !lastNameIsEmpty && !firstNameIsEmpty && !phoneIsEmpty && !passwordIsEmpty && !confirmPasswordIsEmpty
    }


}

class UserViewModelFactory(private val dataStoreManager: DataStoreManager) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        UserViewModel(dataStoreManager) as T

}
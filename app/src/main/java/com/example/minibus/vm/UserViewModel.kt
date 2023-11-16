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
import com.example.minibus.state_models.UserUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UserViewModel(private val dataStoreManager: DataStoreManager) : ViewModel() {

    private val _userUiState = MutableStateFlow(UserUiState())
    val userUiState = _userUiState.asStateFlow()

    var showNotification by mutableStateOf(false)
    var errorPhone by mutableIntStateOf(0)
    var errorFirstName by mutableIntStateOf(0)
    var errorLastName by mutableIntStateOf(0)

    private var fieldIsUpdated = false

    init {
        getUserData()
        Log.d("UserViewModel", "create")
    }

    private fun getUserData() {
        viewModelScope.launch {
            dataStoreManager.getUserData().collect { user ->
                _userUiState.update { current ->
                    current.copy(
                        firstName = user.firstName,
                        lastName = user.lastName,
                        phone = if (user.phoneNumber.contains("+")) user.phoneNumber
                        else "+" + user.phoneNumber
                    )
                }
            }
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

    private fun checkFields(): Boolean {
        val lastNameIsEmpty = lastNameIsEmpty()
        val firstNameIsEmpty = firstNameIsEmpty()
        val phoneIsEmpty = phoneIsEmpty()
        return !lastNameIsEmpty && !firstNameIsEmpty && !phoneIsEmpty
    }

    fun updateUser() {
        if (checkFields() && fieldIsUpdated) {

            changeNotification(true)

            viewModelScope.launch {
                dataStoreManager.saveUserData(
                    userUiState.value.firstName,
                    userUiState.value.lastName,
                    userUiState.value.phone
                )
            }

            _userUiState.update { current ->
                current.copy(
                    lastNameIsEmpty = false,
                    phoneIsEmpty = false,
                    firstNameIsEmpty = false
                )
            }
            fieldIsUpdated = false
        }
    }

    fun changeNotification(turn: Boolean) {
        showNotification = turn
    }
}

class UserViewModelFactory(private val dataStoreManager: DataStoreManager) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        UserViewModel(dataStoreManager) as T

}
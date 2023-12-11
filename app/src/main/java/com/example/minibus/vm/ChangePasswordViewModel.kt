package com.example.minibus.vm

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.minibus.R
import com.example.minibus.network.MinibusApi
import com.example.minibus.state_models.ButtonUiState
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class ChangePasswordViewModel() : ViewModel() {

    var password by mutableStateOf("")
    var confirmationPasswordField by mutableStateOf("")

    var errorPassword by mutableIntStateOf(0)
    var errorConfirmationPassword by mutableIntStateOf(0)

    var passwordIsEmpty by mutableStateOf(false)
    var confirmationPasswordFieldIsEmpty by mutableStateOf(false)

    var changePasswordButton: ButtonUiState by mutableStateOf(ButtonUiState.Defolt)

    fun changePassword(userId:Int) {
        if (checkFieldsForUpdates()) {

            changePasswordButton = ButtonUiState.Loading


            viewModelScope.launch {
                changePasswordButton = try {
                    MinibusApi.retrofitService.changePassword(userId, password)
                    ButtonUiState.Success

                } catch (e: IOException) {
                    Log.d("change Password ERROR", "$e")
                    ButtonUiState.Error

                } catch (e: HttpException) {
                    Log.d("Change Password ERROR", "$e")
                    ButtonUiState.Error
                }
            }
        }
    }


    fun updatePassword(input: String) {
        password = input
    }

    fun updateConfirmationPasswordField(input: String) {
        confirmationPasswordField = input

    }

    private fun passwordIsEmpty(): Boolean {
        val passwordValid = password.length in 6..15

        errorPassword = when {
            password.isEmpty() -> R.string.error_format_empty
            !passwordValid -> R.string.password_error
            else -> 0

        }
        passwordIsEmpty = password.isEmpty() || !passwordValid

        return passwordIsEmpty
    }

    private fun confirmPasswordIsEmpty(): Boolean {

        val confirmationPasswordValid = confirmationPasswordField == password

        errorConfirmationPassword = when {
            confirmationPasswordField.isEmpty() -> R.string.error_format_empty
            !confirmationPasswordValid -> R.string.password_mismatch_error
            else -> 0
        }

        confirmationPasswordFieldIsEmpty = password.isEmpty() || !confirmationPasswordValid

        return confirmationPasswordFieldIsEmpty
    }

    private fun checkFieldsForUpdates(): Boolean {
        val confirmPasswordIsEmpty = confirmPasswordIsEmpty()
        val passwordIsEmpty = passwordIsEmpty()
        return !confirmPasswordIsEmpty && !passwordIsEmpty
    }

}

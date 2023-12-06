package com.example.minibus.state_models

data class UserUiState(
    val userId: Int = 0,
    val firstName: String = "",
    val lastName: String = "",
    val phone: String = "",
    val password: String = "",
    val confirmationPasswordField: String = "",
    val passwordIsEmpty : Boolean = false,
    val confirmationPasswordFieldIsEmpty: Boolean = false,
    val firstNameIsEmpty: Boolean = false,
    val lastNameIsEmpty: Boolean = false,
    val phoneIsEmpty: Boolean = false
)
package com.example.minibus.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.minibus.R
import com.example.minibus.dataStoreManager.DataStoreManager
import com.example.minibus.snackbarClasses.SnackbarDelegate
import com.example.minibus.state_models.ButtonUiState
import com.example.minibus.ui.theme.MinibusTheme
import com.example.minibus.vm.UserViewModel
import com.example.minibus.vm.UserViewModelFactory


@Composable
fun LoginScreen(
    userViewModel: UserViewModel,
    navController: NavController,
    snackbarDelegate: SnackbarDelegate
) {

    val userState by userViewModel.userUiState.collectAsState()

    var errorVisible by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .padding(
                start = dimensionResource(id = R.dimen.padding_medium),
                end = dimensionResource(id = R.dimen.padding_medium),
                top = dimensionResource(id = R.dimen.image_size)
            )
            .fillMaxSize()
    ) {
        ScreenTitle("Вход", fontSize = 20.sp)
        InputField(
            title = stringResource(id = R.string.user_phone_number),
            text = userState.phone,
            label = stringResource(id = R.string.input_phone_number),
            modifier = Modifier.padding(
                bottom =
                dimensionResource(id = R.dimen.padding_extra_small)
            ),
            onValueChanged = { userViewModel.updatePhone(it) },
            isTextEmpty = userState.phoneIsEmpty,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            errorText = userViewModel.errorPhone
        )

        PasswordInputField(
            title = stringResource(id = R.string.password), text = userState.password,
            label = stringResource(id = R.string.input_password),
            modifier = Modifier.padding(
                bottom =
                dimensionResource(id = R.dimen.padding_extra_small)
            ),
            onValueChanged = { userViewModel.updatePassword(it) },
            isTextEmpty = userState.passwordIsEmpty,
            errorText = userViewModel.errorPassword,

            )

        if (errorVisible)
            ErrorText(stringResource(id = R.string.sign_in_error))

        Button(
            onClick = {
                userViewModel.logInUser()
            }, modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = dimensionResource(id = R.dimen.padding_medium),
                    bottom = dimensionResource(id = R.dimen.padding_small)
                )
                .height(56.dp)

        ) {
            when (userViewModel.logInButtonState) {
                ButtonUiState.Defolt -> Text(text = stringResource(R.string.sign_in))
                ButtonUiState.Error -> {
                    Text(text = stringResource(R.string.sign_in))
                    errorVisible = true
                }
                ButtonUiState.Loading -> ButtonProgressIndicator()
                ButtonUiState.Success -> {
                    Text(text = stringResource(R.string.sign_in))
                    navController.navigate("option") {

                        popUpTo("logInScreen") {
                            saveState = true
                            inclusive = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                    userViewModel.setDefaultLogInButtonState()
                }
            }
        }
        RegistrationRow(registrationOnClick = {
            userViewModel.updateConfirmationPasswordField()
            navController.navigate("signUpScreen") })
    }
}

@Composable
fun ErrorText(message: String) {
        Text(text = message, modifier = Modifier.padding(start = dimensionResource(id = R.dimen.padding_small),top = dimensionResource(id = R.dimen.padding_extra_small)), color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
  }

@Composable
fun RegistrationRow(registrationOnClick: () -> Unit) {
    var clicked by remember { mutableStateOf(false) }
    val textColor by animateColorAsState(
        targetValue = if (clicked) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.outline,
        label = ""
    )

    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {


        val interactionSource = remember { MutableInteractionSource() }
        val isPressed = interactionSource.collectIsPressedAsState()
        val textColor =
            if (isPressed.value) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.outline

        Text(
            text = stringResource(R.string.sign_up),
            textDecoration = TextDecoration.Underline,
            modifier = Modifier.clickable(
                interactionSource = interactionSource,
                indication = null, // убираем стандартное выделение
                onClick = { registrationOnClick() }
            ),
            color = textColor
        )

    }
}


@Composable
@Preview
fun LoginScreenPreview() {
    MinibusTheme(useDarkTheme = false) {
        Surface(modifier = Modifier.fillMaxSize()) {
            val dataStoreManager = DataStoreManager(LocalContext.current)
            val userViewModel: UserViewModel =
                viewModel(factory = UserViewModelFactory(dataStoreManager))
            LoginScreen(userViewModel = userViewModel, rememberNavController(), SnackbarDelegate())
        }
    }
}

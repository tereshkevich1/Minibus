package com.example.minibus.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.minibus.R
import com.example.minibus.snackbarClasses.SnackbarBottomPadding
import com.example.minibus.snackbarClasses.SnackbarDelegate
import com.example.minibus.snackbarClasses.SnackbarState
import com.example.minibus.state_models.ButtonUiState
import com.example.minibus.ui.theme.MinibusTheme
import com.example.minibus.vm.ChangePasswordViewModel

@Composable
fun PasswordChangeScreen(
    userId: Int,
    navController: NavController,
    snackbarDelegate: SnackbarDelegate
) {
    val changePasswordViewModel: ChangePasswordViewModel = viewModel()
    var errorVisible by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .padding(
                start = dimensionResource(id = R.dimen.padding_medium),
                end = dimensionResource(id = R.dimen.padding_medium),
                bottom = dimensionResource(id = R.dimen.padding_medium),
                top = dimensionResource(id = R.dimen.padding_extra_small)
            )
            .fillMaxWidth()
    ) {

        PasswordInputField(
            title = stringResource(id = R.string.new_password), text = changePasswordViewModel.password,
            label = stringResource(id = R.string.input_new_password),
            modifier = Modifier.padding(
                bottom =
                dimensionResource(id = R.dimen.padding_extra_small)
            ),
            onValueChanged = { changePasswordViewModel.updatePassword(it) },
            isTextEmpty = changePasswordViewModel.passwordIsEmpty,
            errorText = changePasswordViewModel.errorPassword,

            )

        PasswordInputField(
            title = stringResource(id = R.string.password_confirmation_field),
            text = changePasswordViewModel.confirmationPasswordField,
            label = stringResource(id = R.string.reenter_password),
            modifier = Modifier.padding(
                bottom =
                dimensionResource(id = R.dimen.padding_extra_small)
            ),
            onValueChanged = { changePasswordViewModel.updateConfirmationPasswordField(it) },
            isTextEmpty = changePasswordViewModel.confirmationPasswordFieldIsEmpty,
            errorText = changePasswordViewModel.errorConfirmationPassword
        )

        if (errorVisible) {
            ErrorText("Что то пошло не так...")
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                changePasswordViewModel.changePassword(userId)
            }, modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            when (changePasswordViewModel.changePasswordButton) {
                ButtonUiState.Defolt -> Text(text = stringResource(R.string.create_account))
                ButtonUiState.Error -> {
                    Text(text = stringResource(R.string.create_account))
                    errorVisible = true
                }
                ButtonUiState.Loading -> ButtonProgressIndicator()
                ButtonUiState.Success -> {
                    NavigateToProfile(navController, snackbarDelegate)
                    Text(text = stringResource(R.string.create_account))
                }
            }

        }

    }
}

@Composable
fun NavigateToProfile(navController: NavController, snackbarDelegate: SnackbarDelegate) {
    val message = stringResource(R.string.password_changed)
    LaunchedEffect(Unit) {
        navController.popBackStack()
        snackbarDelegate.showSnackbar(
            state = SnackbarState.DEFAULT,
            message = message,
            snackbarBottomPadding = SnackbarBottomPadding.DEFAULT
        )
    }
}

@Composable
@Preview
fun PasswordChangeScreenPreview() {
    MinibusTheme(useDarkTheme = true) {
        Surface(modifier = Modifier.fillMaxSize()) {
            PasswordChangeScreen(5, rememberNavController(), SnackbarDelegate())
        }
    }
}
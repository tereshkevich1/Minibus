package com.example.minibus.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.minibus.R
import com.example.minibus.dataStoreManager.DataStoreManager
import com.example.minibus.state_models.ButtonUiState
import com.example.minibus.ui.theme.MinibusTheme
import com.example.minibus.vm.UserViewModel
import com.example.minibus.vm.UserViewModelFactory

@Composable
fun RegistrationScreen(userViewModel: UserViewModel, navController: NavController) {

    val userState by userViewModel.userUiState.collectAsState()

    Column(
        modifier = Modifier
            .padding(dimensionResource(id = R.dimen.padding_medium))
            .fillMaxWidth()
    ) {
        InputField(
            title = stringResource(id = R.string.user_first_name),
            text = userState.firstName,
            label = stringResource(id = R.string.input_first_name),
            modifier = Modifier.padding(
                bottom =
                dimensionResource(id = R.dimen.padding_extra_small)
            ),
            onValueChanged = { userViewModel.updateFirstName(it) },
            isTextEmpty = userState.firstNameIsEmpty,
            errorText = userViewModel.errorFirstName

        )
        Log.d("ResourcesTagpERSON", "is ${userViewModel.errorPhone}")

        InputField(
            title = stringResource(id = R.string.user_last_name),
            text = userState.lastName,
            label = stringResource(id = R.string.input_last_name),
            modifier = Modifier.padding(
                bottom =
                dimensionResource(id = R.dimen.padding_extra_small)
            ),
            onValueChanged = { userViewModel.updateLastName(it) },
            isTextEmpty = userState.lastNameIsEmpty,
            errorText = userViewModel.errorLastName
        )

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

        PasswordInputField(
            title = stringResource(id = R.string.password_confirmation_field),
            text = userState.confirmationPasswordField,
            label = stringResource(id = R.string.reenter_password),
            modifier = Modifier.padding(
                bottom =
                dimensionResource(id = R.dimen.padding_extra_small)
            ),
            onValueChanged = { userViewModel.updateConfirmationPasswordField(it) },
            isTextEmpty = userState.confirmationPasswordFieldIsEmpty,
            errorText = userViewModel.errorConfirmationPassword
        )

        //ErrorPanel()

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                userViewModel.addUser()
            }, modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            when (userViewModel.buttonState) {
                ButtonUiState.Defolt -> Text(text = stringResource(R.string.create_account))
                ButtonUiState.Error -> Text(text = "чето не то")
                ButtonUiState.Loading -> ButtonProgressIndicator()
                ButtonUiState.Success -> {
                    Text(text = stringResource(R.string.create_account))
                    navController.navigate("option") {
                        popUpTo("signUpScreen") {
                            saveState = true
                            inclusive = true
                        }
                        // Avoid multiple copies of the same destination when
                        // reselecting the same item
                        launchSingleTop = true
                        // Restore state when reselecting a previously selected item
                        restoreState = true
                    }
                }
            }

        }

    }
}

@Composable
fun PasswordInputField(
    title: String,
    text: String,
    isTextEmpty: Boolean,
    label: String,
    modifier: Modifier,
    onValueChanged: (String) -> Unit,
    errorText: Int

) {
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    Column(modifier = modifier) {
        Text(
            text = title,
            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_small)),
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
        )
        OutlinedTextField(
            value = text,
            onValueChange = onValueChanged,
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            isError = isTextEmpty,
            label = {
                Text(if (isTextEmpty) stringResource(errorText) else label)
            },
            placeholder = {
                if (isTextEmpty) Text(label)
            },
            shape = RoundedCornerShape(
                8.dp
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            singleLine = true,
            trailingIcon = {
                val image = if (passwordVisible)
                    painterResource(R.drawable.outline_visibility_24)
                else painterResource(R.drawable.baseline_remove_24)
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(painter = image, "")
                }
            }
        )
    }

}

@Composable
fun ScreenTitle(title: String, fontSize: TextUnit = 18.sp) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        Text(
            text = title,
            fontSize = fontSize,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
@Preview
fun RegistrationScreenDarkPreview() {
    MinibusTheme(useDarkTheme = false) {

        val dataStoreManager = DataStoreManager(LocalContext.current)
        val userViewModel: UserViewModel =
            viewModel(factory = UserViewModelFactory(dataStoreManager))
        Surface(modifier = Modifier.fillMaxSize()) {
            RegistrationScreen(userViewModel, rememberNavController())
        }

    }
}


package com.example.minibus.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.minibus.R
import com.example.minibus.dataStoreManager.DataStoreManager
import com.example.minibus.ui.theme.MinibusTheme
import com.example.minibus.vm.UserViewModel
import com.example.minibus.vm.UserViewModelFactory
import kotlinx.coroutines.delay

@Composable
fun PersonalInformationScreen(userViewModel: UserViewModel, navController: NavHostController) {

    LaunchedEffect(Unit) {
        userViewModel.getUserData()
    }
    val userState by userViewModel.userUiState.collectAsState()
    val openDialog = remember { mutableStateOf(false) }
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
                dimensionResource(id = R.dimen.padding_small)
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
                dimensionResource(id = R.dimen.padding_small)
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
                dimensionResource(id = R.dimen.padding_small)
            ),
            onValueChanged = { userViewModel.updatePhone(it) },
            isTextEmpty = userState.phoneIsEmpty,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            errorText = userViewModel.errorPhone
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = dimensionResource(id = R.dimen.padding_extra_small))
        ) {
            LogOutProfilePanel(stringResource(R.string.change_password), onClick = {})
            Spacer(modifier = Modifier.weight(1f))
            LogOutProfilePanel(
                stringResource(R.string.log_out_profile),
                onClick = { openDialog.value = true
                    Log.d("openDialog",openDialog.value.toString())})
        }

        //ErrorPanel()


        Spacer(modifier = Modifier.weight(0.5f))



        if (userViewModel.showNotification) {
            TopNotification(
                message = "Данные успешно обновлены",
                visible = userViewModel.showNotification,
                onDismiss = { userViewModel.changeNotification(false) }
            )
        }


        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                userViewModel.updateUser()
            }, modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text(text = stringResource(R.string.save_text))
        }



        when {
            openDialog.value -> {
                ConfirmationDialog(
                    onDismissRequest = { openDialog.value = false },
                    onConfirmation = {
                        userViewModel.clearFields()
                        userViewModel.clearUserData{
                            navController.navigate("logInScreen") {
                                popUpTo("Search") {
                                    inclusive = false
                                }
                                launchSingleTop = true
                            }
                        }
                    },
                    dialogTitle = "Выход из аккаунта",
                    dialogText = "Вы действительно хотите выйти из аккаунта?"
                )
            }

        }

    }

}


@Composable
fun InputField(
    title: String,
    text: String,
    isTextEmpty: Boolean,
    label: String,
    modifier: Modifier,
    onValueChanged: (String) -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions(),
    errorText: Int,
    padding: Dp = dimensionResource(id = R.dimen.padding_small)

) {

    Column(modifier = modifier) {
        Text(
            text = title,
            modifier = Modifier.padding(
                top = padding,
                bottom = padding,
                start = dimensionResource(id = R.dimen.padding_small),
                end = dimensionResource(id = R.dimen.padding_small)
            ),
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
            keyboardOptions = keyboardOptions,
            singleLine = true,
        )
    }

}

@Composable
fun ErrorPanel(serverErrorIsVisible: Boolean) {
    Text(
        text = stringResource(R.string.error_phone_number_is_not_available),
        color = MaterialTheme.colorScheme.error,
        modifier = Modifier.alpha(if (serverErrorIsVisible) 1f else 0f)
    )
}

@Composable
fun LogOutProfilePanel(label: String, onClick: () -> Unit) {
    Row(
        horizontalArrangement = Arrangement.Start, modifier = Modifier


    ) {
        val interactionSource = remember { MutableInteractionSource() }
        val isPressed = interactionSource.collectIsPressedAsState()
        val textColor =
            if (isPressed.value) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.outline

        Text(
            text = label,
            textDecoration = TextDecoration.Underline,
            modifier = Modifier.clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = { onClick() }
            ),
            color = textColor
        )
    }
}

@Composable
fun TopNotification(message: String, visible: Boolean, onDismiss: () -> Unit) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.inversePrimary)
    ) {
        Text(
            text = message,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.align(Alignment.Center)
        )
    }

    LaunchedEffect(visible) {
        if (visible) {
            delay(2000)
            onDismiss()
        }
    }
}

@Composable
@Preview
fun PersonalInformationScreenDarkPreview() {
    val dataStoreManager = DataStoreManager(LocalContext.current)
    val userViewModel: UserViewModel = viewModel(factory = UserViewModelFactory(dataStoreManager))
    MinibusTheme(useDarkTheme = true) {
        PersonalInformationScreen(userViewModel, rememberNavController())
    }

}

@Composable
@Preview
fun PersonalInformationScreenLightPreview() {
    val dataStoreManager = DataStoreManager(LocalContext.current)
    val userViewModel: UserViewModel = viewModel(factory = UserViewModelFactory(dataStoreManager))
    MinibusTheme(useDarkTheme = true) {

        PersonalInformationScreen(userViewModel, rememberNavController())

    }

}

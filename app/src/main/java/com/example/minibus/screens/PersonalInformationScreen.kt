package com.example.minibus.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.minibus.R
import com.example.minibus.dataStoreManager.DataStoreManager
import com.example.minibus.ui.theme.MinibusTheme
import com.example.minibus.vm.UserViewModel
import com.example.minibus.vm.UserViewModelFactory

@Composable
fun PersonalInformationScreen(userViewModel: UserViewModel) {


    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.padding_medium))
                .fillMaxWidth()
        ) {

            InputField(
                title = stringResource(id = R.string.user_first_name),
                text = userViewModel.firstName,
                label = stringResource(id = R.string.input_first_name),
                modifier = Modifier.padding(
                    bottom =
                    dimensionResource(id = R.dimen.padding_small)
                ),
                onValueChanged = { userViewModel.updateFirstName(it) }
            )

            InputField(
                title = stringResource(id = R.string.user_last_name),
                text = userViewModel.lastName,
                label = stringResource(id = R.string.input_last_name),
                modifier = Modifier.padding(
                    bottom =
                    dimensionResource(id = R.dimen.padding_small)
                ),
                onValueChanged = { userViewModel.updateLastName(it) }
            )

            InputField(
                title = stringResource(id = R.string.user_phone_number),
                text = userViewModel.phone,
                label = stringResource(id = R.string.input_phone_number),
                modifier = Modifier.padding(
                    bottom =
                    dimensionResource(id = R.dimen.padding_small)
                ),
                onValueChanged = { userViewModel.updatePhone(it) }
            )


            Spacer(modifier = Modifier.weight(1f))
            ElevatedButton(
                onClick = {  }, modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(text = stringResource(R.string.save_text))
            }

        }
    }
}


@Composable
fun InputField(
    title: String,
    text: String,
    label: String,
    modifier: Modifier,
    onValueChanged: (String) -> Unit
) {

    Column(modifier = modifier) {
        Text(
            text = title,
            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_small))
        )
        OutlinedTextField(
            value = text,
            onValueChange = onValueChanged,
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text(
                    text = label
                )
            })
    }

}

@Composable
@Preview
fun PersonalInformationScreenDarkPreview() {
    val dataStoreManager = DataStoreManager(LocalContext.current)
    val userViewModel: UserViewModel = viewModel(factory = UserViewModelFactory(dataStoreManager))
    MinibusTheme(useDarkTheme = true) {
        PersonalInformationScreen(userViewModel)
    }

}

@Composable
@Preview
fun PersonalInformationScreenLightPreview() {
    val dataStoreManager = DataStoreManager(LocalContext.current)
    val userViewModel: UserViewModel = viewModel(factory = UserViewModelFactory(dataStoreManager))
    MinibusTheme(useDarkTheme = true) {

        PersonalInformationScreen(userViewModel)

    }

}

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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.minibus.R
import com.example.minibus.ui.theme.MinibusTheme

@Composable
fun PersonalInformationScreen() {

    Column(
        modifier = Modifier
            .padding(dimensionResource(id = R.dimen.padding_medium))
            .fillMaxWidth()
    ) {

        InputField(
            title = stringResource(id = R.string.user_first_name),
            text = "",
            label = stringResource(id = R.string.input_first_name),
            modifier = Modifier.padding(
                bottom =
                dimensionResource(id = R.dimen.padding_small)
            )
        )

        InputField(
            title = stringResource(id = R.string.user_last_name),
            text = "",
            label = stringResource(id = R.string.input_last_name),
            modifier = Modifier.padding(
                bottom =
                dimensionResource(id = R.dimen.padding_small)
            )
        )

        InputField(
            title = stringResource(id = R.string.user_phone_number),
            text = "",
            label = stringResource(id = R.string.input_phone_number),
            modifier = Modifier.padding(
                bottom =
                dimensionResource(id = R.dimen.padding_small)
            )
        )


        Spacer(modifier = Modifier.weight(1f))
        ElevatedButton(
            onClick = { /*TODO*/ }, modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text(text = stringResource(R.string.continue_name))
        }

    }
}


@Composable
fun InputField(title: String, text: String, label: String, modifier: Modifier) {

    Column(modifier = modifier) {
        Text(text = title, modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_small)))
        OutlinedTextField(value = text, onValueChange = {}, modifier = Modifier.fillMaxWidth(),
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
    MinibusTheme(useDarkTheme = true) {
        Surface(modifier = Modifier.fillMaxSize()) {
            PersonalInformationScreen()
        }
    }

}

@Composable
@Preview
fun PersonalInformationScreenLightPreview() {
    MinibusTheme(useDarkTheme = false) {
        Surface(modifier = Modifier.fillMaxSize()) {
            PersonalInformationScreen()
        }
    }

}

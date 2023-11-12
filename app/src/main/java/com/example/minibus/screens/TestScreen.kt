package com.example.minibus.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.minibus.ui.theme.MinibusTheme

@Composable
fun TestScreen() {
    Surface(modifier = Modifier.fillMaxSize()) {
        SwitchPanel(modifier = Modifier.fillMaxWidth())
    }
}

@Composable
fun SwitchPanel(modifier: Modifier) {

    var firstButtonActive by rememberSaveable { mutableStateOf(true) }

    val activeColor = MaterialTheme.colorScheme.primary

    val passiveColor = Color.LightGray

    Row(
        modifier = modifier.fillMaxWidth(1f),
        verticalAlignment = Alignment.Top

    ) {
        CustomSwitchButton(
            activeColor,
            passiveColor,
            { firstButtonActive = true },
            "Будущие",
            firstButtonActive,
            Modifier.weight(1f)
        )
        CustomSwitchButton(
            passiveColor,
            activeColor,
            { firstButtonActive = false },
            "Архив",
            firstButtonActive,
            Modifier.weight(1f)
        )
    }
}


@Composable
fun CustomSwitchButton(
    activeColor: Color,
    passiveColor: Color,
    changeActive: () -> Unit,
    text: String,
    firstButtonActive: Boolean,
    modifier: Modifier
) {
    Button(
        onClick = { changeActive() },
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        modifier = modifier,
        shape = RectangleShape,
        contentPadding = PaddingValues(0.dp)
    ) {

        Column(
            modifier = Modifier.fillMaxWidth(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = text,
                fontWeight = FontWeight.Bold,
                color = if (firstButtonActive) activeColor else passiveColor
            )
            Spacer(modifier = Modifier.height(15.dp))
            Divider(
                modifier = Modifier.fillMaxWidth(), thickness = 3.dp,
                color = if (firstButtonActive) activeColor else passiveColor
            )

        }
    }
}


@Composable
fun TwoButtons() {
    // Состояние кнопок, true - первая кнопка активна, false - вторая кнопка активна
    var firstButtonActive by rememberSaveable { mutableStateOf(true) }

    val activeColor = MaterialTheme.colorScheme.primary

    val passiveColor = Color.LightGray

    val firstButton = @Composable {

        Button(
            onClick = { firstButtonActive = true },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            modifier = Modifier.fillMaxWidth(0.5f),
            shape = RectangleShape,
            contentPadding = PaddingValues(0.dp)
        ) {

            Column(
                modifier = Modifier.fillMaxWidth(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Будущие",
                    fontWeight = FontWeight.Bold,
                    color = if (firstButtonActive) activeColor else Color.LightGray
                )
                Spacer(modifier = Modifier.height(15.dp))
                Divider(
                    modifier = Modifier.fillMaxWidth(), thickness = 3.dp,
                    color = if (firstButtonActive) activeColor else Color.LightGray
                )

            }
        }
    }

    val secondButton = @Composable {

        Button(
            onClick = { firstButtonActive = false },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            modifier = Modifier.fillMaxWidth(),
            shape = RectangleShape,
            contentPadding = PaddingValues(0.dp)
        ) {


            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Архив",
                    fontWeight = FontWeight.Bold,
                    color = if (firstButtonActive) Color.LightGray else activeColor
                )
                Spacer(modifier = Modifier.height(15.dp))
                Divider(
                    modifier = Modifier.fillMaxWidth(), thickness = 3.dp,
                    color = if (firstButtonActive) Color.LightGray else activeColor
                )
            }

        }

    }



    Row(

        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        firstButton()
        secondButton()
    }
}
@Composable
fun CustomButton() {
    Button(onClick = { /*TODO*/ }) {

    }
}

@Composable
@Preview
fun TestScreenDarkPreview() {
    MinibusTheme(useDarkTheme = false) {
        TestScreen()
    }
}
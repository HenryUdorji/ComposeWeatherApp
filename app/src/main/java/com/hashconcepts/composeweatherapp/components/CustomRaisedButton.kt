package com.hashconcepts.composeweatherapp.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import com.hashconcepts.composeweatherapp.ui.theme.PrimaryDark
import com.hashconcepts.composeweatherapp.ui.theme.PrimaryLight

/**
 * @created 29/07/2022 - 11:24 PM
 * @project ComposeInstagramClone
 * @author  ifechukwu.udorji
 */

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CustomRaisedButton(
    modifier: Modifier = Modifier,
    text: String,
    isLoading: Boolean = false,
    onClick: () -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Button(
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(backgroundColor = PrimaryDark),
        shape = RoundedCornerShape(size = 5.dp),
        onClick = {
            keyboardController?.hide()
            onClick()
        }
    ) {
        if (isLoading) {
            CircularProgressIndicator(color = PrimaryLight)
        } else {
            Text(
                text = text,
                style = MaterialTheme.typography.button,
                color = Color.White,
                modifier = Modifier.padding(vertical = 7.dp)
            )
        }
    }
}
package com.example.getintouch.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.Modifier
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.getintouch.R
import com.example.getintouch.ui.component.FormField

@Composable
fun SignUpScreen(
    isLoading: Boolean,
    errorMessage: String,
    showErrorMessage: Boolean,
    onSignUp: (String, String, String, String, String) -> Unit
) {
    var organizationName by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(bottom = 16.dp)
            .imePadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(id = R.drawable.getintouch),
            contentDescription = "Instagram",
            modifier = Modifier.size(200.dp),
            tint = Color.Unspecified
        )

        Column(
            modifier = Modifier
                .padding(horizontal = 30.dp)
        ) {
            FormField(
                label = "Organization Name",
                value = organizationName
            ) {
                organizationName = it
            }
            FormField("Name", name) { name = it }
            FormField("Email", email) { email = it }
            FormField("Phone", phone) { phone = it }
            FormField("Password", password) { password = it }

            if (showErrorMessage) {
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(6.dp))
                    .background(
                        MaterialTheme.colorScheme.errorContainer
                    )
                    .padding(12.dp)

                ) {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black
                ),
                onClick = {
                    onSignUp(
                        organizationName,
                        name,
                        email,
                        phone,
                        password
                    )
                    organizationName = ""
                    name = ""
                    email = ""
                    phone = ""
                    password = ""
                },
                enabled = !isLoading // disable while loading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(20.dp)
                    )
                } else {
                    Text(text = "Sign Up")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignUpScreenPreview() {
    SignUpScreen(
        isLoading = false,
        errorMessage = "Ini error",
        showErrorMessage = true,
        onSignUp = { organizationName, name, email, phone, password ->

        }
    )
}
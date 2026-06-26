package com.example.getintouch.ui.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.getintouch.R
import com.example.getintouch.ui.component.Dropdown
import com.example.getintouch.ui.component.FormField
import com.example.getintouch.ui.component.MultiSelectDropdown
import com.example.getintouch.ui.model.DepartmentUi
import com.example.getintouch.ui.model.HobbyUi

@Composable
fun MemberSignUpScreen(
    isLoading: Boolean,
    hobbies: List<HobbyUi>,
    departments: List<DepartmentUi>,
    errorMessage: String,
    showErrorMessage: Boolean,
    onMemberSignUp: (String, String, DepartmentUi?, List<HobbyUi>) -> Unit,
    onClickLogIn: () -> Unit,
    modifier: Modifier = Modifier
) {
    var name by remember { mutableStateOf("") }
    var selectedDepartment by remember {
        mutableStateOf<DepartmentUi?>(null)
    }
    var selectedHobbies by remember {
        mutableStateOf<List<HobbyUi>>(emptyList())
    }
    var password by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
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
            FormField("Name", name) { name = it }
            Dropdown(
                label = "Department",
                items = departments,
                selectedItem = selectedDepartment,
                itemLabel = { it.name },
                onItemSelected = {
                    selectedDepartment = it
                },
                modifier = Modifier
                    .padding(bottom = 16.dp)
            )
            MultiSelectDropdown(
                hobbies = hobbies,
                selectedHobbies = selectedHobbies,
                onSelectionChange = {
                    selectedHobbies = it
                },
                modifier = Modifier
                    .padding(bottom = 20.dp)
            )
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
                    onMemberSignUp(
                        name,
                        password,
                        selectedDepartment,
                        selectedHobbies
                    )

                    name = ""
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

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Text(text = "Sudah punya akun? ")

                Text(
                    text = "Masuk",
                    color = Color.Blue,
                    modifier = Modifier.clickable {
                        onClickLogIn()
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MemberSignUpScreenPreview() {
    MemberSignUpScreen(
        isLoading = false,
        hobbies = listOf(
            HobbyUi(
                id = 1,
                name = "Gaming",
                isSelected = false
            ),
            HobbyUi(
                id = 2,
                name = "Reading",
                isSelected = false
            )
        ),

        departments = listOf(
            DepartmentUi(
                id = 1,
                name = "Mobile Developer",
                isSelected = false
            ),
            DepartmentUi(
                id = 2,
                name = "UI/UX",
                isSelected = false
            )
        ),
        errorMessage = "Ini error",
        showErrorMessage = true,
        onMemberSignUp = { name, password, selectedDepartment, selectedHobbies ->

        },
        onClickLogIn = {}
    )
}
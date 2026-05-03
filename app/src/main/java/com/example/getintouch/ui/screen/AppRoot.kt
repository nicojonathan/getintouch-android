package com.example.getintouch.ui.screen

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.getintouch.ui.viewmodel.SessionViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun AppRoot(context: Context) {

    val sessionViewModel: SessionViewModel = viewModel()

    val currentUser = sessionViewModel.currentUser

    if (currentUser == null) {
        InviteMemberScreen()
//        SignUpScreen(
//            isLoading = sessionViewModel.isLoading,
//            errorMessage = sessionViewModel.errorMessage,
//            showErrorMessage = sessionViewModel.showErrorMessage,
//            onSignUp = { organizationName, name, email, phone, password ->
//                sessionViewModel.adminSignUp(
//                    organizationName,
//                    name,
//                    email,
//                    phone,
//                    password
//                )
//            }
//        )
    } else {
        if (currentUser.role == "admin") {
            InviteMemberScreen();
        } else {
            MainScreen(context)
        }
    }
}
package com.example.getintouch.ui.screen

import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.getintouch.ui.viewmodel.SessionViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.getintouch.data.datastore.DeviceIdManager
import com.example.getintouch.data.local.AuthPreferences
import com.example.getintouch.data.repository.AuthRepository
import com.example.getintouch.data.repository.DepartmentRepository
import com.example.getintouch.data.repository.HobbyRepository
import com.example.getintouch.ui.viewmodel.SessionViewModelFactory
import android.os.Build
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.ui.Modifier
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.tasks.await

@Composable
fun AppRoot(
    context: Context,
    inviteToken: String?,
    modifier: Modifier = Modifier
        .navigationBarsPadding()
) {
    val deviceIdManager = DeviceIdManager(context)
    val deviceName = "${Build.MANUFACTURER} ${Build.MODEL}"
    var deviceId by remember {
        mutableStateOf("")
    }

    var fcmToken by remember {
        mutableStateOf("")
    }

    val authPreferences = remember {
        AuthPreferences(context)
    }
    val repo = remember {
        AuthRepository()
    }
    val departmentRepository = remember {
        DepartmentRepository(context)
    }
    val hobbyRepository = remember {
        HobbyRepository(context)
    }

    val sessionViewModel: SessionViewModel = viewModel(
        factory = SessionViewModelFactory(authPreferences, repo, departmentRepository, hobbyRepository, inviteToken)
    )
    LaunchedEffect(Unit) {
        deviceId = deviceIdManager.getOrCreateDeviceId()

        Log.e("Device ID: ", "${deviceId})")

        fcmToken = FirebaseMessaging
            .getInstance()
            .token
            .await()

        Log.e("FCM Token Login: ", "${fcmToken})")

        sessionViewModel.loadSession()
    }

    val currentUser = sessionViewModel.currentUser

    if (currentUser == null) {
        when (sessionViewModel.authMenu) {
            "signup" -> SignUpScreen(
                isLoading = sessionViewModel.isLoading,
                errorMessage = sessionViewModel.errorMessage,
                showErrorMessage = sessionViewModel.showErrorMessage,
                onSignUp = { organizationName, name, email, phone, password ->
                    sessionViewModel.adminSignUp(
                        organizationName,
                        name,
                        email,
                        phone,
                        password
                    )
                },
                onClickLogIn = {
                    sessionViewModel.authMenu = "login"
                },
                modifier = modifier
            )

            "membersignup" -> MemberSignUpScreen(
                isLoading = sessionViewModel.isLoading,
                hobbies = sessionViewModel.hobbies,
                departments = sessionViewModel.departments,
                errorMessage = sessionViewModel.errorMessage,
                showErrorMessage = sessionViewModel.showErrorMessage,
                onMemberSignUp = { name, password, selectedDepartment, selectedHobbies ->
                    if (inviteToken != null) {
                        sessionViewModel.memberSignUp(
                            name,
                            password,
                            selectedDepartment,
                            selectedHobbies,
                            inviteToken,
                            deviceId,
                            deviceName,
                            fcmToken
                        )
                    }
                },
                onClickLogIn = {
                    sessionViewModel.authMenu = "login"
                },
                modifier = modifier
            )

            "login" -> LogInScreen(
                isLoading = sessionViewModel.isLoading,
                errorMessage = sessionViewModel.errorMessage,
                showErrorMessage = sessionViewModel.showErrorMessage,
                onLogIn = { email, password  ->
                    sessionViewModel.logIn(
                        email,
                        password,
                        deviceId,
                        deviceName,
                        fcmToken
                    )
                },
                onClickSignUp = {
                    if (sessionViewModel.prevAuthMenu == "signup") {
                        sessionViewModel.authMenu = "signup"
                    } else {
                        sessionViewModel.authMenu = "membersignup"
                    }
                },
                modifier = modifier
            )
        }
    } else {
        if (currentUser.role == "admin") {
            AdminMainScreen(
                authPreferences = authPreferences,
                onLogOut = {
                    sessionViewModel.logout()
                }
            );
        } else {
            MainScreen(context, sessionViewModel)
        }
    }
}
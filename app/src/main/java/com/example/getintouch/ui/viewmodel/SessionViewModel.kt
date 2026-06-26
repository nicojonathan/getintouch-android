package com.example.getintouch.ui.viewmodel

import android.app.Person
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.getintouch.data.api.socket.SocketManager
import com.example.getintouch.data.datastore.DeviceIdManager
import com.example.getintouch.data.local.AuthPreferences
import com.example.getintouch.data.model.DepartmentDto
import com.example.getintouch.data.repository.AuthRepository
import com.example.getintouch.data.repository.DepartmentRepository
import com.example.getintouch.data.repository.HobbyRepository
import com.example.getintouch.ui.model.DepartmentUi
import com.example.getintouch.ui.model.HobbyUi
import com.example.getintouch.ui.model.PersonUi
import com.example.getintouch.utils.isSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SessionViewModel(
    private val authPreferences: AuthPreferences,
    private val repo: AuthRepository,
    private val departmentRepository: DepartmentRepository,
    private val hobbyRepository: HobbyRepository,
    private val inviteToken: String?
) : ViewModel() {
    var prevAuthMenu by mutableStateOf<String>("")
    var authMenu by mutableStateOf<String>("")
    var isLoading by mutableStateOf(false)
    var showErrorMessage by mutableStateOf(false)
    var errorMessage by mutableStateOf<String>("")
    var departments by mutableStateOf<List<DepartmentUi>>(emptyList())
        private set

    var hobbies by mutableStateOf<List<HobbyUi>>(emptyList())
        private set

    var currentUser by mutableStateOf<PersonUi?>(null)
        private set
    var isSessionChecked by mutableStateOf(false)

    fun updateCurrentUser(newPerson: PersonUi) {
        currentUser = newPerson
    }

    fun loadSession() {
        viewModelScope.launch {

            val token = authPreferences.tokenFlow.first()

            try {
                coroutineScope {
                    val departmentsDeferred = async(Dispatchers.IO) {
                        val response = departmentRepository.loadDepartments(
                            token,
                            inviteToken
                        )
                        response
                    }

                    val hobbiesDeferred = async(Dispatchers.IO) {

                        val response = hobbyRepository.loadHobbies(token)

                        response
                    }

                    val departmentsResponse =
                        departmentsDeferred.await()

                    val hobbiesResponse =
                        hobbiesDeferred.await()

                    if (!isSuccess(departmentsResponse.statusCode)) {

                        errorMessage =
                            departmentsResponse.body.message

                        showErrorMessage = true

                        return@coroutineScope
                    }

                    if (!isSuccess(hobbiesResponse.statusCode)) {
                        errorMessage =
                            hobbiesResponse.body.message

                        showErrorMessage = true

                        return@coroutineScope
                    }

                    hobbies =
                        hobbiesResponse.body.hobbies.map { hobby ->

                            HobbyUi(
                                id = hobby.id,
                                name = hobby.name,
                                isSelected = false,
                            )
                        }

                    departments =
                        departmentsResponse.body.departments.map { department ->
                            DepartmentUi(
                                id = department.id,
                                name = department.name,
                                isSelected = false
                            )
                        }

                }
            } catch (e: Exception) {
                currentUser = null
            }

            if (!token.isNullOrBlank()) {
                try {
                    val profileResponse = withContext(Dispatchers.IO) {
                        repo.getProfile(token)
                    }

                    if (!isSuccess(profileResponse.statusCode)) {
                        currentUser = null
                        authMenu = "login"
                    } else {

                        val user = profileResponse.body.user
                        Log.e("Ini User Woi: ", "${user}")
                        currentUser = PersonUi(
                            id = user.id,
                            name = user.name,

                            department = user.department ?: DepartmentDto(id = 0, name = ""),

                            hobbies = user.hobbies,

                            description = user.description,
                            instagram = user.instagram,
                            linkedin = user.linkedin,
                            profileUrl = user.profileUrl,
                            role = user.role,
                        )

                        // Connect to websocket here
                        SocketManager.connect(user.id)
                    }

                } catch (e: Exception) {
                    Log.e("ADA EXception: ", "${e}")
                    currentUser = null
                }

            } else {
                showErrorMessage = false
                authMenu = "signup"
            }

            // khusus menu member signup
            if (inviteToken != null) {
                showErrorMessage = false
                authMenu = "membersignup"
            }

            isSessionChecked = true
        }
    }


    fun adminSignUp(
        organizationName: String,
        name: String,
        email: String,
        phone: String,
        password: String
    ) {
        viewModelScope.launch {
            try {
                isLoading = true
                val response = withContext(Dispatchers.IO) {
                    repo.adminSignup(organizationName, name, email, phone, password)
                }

                if (response.statusCode in 200..299) {
                    authPreferences.saveToken(response.body.token)
                    val user = response.body.user
                    currentUser = PersonUi(
                        id = user.id,
                        name = user.name,

                        department = user.department,

                        hobbies = user.hobbies,

                        description = user.description,
                        instagram = user.instagram,
                        linkedin = user.linkedin,
                        profileUrl = user.profileUrl,
                        role = user.role,
                    )
                } else {
                    errorMessage = response.body.message
                    showErrorMessage = true
                }

            } catch (e: Exception) {
                errorMessage = e.message ?: ""
                showErrorMessage = true
            } finally {
                isLoading = false
            }
        }
    }

    fun memberSignUp(
        name: String,
        password: String,
        department: DepartmentUi?,
        hobbies: List<HobbyUi>,
        inviteToken: String,
        deviceId: String,
        deviceName: String,
        fcmToken: String
    ) {
        viewModelScope.launch {
            try {
                isLoading = true
                val response = withContext(Dispatchers.IO) {
                    repo.memberSignup(name, password, department, hobbies, inviteToken, deviceId, deviceName, fcmToken)
                }

                if (response.statusCode in 200..299) {
                    authPreferences.saveToken(response.body.token)
                    val user = response.body.user
                    currentUser = PersonUi(
                        id = user.id,
                        name = user.name,

                        department = user.department,

                        hobbies = user.hobbies,

                        description = user.description,
                        instagram = user.instagram,
                        linkedin = user.linkedin,
                        profileUrl = user.profileUrl,
                        role = user.role,
                    )
                } else {
                    errorMessage = response.body.message
                    showErrorMessage = true
                }

            } catch (e: Exception) {
                errorMessage = e.message ?: ""
                showErrorMessage = true
            } finally {
                isLoading = false
            }
        }
    }

    fun logIn(
        email: String,
        password: String,
        deviceId: String,
        deviceName: String,
        fcmToken: String,
    ) {
        prevAuthMenu = ""
        viewModelScope.launch {
            try {
                isLoading = true
                val response = withContext(Dispatchers.IO) {
                    repo.logIn(email, password, deviceId, deviceName, fcmToken)
                }

                if (response.statusCode in 200..299) {
                    authPreferences.saveToken(response.body.token)
                    val user = response.body.user
                    Log.e("Ini User Login Woi: ", "${user}")
                    currentUser = PersonUi(
                            id = user.id,
                            name = user.name,

                            department = user.department,

                            hobbies = user.hobbies,

                            description = user.description,
                            instagram = user.instagram,
                            linkedin = user.linkedin,
                            profileUrl = user.profileUrl,
                            role = user.role,
                        )
                } else {
                    errorMessage = response.body.message
                    showErrorMessage = true
                }

            } catch (e: Exception) {
                errorMessage = e.message ?: ""
                showErrorMessage = true
            } finally {
                isLoading = false
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            if (currentUser?.role == "admin") {
                prevAuthMenu = "signup"
                authMenu = "signup"
            } else if (currentUser?.role == "member") {
                prevAuthMenu = "membersignup"
                authMenu = "membersignup"
            }
            authPreferences.clearToken()
            currentUser = null
        }
    }
}
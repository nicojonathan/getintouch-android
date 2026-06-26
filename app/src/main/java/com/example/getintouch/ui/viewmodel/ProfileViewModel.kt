package com.example.getintouch.ui.viewmodel

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.getintouch.data.local.AuthPreferences
import com.example.getintouch.data.repository.DepartmentRepository
import com.example.getintouch.data.repository.HobbyRepository
import com.example.getintouch.data.repository.PersonRepository
import com.example.getintouch.ui.model.DepartmentUi
import com.example.getintouch.ui.model.HobbyUi
import com.example.getintouch.ui.model.PersonUi
import com.example.getintouch.utils.isSuccess
import com.example.getintouch.utils.uriToFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class ProfileViewModel(
    private val authPreferences: AuthPreferences,
    private val repo: PersonRepository,
    private val departmentRepository: DepartmentRepository,
    private val hobbyRepository: HobbyRepository,
) : ViewModel() {
    var isLoading by mutableStateOf(false)
    var showErrorMessage by mutableStateOf(false)
    var errorMessage by mutableStateOf<String>("")
    var departments by mutableStateOf<List<DepartmentUi>>(emptyList())
        private set

    var hobbies by mutableStateOf<List<HobbyUi>>(emptyList())
        private set

    init {
        loadDepartmentsAndHobbies()
    }
    fun loadDepartmentsAndHobbies() {
        viewModelScope.launch {
            val token = authPreferences.tokenFlow.first()

            try {
                coroutineScope {
                    val departmentsDeferred = async(Dispatchers.IO) {
                        val response = departmentRepository.loadDepartments(
                            token,
                            null
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
                errorMessage = "Terjadi Kesalahan"
                showErrorMessage = true
            }
        }
    }

    suspend fun editProfileData(person: PersonUi): PersonUi? {
        return try {
            isLoading = true

            val token = authPreferences.tokenFlow.first()

            if (token.isNullOrBlank()) {
                errorMessage = "Invalid Access"
                showErrorMessage = true
                return null
            }

            val response = withContext(Dispatchers.IO) {
                repo.editProfileData(person, token)
            }

            if (response.statusCode in 200..299) {
                val user = response.body.user
                PersonUi(
                    id = user.id,
                    name = user.name,
                    department = user.department,
                    hobbies = user.hobbies,
                    description = user.description,
                    instagram = user.instagram,
                    linkedin = user.linkedin,
                    profileUrl = user.profileUrl,
                    role = user.role
                )
            } else {
                errorMessage = response.body.message
                showErrorMessage = true
                null
            }
        } catch (e: Exception) {
            errorMessage = e.message ?: ""
            showErrorMessage = true
            null
        } finally {
            isLoading = false
        }
    }
    suspend fun uploadProfilePicture(file: File): String? {
        return try {
            isLoading = true

            val token = authPreferences.tokenFlow.first()

            if (token.isNullOrBlank()) {
                errorMessage = "Invalid Access"
                showErrorMessage = true
                return null
            }

            val response = withContext(Dispatchers.IO) {
                repo.uploadProfilePicture(file, token)
            }

            if (response.statusCode in 200..299) {
                response.body.fileUrl
            } else {
                errorMessage = response.body.message
                showErrorMessage = true
                null
            }

        } catch (e: Exception) {
            errorMessage = e.message ?: ""
            showErrorMessage = true
            null
        } finally {
            isLoading = false
        }
    }
}
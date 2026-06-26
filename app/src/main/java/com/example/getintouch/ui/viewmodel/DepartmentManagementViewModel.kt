package com.example.getintouch.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.getintouch.data.local.AuthPreferences
import com.example.getintouch.data.repository.DepartmentRepository
import com.example.getintouch.ui.model.DepartmentUi
import com.example.getintouch.utils.isSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class DepartmentManagementViewModel(
    private val authPreferences: AuthPreferences,
    private val departmentRepository: DepartmentRepository
): ViewModel() {
    var isLoading by mutableStateOf(false)
    var showErrorMessage by mutableStateOf(false)
    var errorMessage by mutableStateOf<String>("")

    var departments by mutableStateOf<List<DepartmentUi>>(emptyList())
        private set

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            val token = authPreferences.tokenFlow.first();

            if (token.isNullOrBlank()) {
                errorMessage = "Invalid Access"
                showErrorMessage = true

            } else {
                try {
                    coroutineScope {

                        val departmentsDeferred = async(Dispatchers.IO) {
                            departmentRepository.loadDepartments(token, null)
                        }

                        val departmentsResponse = departmentsDeferred.await()

                        // DEPARTMENTS CHECK
                        if (!isSuccess(departmentsResponse.statusCode)) {
                            errorMessage = departmentsResponse.body.message
                            showErrorMessage = true

                            return@coroutineScope
                        }

                        departments = departmentsResponse.body.departments.map { department ->
                            DepartmentUi(
                                id = department.id,
                                name = department.name,
                                isSelected = false
                            )
                        }
                    }

                } catch (e: Exception) {
                    errorMessage = e.message ?: ""
                    showErrorMessage = true

                } finally {
                    isLoading = false
                }
            }
        }
    }

    fun addDepartment(departmentName: String) {
        viewModelScope.launch {
            val token = authPreferences.tokenFlow.first();

            if (token.isNullOrBlank()) {
                errorMessage = "Invalid Access"
                showErrorMessage = true

            } else {
                try {
                    coroutineScope {

                        val departmentDeferred = async(Dispatchers.IO) {
                            departmentRepository.addDepartment(token, departmentName)
                        }

                        val departmentResponse = departmentDeferred.await()

                        // DEPARTMENTS CHECK
                        if (!isSuccess(departmentResponse.statusCode)) {
                            errorMessage = departmentResponse.body.message
                            showErrorMessage = true

                            return@coroutineScope
                        }

                        // var addedDepartment = departmentResponse.body.department
                        val addedDepartment = DepartmentUi(
                            id = departmentResponse.body.department.id,
                            name = departmentResponse.body.department.name,
                            isSelected = false
                        )

                        departments = departments + addedDepartment
                    }

                } catch (e: Exception) {
                    errorMessage = e.message ?: ""
                    showErrorMessage = true

                } finally {
                    isLoading = false
                }
            }
        }
    }

    fun removeDepartment(departmentId: Int) {
        viewModelScope.launch {
            val token = authPreferences.tokenFlow.first();

            if (token.isNullOrBlank()) {
                errorMessage = "Invalid Access"
                showErrorMessage = true

            } else {
                try {
                    coroutineScope {

                        val departmentDeferred = async(Dispatchers.IO) {
                            departmentRepository.removeDepartmentById(token, departmentId)
                        }

                        val departmentResponse = departmentDeferred.await()


                        // DEPARTMENTS CHECK
                        if (!isSuccess(departmentResponse.statusCode)) {
                            errorMessage = departmentResponse.body.message
                            showErrorMessage = true

                            return@coroutineScope
                        }

                        val deletedDepartment = DepartmentUi(
                            id = departmentResponse.body.department.id,
                            name = departmentResponse.body.department.name,
                            isSelected = false
                        )

                        departments = departments.filter {
                            it.id != deletedDepartment.id
                        }
                    }

                } catch (e: Exception) {
                    errorMessage = e.message ?: ""
                    showErrorMessage = true

                } finally {
                    isLoading = false
                }
            }
        }
    }
}
package com.example.getintouch.ui.viewmodel

import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.getintouch.data.repository.PersonRepository
import com.example.getintouch.ui.model.PersonUi
import com.example.getintouch.ui.model.DepartmentUi
import com.example.getintouch.ui.model.HobbyUi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewModelScope
import com.example.getintouch.data.datastore.DeviceIdManager
import com.example.getintouch.data.local.AuthPreferences
import com.example.getintouch.data.repository.DepartmentRepository
import com.example.getintouch.data.repository.HobbyRepository
import com.example.getintouch.data.repository.NotificationRepository
import com.example.getintouch.ui.UiEvent
import com.example.getintouch.ui.model.NotificationType
import com.example.getintouch.ui.model.NotificationUi
import com.example.getintouch.utils.isSuccess
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class HomeViewModel(
    private val authPreferences: AuthPreferences,
    private val repository: PersonRepository,
    private val hobbyRepository: HobbyRepository,
    private val departmentRepository: DepartmentRepository,
    private val notificationRepository: NotificationRepository,
    private val context: Context
) : ViewModel() {
    var isLoading by mutableStateOf(false)
    var showErrorMessage by mutableStateOf(false)
    var errorMessage by mutableStateOf<String>("")

    private var allPersons: List<PersonUi> = emptyList()

    var persons by mutableStateOf<List<PersonUi>>(emptyList())
        private set

    var departments by mutableStateOf<List<DepartmentUi>>(emptyList())
        private set

    var hobbies by mutableStateOf<List<HobbyUi>>(emptyList())
        private set

    var selectedPersonForDetail by mutableStateOf<PersonUi?>(null)

    // _namaVar artinya versi internal yang bisa diubah.
    private val _uiEvent = MutableSharedFlow<UiEvent>()

    // namaVar artinya versi publik yang hanya bisa dibaca
    val uiEvent = _uiEvent.asSharedFlow()

    init {
        loadData()
    }

    // initial data from DB
    private fun loadData() {
        viewModelScope.launch {
            val token = authPreferences.tokenFlow.first();

            if (token.isNullOrBlank()) {
                errorMessage = "Invalid Access"
                showErrorMessage = true

            } else {
                try {
                    coroutineScope {

                        val membersDeferred = async(Dispatchers.IO) {
                            repository.loadMembers(token)
                        }

                        val departmentsDeferred = async(Dispatchers.IO) {
                            departmentRepository.loadDepartments(token, null)
                        }

                        val hobbiesDeferred = async(Dispatchers.IO) {
                            hobbyRepository.loadHobbies(token)
                        }

                        val membersResponse = membersDeferred.await()

                        val departmentsResponse = departmentsDeferred.await()

                        val hobbiesResponse = hobbiesDeferred.await()

                        // MEMBERS CHECK
                        if (!isSuccess(membersResponse.statusCode)) {
                            errorMessage = membersResponse.body.message
                            showErrorMessage = true

                            return@coroutineScope
                        }

                        // DEPARTMENTS CHECK
                        if (!isSuccess(departmentsResponse.statusCode)) {
                            errorMessage = departmentsResponse.body.message
                            showErrorMessage = true

                            return@coroutineScope
                        }

                        // HOBBIES CHECK
                        if (!isSuccess(hobbiesResponse.statusCode)) {
                            errorMessage = hobbiesResponse.body.message
                            showErrorMessage = true

                            return@coroutineScope
                        }

                        val departmentMap =
                            departmentsResponse.body.departments.associateBy {
                                it.id
                            }

                        val hobbyMap =
                            hobbiesResponse.body.hobbies.associateBy {
                                it.id
                            }

                        persons = membersResponse.body.users.map { person ->

                            PersonUi(
                                id = person.id,
                                name = person.name,

                                department = person.department,

                                hobbies = person.hobbies,

                                description = person.description ?: "",
                                instagram = person.instagram ?: "",
                                linkedin = person.linkedin ?: "",
                                profileUrl = person.profileUrl ?: "",
                                role = person.role,
                            )
                        }

                        allPersons = persons;

                        departments = departmentsResponse.body.departments.map { department ->
                            DepartmentUi(
                                id = department.id,
                                name = department.name,
                                isSelected = false
                            )
                        }

                        hobbies = hobbiesResponse.body.hobbies.map { hobby ->
                            HobbyUi(
                                id = hobby.id,
                                name = hobby.name,
                                isSelected = false,
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

    // filtering
    private fun applyFilters(searchInput: String? = null) {
        val selectedHobbyNames = hobbies
            .filter { it.isSelected }
            .map { it.name }

        val selectedDepartmentNames = departments
            .filter { it.isSelected }
            .map { it.name }

        persons = allPersons.filter { person ->

            val matchesHobby = when {
                selectedHobbyNames.isEmpty() -> true

                selectedHobbyNames.size == 1 ->
                    person.hobbies.any { it.name in selectedHobbyNames }

                else ->
                    person.hobbies.map { it.name }.containsAll(selectedHobbyNames)
            }

            val matchesDepartment =
                selectedDepartmentNames.isEmpty() ||
                        person.department.name in selectedDepartmentNames

            val matchesSearch =
                searchInput.isNullOrBlank() ||
                        person.name.contains(searchInput, ignoreCase = true) || person.description.contains(searchInput, ignoreCase = true)

            matchesHobby && matchesDepartment && matchesSearch
        }
    }

    fun onHobbyClicked(id: Int) {
        // 1. toggle hobby
        hobbies = hobbies.map { hobby ->
            if (hobby.id == id) {
                hobby.copy(isSelected = !hobby.isSelected)
            } else {
                hobby
            }
        }

        applyFilters()
    }

    fun onDepartmentClicked(id: Int) {
        // 1. toggle department
        departments = departments.map { department ->
            if (department.id == id) {
                // department.isSelected = !department.isSelected
                department.copy(isSelected = !department.isSelected)
                // department
            } else {
                department
            }
        }

        applyFilters()
    }

    fun onSearch(searchInput: String) {
        applyFilters(searchInput)
    }

    fun onPersonCardClicked(id: Int) {
        selectedPersonForDetail = allPersons.find { person ->
            person.id == id
        }
    }

    suspend fun onUpdateFcmToken() {
        val deviceIdManager = DeviceIdManager(context)
        val deviceName = "${Build.MANUFACTURER} ${Build.MODEL}"
        val deviceId = deviceIdManager.getOrCreateDeviceId()

        val currentToken = FirebaseMessaging
            .getInstance()
            .token
            .await()
        Log.e("FCM Token for Sync: ", "${currentToken}")

        val token = authPreferences.tokenFlow.first()

        if (!token.isNullOrBlank()) {
            notificationRepository.syncFcmToken(deviceId, deviceName, currentToken, token)
        }
    }

    fun sendNotification(receiverId: Int, receiverName: String) {
        viewModelScope.launch {
            val token = authPreferences.tokenFlow.first()

            if (!token.isNullOrBlank()) {
                try {
                    val response = withContext(Dispatchers.IO) {
                        repository.sendNotification(receiverId, token)
                    }

                    if (isSuccess(response.statusCode)) {
                        val notification = response.body.notification
                        var message = ""

                        if (notification.type == NotificationType.SAY_HI.value) {
                            message = "You said hi to ${receiverName}"
                        } else if (notification.type == NotificationType.LIKE.value) {
                            message = "You liked ${receiverName}'s profile" //implemented later
                        } else if (notification.type == NotificationType.FOLLOW.value) {
                            message = "You followed ${receiverName}"
                        }

                        _uiEvent.emit(
                            UiEvent.ShowToast(message)
                        )
                    } else {
                        _uiEvent.emit(
                            UiEvent.ShowToast(response.body.message)
                        )
                    }
                } catch (e: Exception) {
                    _uiEvent.emit(
                        UiEvent.ShowToast(e.message ?: "")
                    )
                }
            }
        }
    }
}
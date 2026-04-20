package com.example.getintouch.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.getintouch.data.repository.PersonRepository
import com.example.getintouch.ui.model.PersonUi
import com.example.getintouch.ui.model.DepartmentUi
import com.example.getintouch.ui.model.HobbyUi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.getintouch.data.repository.DepartmentRepository
import com.example.getintouch.data.repository.HobbyRepository

class HomeViewModel(
    private val repository: PersonRepository,
    private val hobbyRepository: HobbyRepository,
    private val departmentRepository: DepartmentRepository
) : ViewModel() {
    private var allPersons: List<PersonUi> = emptyList()

    var persons by mutableStateOf<List<PersonUi>>(emptyList())
        private set

    var departments by mutableStateOf<List<DepartmentUi>>(emptyList())
        private set

    var hobbies by mutableStateOf<List<HobbyUi>>(emptyList())
        private set

    var selectedPersonForDetail by mutableStateOf<PersonUi?>(null)

    init {
        loadData()
    }

    // initial data from DB
    private fun loadData() {
        allPersons = repository.loadPersons()
        persons = allPersons

        departments = departmentRepository.getDepartments()
        hobbies = hobbyRepository.getHobbies()
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
                    person.hobbies.any { it in selectedHobbyNames }

                else ->
                    person.hobbies.containsAll(selectedHobbyNames)
            }

            val matchesDepartment =
                selectedDepartmentNames.isEmpty() ||
                        person.department in selectedDepartmentNames

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

    fun generateMockLoggedInUser(): PersonUi {
        return PersonUi(
            id = 1,
            name = "John Doe",
            department = "Engineering",
            hobbies = listOf("Reading", "Gaming"),
            description = "Halo, nama saya John Doe. Saya belajar dari department engineering, dan hobby saya adalah membaca buku self-development dan bermain game.",
            instagram = "@john.doe",
            linkedin = "@johndoe",
            profileUrl = "https://picsum.photos/id/1005/400/400"
        )
    }
}
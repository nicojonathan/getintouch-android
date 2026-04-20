package com.example.getintouch.ui.screen

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.getintouch.data.repository.DepartmentRepository
import com.example.getintouch.data.repository.HobbyRepository
import com.example.getintouch.data.repository.PersonRepository
import com.example.getintouch.ui.viewmodel.HomeViewModel

@Composable
fun MainScreen(context: Context) {
    val homeViewModel = remember {
        val hobbyRepository = HobbyRepository(context)
        val departmentRepository = DepartmentRepository(context)
        val personRepository = PersonRepository(context, hobbyRepository, departmentRepository)

        HomeViewModel(personRepository, hobbyRepository, departmentRepository)
    }

    var selectedMenu by remember { mutableStateOf("home") }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopBar(selectedMenu = selectedMenu)
        },
        bottomBar = {
            BottomNavigationBar(
                selected = selectedMenu,
                onItemSelected = { selectedMenu = it }
            )
        }
    ) { innerPadding ->
        when (selectedMenu) {
            "home" -> HomeScreen(
                viewModel = homeViewModel,
                modifier = Modifier.padding(innerPadding),
                onPersonCardClick = { personId ->
                    homeViewModel.onPersonCardClicked(personId)

                    if (homeViewModel.selectedPersonForDetail !== null) {
                        selectedMenu = "persondetail"
                    }
                }
            )

            "persondetail" -> PersonDetailScreen(
                person = homeViewModel.selectedPersonForDetail,
                onClickBack = { menu ->
                    selectedMenu = menu
                    homeViewModel.selectedPersonForDetail = null
                }
            )

            "profile" -> ProfileScreen(
                person = homeViewModel.generateMockLoggedInUser(),
                modifier = Modifier.padding(innerPadding)
            )

            "notification" -> NotificationScreen(
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(selectedMenu: String) {
    if (selectedMenu !== "persondetail") {
        TopAppBar(
            title = {
                Text(
                    text = when (selectedMenu) {
                        "home" -> "Home"
                        "profile" -> "Profile"
                        "notification" -> "Notification"
                        else -> ""
                    }
                )
            },
            modifier = Modifier.padding(bottom = 10.dp)
        )
    }
}

@Composable
fun BottomNavigationBar(
    selected: String,
    onItemSelected: (String) -> Unit
) {
    NavigationBar(
        containerColor = Color.White
    ) {
        val navItemColors = NavigationBarItemDefaults.colors(
            indicatorColor = Color.White
        )
        NavigationBarItem(
            selected = selected == "home",
            onClick = { onItemSelected("home") },
            icon = {
                Icon(
                    imageVector = if (selected == "home")
                        Icons.Filled.Home
                    else
                        Icons.Outlined.Home,
                    contentDescription = "Home"
                )
            },
            colors = navItemColors
        )
        NavigationBarItem(
            selected = selected == "notification",
            onClick = { onItemSelected("notification") },
            icon = {
                Icon(
                    imageVector = if (selected == "notification")
                        Icons.Filled.Notifications
                    else
                        Icons.Outlined.Notifications,
                    contentDescription = "Notification"
                )
            },
            colors = navItemColors
        )
        NavigationBarItem(
            selected = selected == "profile",
            onClick = { onItemSelected("profile") },
            icon = {
                Icon(
                    imageVector = if (selected == "profile")
                        Icons.Filled.Person
                    else
                        Icons.Outlined.Person,
                    contentDescription = "Profile"
                )
            },
            colors = navItemColors
        )
    }
}
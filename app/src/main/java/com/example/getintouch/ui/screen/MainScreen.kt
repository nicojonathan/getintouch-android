package com.example.getintouch.ui.screen

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
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
import com.example.getintouch.data.api.AppRepositories
import com.example.getintouch.data.api.socket.SocketManager
import com.example.getintouch.data.local.AuthPreferences
import com.example.getintouch.data.repository.DepartmentRepository
import com.example.getintouch.data.repository.HobbyRepository
import com.example.getintouch.data.repository.NotificationRepository
import com.example.getintouch.data.repository.PersonRepository
import com.example.getintouch.ui.model.NotificationUi
import com.example.getintouch.ui.viewmodel.HomeViewModel
import com.example.getintouch.ui.viewmodel.NotificationViewModel
import com.example.getintouch.ui.viewmodel.ProfileViewModel
import com.example.getintouch.ui.viewmodel.SessionViewModel
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.Alignment


@Composable
fun MainScreen(
    context: Context,
    sessionViewModel: SessionViewModel
) {
    fun instantiateRepo(): AppRepositories {
        val hobbyRepository = HobbyRepository(context)
        val departmentRepository = DepartmentRepository(context)
        val personRepository =
            PersonRepository(context, hobbyRepository, departmentRepository)
        val notificationRepository = NotificationRepository(context)
        val authPreferences = AuthPreferences(context)

        return AppRepositories(
            hobbyRepository,
            departmentRepository,
            personRepository,
            notificationRepository,
            authPreferences
        )
    }

    val repositories = remember {
        instantiateRepo()
    }
    val homeViewModel = remember {
        val (hobbyRepository, departmentRepository, personRepository, notificationRepository, authPreferences) = repositories


        HomeViewModel(authPreferences, personRepository, hobbyRepository, departmentRepository, notificationRepository, context)
    }

    val profileViewModel = remember {
        val (hobbyRepository, departmentRepository, personRepository, _, authPreferences) = repositories
        ProfileViewModel(authPreferences, personRepository, departmentRepository, hobbyRepository)
    }

    val notificationViewModel = remember {
        val (_, _, _, notificationRepository, authPreferences) = repositories
        NotificationViewModel(authPreferences, notificationRepository)
    }

    var selectedMenu by remember { mutableStateOf("home") }
    var previousMenu by remember { mutableStateOf<String?>(null) }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding(),
        topBar = {
            TopBar(
                selectedMenu = selectedMenu,
                onLogOut = { sessionViewModel.logout() }
            )
        },
        bottomBar = {
            BottomNavigationBar(
                selected = selectedMenu,
                onItemSelected = { selectedMenu = it }
            )
        },
        containerColor = Color.White,
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
                uiEvent = homeViewModel.uiEvent,
                onClickBack = {
                    selectedMenu = previousMenu ?: "home"
                    homeViewModel.selectedPersonForDetail = null
                },
                onSendNotification = { receiverId, receiverName ->
                    homeViewModel.sendNotification(receiverId, receiverName)
                }
            )

            "profile" -> sessionViewModel.currentUser?.let {
                ProfileRoute(
                    person = it,
                    departments = profileViewModel.departments,
                    hobbies = profileViewModel.hobbies,
                    profileViewModel = profileViewModel,
                    modifier = Modifier.padding(innerPadding),
                    updateCurrentUserData = { newPerson ->
                        sessionViewModel.updateCurrentUser(newPerson)
                    },
                )
            }

            "notification" -> NotificationScreen(
                modifier = Modifier.padding(innerPadding),
                notifications = notificationViewModel.notifications,
                socketManager = SocketManager,
                onNewNotificationArrive = {notification -> notificationViewModel.addNotification(notification)},
                onNotificationClicked = { personId ->
                    previousMenu = selectedMenu
                    homeViewModel.onPersonCardClicked(personId)

                    if (homeViewModel.selectedPersonForDetail !== null) {
                        selectedMenu = "persondetail"
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    selectedMenu: String,
    onLogOut: () -> Unit
) {
    if (selectedMenu === "profile") {
        TopAppBar(
            title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Profile"
                    )

                    IconButton(
                        onClick = {
                            onLogOut()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Logout,
                            contentDescription = "Logout",
                            tint = Color.Red
                        )
                    }
                }
            },
            modifier = Modifier.padding(bottom = 10.dp),
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White,
                titleContentColor = Color.Black,
                actionIconContentColor = Color.Black,
                navigationIconContentColor = Color.Black
            ),
        )
    } else if (selectedMenu !== "persondetail") {
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
            modifier = Modifier.padding(bottom = 10.dp),
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White,
                titleContentColor = Color.Black,
                actionIconContentColor = Color.Black,
                navigationIconContentColor = Color.Black
            ),
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
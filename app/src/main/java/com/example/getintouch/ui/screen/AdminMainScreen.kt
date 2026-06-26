package com.example.getintouch.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.getintouch.R
import com.example.getintouch.data.local.AuthPreferences
import com.example.getintouch.data.repository.AuthRepository
import com.example.getintouch.data.repository.DepartmentRepository
import com.example.getintouch.data.repository.OrganizationRepository
import com.example.getintouch.ui.model.DepartmentUi
import com.example.getintouch.ui.navigation.AdminMenu
import com.example.getintouch.ui.viewmodel.DepartmentManagementViewModel
import com.example.getintouch.ui.viewmodel.DepartmentManagementViewModelFactory
import com.example.getintouch.ui.viewmodel.InviteMemberViewModel
import com.example.getintouch.ui.viewmodel.InviteMemberViewModelFactory

@Composable
fun AdminMainScreen(
    authPreferences: AuthPreferences,
    onLogOut: () -> Unit
) {
    val context = LocalContext.current

    var currentMenu by remember {
        mutableStateOf(AdminMenu.MANAGE_DEPARTMENT)
    }
    
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    val orgRepo = remember {
        OrganizationRepository()
    }

    val departmentRepo = remember {
        DepartmentRepository(context)
    }

    val inviteMemberViewModel: InviteMemberViewModel = viewModel(
        factory = InviteMemberViewModelFactory(orgRepo, authPreferences)
    )
    val departmentManagementViewModel: DepartmentManagementViewModel = viewModel(
        factory = DepartmentManagementViewModelFactory(departmentRepo, authPreferences)
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = Color.White
            ) {

                Column(
                    modifier = Modifier.fillMaxSize()
                ) {

                    Icon(
                        painter = painterResource(id = R.drawable.getintouch),
                        contentDescription = "Instagram",
                        modifier = Modifier
                            .size(150.dp)
                            .align(Alignment.CenterHorizontally),
                        tint = Color.Unspecified
                    )

                    NavigationDrawerItem(
                        colors = NavigationDrawerItemDefaults.colors(
                            selectedContainerColor = Color.Black,
                            selectedTextColor = Color.White,
                            unselectedContainerColor = Color.White,
                            unselectedTextColor = Color.Black
                        ),
                        shape = RectangleShape,
                        label = { Text("Manage Department") },
                        selected = currentMenu == AdminMenu.MANAGE_DEPARTMENT,
                        onClick = {
                            currentMenu = AdminMenu.MANAGE_DEPARTMENT
                        }
                    )

                    NavigationDrawerItem(
                        colors = NavigationDrawerItemDefaults.colors(
                            selectedContainerColor = Color.Black,
                            selectedTextColor = Color.White,
                            unselectedContainerColor = Color.White,
                            unselectedTextColor = Color.Black
                        ),
                        shape = RectangleShape,
                        label = { Text("Invite Member") },
                        selected = currentMenu == AdminMenu.INVITE_MEMBER,
                        onClick = {
                            currentMenu = AdminMenu.INVITE_MEMBER
                        }
                    )

                    NavigationDrawerItem(
                        colors = NavigationDrawerItemDefaults.colors(
                            selectedContainerColor = Color.Black,
                            selectedTextColor = Color.White,
                            unselectedContainerColor = Color.White,
                            unselectedTextColor = Color.Black
                        ),
                        shape = RectangleShape,
                        label = { Text("Settings") },
                        selected = currentMenu == AdminMenu.SETTINGS,
                        onClick = {
                            currentMenu = AdminMenu.SETTINGS
                        }
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Button(
                        onClick = {
                            onLogOut()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Black,
                            contentColor = Color.White
                        )
                    ) {
                        Text("Logout")
                    }
                }
            }
        }
    ) {

        when (currentMenu) {
            AdminMenu.MANAGE_DEPARTMENT -> DepartmentManagementScreen(
                drawerState,
                departments = departmentManagementViewModel.departments,
                onAddDepartment = { name -> departmentManagementViewModel.addDepartment(name) },
                onDeleteDepartment = { id -> departmentManagementViewModel.removeDepartment(id) }
            )
            AdminMenu.INVITE_MEMBER -> InviteMemberScreen(drawerState, inviteMemberViewModel)
            AdminMenu.SETTINGS -> TODO()
            AdminMenu.MEMBER_LIST -> TODO()
        }
    }
}
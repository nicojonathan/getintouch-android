package com.example.getintouch.ui.screen

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.getintouch.ui.component.ConfirmationDialog
import com.example.getintouch.ui.model.DepartmentUi
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DepartmentManagementScreen(
    drawerState: DrawerState,
    departments: List<DepartmentUi>,
    onAddDepartment: (String) -> Unit,
    onDeleteDepartment: (Int) -> Unit
) {
    val scope = rememberCoroutineScope()
    var showDialog by remember {
        mutableStateOf(false)
    }

    var showDeleteDialog by remember {
        mutableStateOf(false)
    }

    var idDepartmentToBeDeleted  by remember {
        mutableStateOf(0)
    };

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                ),
                title = { Text("Manage Department") },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            scope.launch {
                                drawerState.open()
                            }
                        }
                    ) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu")
                    }
                }
            )
        },
        containerColor = Color.White,
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
        ) {

            Text(
                text = "Departments",
                style = MaterialTheme.typography.headlineSmall
            )


            Spacer(
                modifier = Modifier.height(16.dp)
            )


            Button(
                onClick = {
                    showDialog = true
                },
                colors =  ButtonDefaults.buttonColors(
                    containerColor = Color.Black
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null
                )

                Spacer(
                    modifier = Modifier.width(8.dp)
                )

                Text("Add Department")
            }


            Spacer(
                modifier = Modifier.height(20.dp)
            )

            LazyColumn {

                items(
                    items = departments,
                    key = { it.id }
                ) { department ->

                    DepartmentItem(
                        department = department,
                        onDelete = {
                            idDepartmentToBeDeleted = department.id
                            showDeleteDialog = true
                        }
                    )
                }
            }
        }
    }



    if(showDialog){

        AddDepartmentDialog(
            onDismiss = {
                showDialog = false
            },
            onSave = {
                onAddDepartment(it)
                showDialog = false
            }
        )
    }

    if (showDeleteDialog) {
        ConfirmationDialog(
            description = "Are you sure you want to delete this department?",
            onConfirm = {
                Log.e("Department to be deleted: ", "${idDepartmentToBeDeleted}")
                onDeleteDepartment(idDepartmentToBeDeleted)
                showDeleteDialog = false
            },
            onDismiss = {
                idDepartmentToBeDeleted = 0;
                showDeleteDialog = false
            }
        )
    }
}

@Composable
fun DepartmentItem(
    department: DepartmentUi,
    onDelete: () -> Unit
){

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Black
        )
    ){

        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ){

            Text(
                text = department.name,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White
            )


            IconButton(
                onClick = onDelete
            ){

                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = Color.White
                )

            }
        }
    }
}

@Composable
fun AddDepartmentDialog(
    onDismiss: () -> Unit,
    onSave: (String) -> Unit
) {
    var departmentName by remember {
        mutableStateOf("")
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        title = {
            Text(
                text = "Add Department",
                fontSize = 18.sp
            )
        },
        text = {
            OutlinedTextField(
                value = departmentName,
                onValueChange = {
                    departmentName = it
                },
                label = {
                    Text("Department Name")
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    if (departmentName.isNotBlank()) {
                        onSave(departmentName.trim())
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black
                )
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Gray
                )
            ) {
                Text("Cancel")
            }
        }
    )
}



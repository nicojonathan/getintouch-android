package com.example.getintouch.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.getintouch.ui.component.FormField
import kotlinx.coroutines.launch
import androidx.compose.material3.Icon
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import com.example.getintouch.ui.viewmodel.InviteMemberViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InviteMemberScreen(
    drawerState: DrawerState,
    inviteMemberViewModel: InviteMemberViewModel
) {
    var emailMember by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val clipboardManager = LocalClipboardManager.current

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                ),
                title = { Text("Invite Member") },
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
                .padding(horizontal = 48.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            FormField("Member's Email:", emailMember) {
                emailMember = it
            }

            if (inviteMemberViewModel.showSuccessMessage) {
                Column(

                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(6.dp))
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = inviteMemberViewModel.successMessage,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )

                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "Copy Link",
                            tint = Color.White,
                            modifier = Modifier.clickable {
                                inviteMemberViewModel.copyInviteLink(clipboardManager)
                            }
                        )
                    }

//                    Button(
//                        modifier = Modifier
//                            .fillMaxWidth(),
//                        colors = ButtonDefaults.buttonColors(
//                            containerColor = Color.Green
//                        ),
//                        onClick = { inviteMemberViewModel.copyInviteLink(clipboardManager) },
//                        enabled = !inviteMemberViewModel.isLoading
//                    ) {
//                        if (inviteMemberViewModel.isLoading) {
//                            CircularProgressIndicator(
//                                color = Color.White,
//                                strokeWidth = 2.dp,
//                                modifier = Modifier.size(20.dp)
//                            )
//                        } else {
//                            Icon(
//                                imageVector = Icons.Default.ContentCopy,
//                                contentDescription = "Copy Link",
//                                tint = Color.White
//                            )
//                        }
//                    }
                }
            }

            if (inviteMemberViewModel.showErrorMessage) {
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(6.dp))
                    .background(
                        MaterialTheme.colorScheme.errorContainer
                    )
                    .padding(12.dp)

                ) {
                    Text(
                        text = inviteMemberViewModel.errorMessage,
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }
            
            Button(
                modifier = Modifier
                    .fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black
                ),
                onClick = { inviteMemberViewModel.inviteMember(emailMember) },
                enabled = !inviteMemberViewModel.isLoading
            ) {
                if (inviteMemberViewModel.isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(20.dp)
                    )
                } else {
                    Text(text = "Invite Member")
                }
            }
        }
    }
}
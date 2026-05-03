package com.example.getintouch.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.getintouch.ui.model.PersonUi
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.getintouch.ui.mapper.toFilterItem
import com.example.getintouch.ui.model.FilterItemUi
import com.example.getintouch.ui.mapper.toFilterItem

@Composable
fun ProfileScreen(
    person: PersonUi,
    modifier: Modifier = Modifier
) {
    var text by remember { mutableStateOf(person.description) }
    var showFullImage by remember { mutableStateOf(false) }
    var showPicker by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 48.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {

        // PP, Name, Department
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
            ,
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box {
                // Foto Profil
                Box(
                    modifier = Modifier
                        .width(100.dp)
                        .height(100.dp)
                        .clip(CircleShape)
                        .clickable { showFullImage = true }
                ) {
                    AsyncImage(
                        model = person.profileUrl,
                        contentDescription = "${person.name} profile",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.matchParentSize()
                    )
                }

                // icon camera
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .shadow(4.dp, CircleShape)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.9f))
                        .border(1.dp, Color.Black.copy(alpha = 0.2f), CircleShape)
                        .align(Alignment.BottomEnd)
                        .clickable { showPicker = true },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = "Camera",
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = person.name,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit",
                    modifier = Modifier.size(16.dp)
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = person.department)

                Icon (
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit",
                    modifier = Modifier.size(16.dp)
                )
            }
        }


        // Hobby & Bio
        Column(
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {

            // Hobby
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Hobby")

                    Icon (
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit",
                        modifier = Modifier.size(16.dp)
                    )
                }

                FilterChips(
                    filterItems = person.hobbies,
                )
            }

            // Description
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Description")

                    Icon (
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit",
                        modifier = Modifier.size(16.dp)
                    )
                }

                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    placeholder = { Text("Write your bio...") },
                    maxLines = 5
                )
            }
        }
    }

    if (showFullImage) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.8f))
                .clickable { showFullImage = false }, // tap outside to close
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = person.profileUrl,
                contentDescription = "Full Image",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clip(CircleShape)
            )
        }
    }

    if (showPicker) {
        Dialog(onDismissRequest = { showPicker = false }) {
            Column(
                modifier = Modifier
                    .background(Color.White, RoundedCornerShape(12.dp))
                    .padding(16.dp)
            ) {
                Text(
                    text = "Choose Image",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                Text(
                    text = "📷 Take Photo",
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            showPicker = false
                            // open camera
                        }
                        .padding(12.dp)
                )

                Text(
                    text = "🖼️ Choose from Gallery",
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            showPicker = false
                            // open gallery
                        }
                        .padding(12.dp)
                )
            }
        }
    }
}

@Composable
fun FilterChips(
    filterItems: List<String>
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(filterItems) { item ->
            Box(
                modifier = Modifier
                    .background(Color.Black, RoundedCornerShape(8.dp))
                    .padding(4.dp)
            ) {
                Text(
                    text = item,
                    color = Color.White
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen(
        person = PersonUi(
            id = 1,
            name = "John Doe",
            department = "Engineering",
            hobbies = listOf("Reading", "Gaming"),
            description = "Halo, nama saya John Doe. Saya belajar dari department engineering, dan hobby saya adalah membaca buku self-development dan bermain game.",
            instagram = "@john.doe",
            linkedin = "@johndoe",
            profileUrl = "https://picsum.photos/id/1005/400/400",
            role = "admin"
        ),
        modifier = Modifier
    )
}


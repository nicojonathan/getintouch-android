package com.example.getintouch.ui.screen

import android.Manifest
import android.app.Person
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.zIndex
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import com.example.getintouch.BuildConfig
import com.example.getintouch.R
import com.example.getintouch.data.model.DepartmentDto
import com.example.getintouch.data.model.HobbyDto
import com.example.getintouch.data.repository.PersonRepository
import com.example.getintouch.ui.component.Dropdown
import com.example.getintouch.ui.component.FormField
import com.example.getintouch.ui.component.MultiSelectDropdown
import com.example.getintouch.ui.mapper.departmentDtoToDepartmentUi
import com.example.getintouch.ui.mapper.departmentUiToDepartmentDto
import com.example.getintouch.ui.mapper.hobbyDtoToHobbyUi
import com.example.getintouch.ui.mapper.hobbyUiToHobbyDto
import com.example.getintouch.ui.mapper.toFilterItem
import com.example.getintouch.ui.model.FilterItemUi
import com.example.getintouch.ui.mapper.toFilterItem
import com.example.getintouch.ui.model.DepartmentUi
import com.example.getintouch.ui.model.HobbyUi
import com.example.getintouch.ui.viewmodel.ProfileViewModel
import com.example.getintouch.utils.uriToFile
import kotlinx.coroutines.launch
import java.io.File

@Composable
fun ProfileRoute(
    person: PersonUi,
    departments: List<DepartmentUi>,
    hobbies: List<HobbyUi>,
    profileViewModel: ProfileViewModel,
    modifier: Modifier = Modifier,
    updateCurrentUserData: (newPerson: PersonUi) -> Unit,
) {

    val scope = rememberCoroutineScope()
    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    ProfileContent(
        person = person,
        departments = departments,
        hobbies = hobbies,
        modifier = modifier,
        isLoading = profileViewModel.isLoading,
        imageUri = imageUri,
        onImageUriChange = {uri -> imageUri = uri},
        onUploadProfilePicture = { file ->
            scope.launch {
                val profileUrl = profileViewModel.uploadProfilePicture(file)

                if (profileUrl != null) {
                    val newPerson = person.copy(profileUrl = profileUrl)
                    updateCurrentUserData(newPerson)
                }

                imageUri = null
            }
        },
        onEditProfileData = { person ->
            scope.launch {
                Log.e("personn: ", "${person}")
                val newPerson = profileViewModel.editProfileData(person)

                if (newPerson != null) {
                    Log.e("newPerson: ", "${newPerson}")
                    updateCurrentUserData(newPerson)
                }
            }
        }
    )
}

@Composable
fun ProfileContent(
    person: PersonUi,
    departments: List<DepartmentUi>,
    hobbies: List<HobbyUi>,
    isLoading: Boolean,
    imageUri: Uri?,
    onImageUriChange: (Uri?) -> Unit,
    onUploadProfilePicture: (File) -> Unit,
    onEditProfileData: (PersonUi) -> Unit,
    modifier: Modifier = Modifier
) {
    Log.e("Person in Profile: ", "${person}")

    val context = LocalContext.current
    var description by remember(person) { mutableStateOf(person.description) }
    var showFullImage by remember { mutableStateOf(false) }
    var showPicker by remember { mutableStateOf(false) }
    var showEditProfileForm by remember { mutableStateOf(false) }

    val cameraImageUri = remember {
        FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            File.createTempFile(
                "camera_image",
                ".jpg",
                context.cacheDir
            )
        )
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            Log.e("cameraImageUri: ", "${cameraImageUri}")
            onImageUriChange(cameraImageUri)
        }
    }

    val cameraPermissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) { isGranted ->

            if (isGranted) {
                cameraLauncher.launch(cameraImageUri)
            }
        }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        onImageUriChange(uri)
    }

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
                    val imageUrl =
                        if (BuildConfig.ENV == "development") {
                            BuildConfig.BASE_URL + person.profileUrl.removePrefix("/")
                        } else {
                            person.profileUrl
                        }

                    AsyncImage(
                        model = imageUrl,
                        placeholder = painterResource(R.drawable.default_profile),
                        error = painterResource(R.drawable.default_profile),
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
                        .clickable { showPicker = true; onImageUriChange(null) },
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
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = person.department.name)
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text(
                    text = "Edit Profile",
                    color = MaterialTheme.colorScheme.primary,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier
                        .clickable { showEditProfileForm = true }
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
                }

                FilterChips(
                    filterItems = person.hobbies.map { it.name },
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
                    Text(
                        text = "Description"
                    )
                }

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    placeholder = { Text("Write your bio...") },
                    maxLines = 5
                )
            }

            // Instagram
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_instagram),
                    contentDescription = "Instagram",
                    modifier = Modifier.size(24.dp),
                    tint = Color.Unspecified
                )

                Text(
                    text = person.instagram
                )
            }

            // Linkedin
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_linkedin),
                    contentDescription = "LinkedIn",
                    modifier = Modifier.size(24.dp),
                    tint = Color.Unspecified
                )

                Text(
                    text = person.linkedin
                )
            }
        }
    }


    if (isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.4f))
                .zIndex(999f),
            contentAlignment = Alignment.Center
        ) {

            CircularProgressIndicator(
                color = Color.White,
                strokeWidth = 2.dp
            )
        }
    }

    if (showFullImage) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.8f))
                .clickable { showFullImage = false }, // tap outside to close
            contentAlignment = Alignment.Center
        ) {
            val imageUrl =
                if (BuildConfig.ENV == "development") {
                    BuildConfig.BASE_URL + person.profileUrl.removePrefix("/")
                } else {
                    person.profileUrl
                }

            AsyncImage(
                model = imageUrl,
                contentDescription = "Full Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .aspectRatio(1f)
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
                            cameraPermissionLauncher.launch(
                                Manifest.permission.CAMERA
                            )
                        }
                        .padding(12.dp)
                )

                Text(
                    text = "🖼️ Choose from Gallery",
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            showPicker = false
                            galleryLauncher.launch("image/*")
                        }
                        .padding(12.dp)
                )
            }
        }
    }

    imageUri?.let { uri ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.85f))
        ) {

            // Preview Image
            AsyncImage(
                model = uri,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(280.dp)
                    .clip(CircleShape)
                    .align(Alignment.Center)
            )

            // Close Button (Top Left)
            IconButton(
                onClick = {
                    onImageUriChange(null)
                },
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(16.dp)
                    .background(
                        Color.Black.copy(alpha = 0.5f),
                        CircleShape
                    )
                    .zIndex(1f)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    tint = Color.White
                )
            }

            // Done Button (Top Right)
            TextButton(
                onClick = {
                    // upload/save image here
                    val file = uriToFile(context, uri)
                    onUploadProfilePicture(file)
                },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
                    .background(
                        Color.Black.copy(alpha = 0.5f),
                        RoundedCornerShape(50)
                    )
                    .zIndex(1f)
            ) {
                Text(
                    text = "Simpan Perubahan",
                    color = Color.White
                )
            }
        }
    }

    if (showEditProfileForm) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 1f))
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            EditProfileForm(
                person = person,
                departments = departments,
                hobbies = hobbies,
                onCancelEditProfile = { showEditProfileForm = false },
                onEditProfileData = { person -> onEditProfileData(person); showEditProfileForm = false }
            )
        }
    }
}

@Composable
fun EditProfileForm(
    person: PersonUi,
    departments: List<DepartmentUi>,
    hobbies: List<HobbyUi>,
    onCancelEditProfile: () -> Unit,
    onEditProfileData: (PersonUi) -> Unit
) {
    var name by remember { mutableStateOf(person.name) }
    var description by remember { mutableStateOf(person.description) }
    var instagram by remember { mutableStateOf(person.instagram) }
    var linkedin by remember { mutableStateOf(person.linkedin) }
    var selectedDepartment by remember {
        mutableStateOf<DepartmentUi?>(departmentDtoToDepartmentUi(person.department))
    }
    var selectedHobbies by remember {
        mutableStateOf<List<HobbyUi>>(person.hobbies.map { hobbyDtoToHobbyUi(it) })
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "Close",
            tint = Color.White,
            modifier = Modifier
                .align(Alignment.End)
                .clickable {
                    onCancelEditProfile()
                }
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(top = 12.dp)
        ) {
            FormField(
                "Name",
                value = name,
                textColor = Color.White,
                containerColor = Color.Transparent,
                borderColor = Color.White
            ) { name = it }

            Dropdown(
                label = "Department",
                selectedTextColor = Color.White,
                itemTextColor = Color.White,
                borderColor = Color.White,
                labelColor = Color.White,
                items = departments,
                selectedItem = selectedDepartment,
                itemLabel = { it.name },
                onItemSelected = {
                    selectedDepartment = it
                },
                modifier = Modifier
                    .padding(bottom = 16.dp)
            )
            MultiSelectDropdown(
                selectedTextColor = Color.White,
                itemTextColor = Color.White,
                borderColor = Color.White,
                labelColor = Color.White,
                hobbies = hobbies,
                selectedHobbies = selectedHobbies,
                onSelectionChange = {
                    selectedHobbies = it
                },
                modifier = Modifier
                    .padding(bottom = 20.dp)
            )
            FormField(
                label = "Description",
                value = description,
                textColor = Color.White,
                containerColor = Color.Transparent,
                borderColor = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                maxLines = 5
            ) {
                description = it
            }

            FormField(
                "Instagram",
                value = instagram,
                textColor = Color.White,
                containerColor = Color.Transparent,
                borderColor = Color.White
            ) { instagram = it }

            FormField(
                "LinkedIn",
                value = linkedin,
                textColor = Color.White,
                containerColor = Color.Transparent,
                borderColor = Color.White
            ) { linkedin = it }

            Button(
                onClick = {

                    val editedPerson = PersonUi(
                        id = person.id,
                        name = name,
                        description = description,
                        department = selectedDepartment?.let { departmentUiToDepartmentDto(it) } ?: person.department,
                        hobbies = selectedHobbies?.let { selectedHobbies.map { hobbyUiToHobbyDto(it) } } ?: person.hobbies,
                        instagram = instagram,
                        linkedin = linkedin,
                        profileUrl = person.profileUrl,
                        role = person.role,
                    )

                    onEditProfileData(editedPerson)

                },
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.CenterHorizontally)
                    .background(
                        Color.Black,
                        CircleShape
                    )
            ) {
                Text(text = "Save Changes")
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

    ProfileContent(
        person = PersonUi(
            id = 1,
            name = "John Doe",
            department = DepartmentDto(id = 1, name = ""),
            hobbies = listOf(
                HobbyDto(
                    id = 1,
                    name = ""
                )
            ),
            description = "Halo, nama saya John Doe. Saya belajar dari department engineering.",
            instagram = "@john.doe",
            linkedin = "@johndoe",
            profileUrl = "https://picsum.photos/id/1005/400/400",
            role = "admin"
        ),
        departments = listOf(
            DepartmentUi(
                id = 1,
                name = "Department",
                isSelected = false
            )
        ),
        hobbies = listOf(
            HobbyUi(
                id = 1,
                name = "Hobby",
                isSelected = false
            )
        ),
        onUploadProfilePicture = {},
        isLoading = false,
        imageUri = null,
        onImageUriChange = {},
        onEditProfileData = {}
    )
}


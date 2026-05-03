package com.example.getintouch.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.getintouch.ui.model.PersonUi
import coil.compose.AsyncImage
import com.example.getintouch.R

@Composable
fun PersonDetailScreen(
    person: PersonUi?,
    onClickBack: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Person's Photo
        Box(modifier = Modifier
            .height(400.dp)
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(
                    bottomStart = 20.dp,
                    bottomEnd = 20.dp
                )
            )
        ) {
            AsyncImage(
                model = person?.profileUrl,
                contentDescription = "${person?.name} profile",
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize()
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .align(Alignment.BottomStart)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color(0xAA000000))
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 32.dp)
                    .padding(bottom = 32.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = person?.name ?: "",
                    color = Color.White,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = person?.department ?: "",
                    color = Color.White
                )
            }

            IconButton(
                onClick = { onClickBack("home") },
                modifier = Modifier
                    .padding(top = 40.dp)
                    .padding(start = 20.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back"
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(top = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Person's Hobby
            @OptIn(ExperimentalLayoutApi::class)
            FlowRow {
                person?.hobbies?.forEach { item ->
                    Box(modifier = Modifier
                        .padding(4.dp)
                        .background(
                            Color.Black,
                            RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = item,
                            color = Color.White
                        )
                    }
                }
            }

            // Person's Description
            Box {
                Text(text = person?.description ?: "")
            }

            // Person's IG
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

                Text(text = person?.instagram ?: "")
            }

            // Person's LinkedIn
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

                Text(text = person?.linkedin ?: "")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PersonDetailPreview() {
    val personMock = PersonUi(
        id = 0,
        name = "John Doe",
        department = "Engineering",
        hobbies = listOf("Reading", "Gaming"),
        description = "Test test test",
        instagram = "@john.doe",
        linkedin = "@john.doe",
        profileUrl = "https://cdn.sejutacita.id/dealls-blog-cms/software_developer_vs_software_engineer_1ff36c15f7.jpg",
        role = "admin"
    )
    
    PersonDetailScreen(person = personMock, onClickBack = {})
}
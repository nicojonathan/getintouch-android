package com.example.getintouch.ui.screen

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.getintouch.data.repository.DepartmentRepository
import com.example.getintouch.data.repository.HobbyRepository
import com.example.getintouch.data.repository.PersonRepository
import com.example.getintouch.ui.model.PersonUi
import com.example.getintouch.ui.viewmodel.HomeViewModel
import androidx.compose.ui.draw.clip
import coil.compose.AsyncImage
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import com.example.getintouch.ui.mapper.toFilterItem
import com.example.getintouch.ui.model.FilterItemUi
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.Dialog

@Composable
fun HomeScreen(viewModel: HomeViewModel, modifier: Modifier = Modifier, onPersonCardClick: (Int) -> Unit) {
    val persons = viewModel.persons
    val departments = viewModel.departments
    val hobbies = viewModel.hobbies
    var searchText by remember { mutableStateOf("") }
    var seeAllDepartment by remember { mutableStateOf(false) }
    var seeAllHobby by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {
            SearchBar(
                searchText = searchText,
                onSearch = { searchInput ->
                    searchText = searchInput;
                    viewModel.onSearch(searchInput)
                }
            )

            // Department
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp) // optional padding
            ) {
                SectionTitle(
                    title = "Department",
                    onClickSeeAll = { seeAllDepartment = true }
                )
                Spacer(modifier = Modifier.height(8.dp)) // optional spacing between items
                FilterChips(
                    filterItems = departments.map { it.toFilterItem() },
                    onItemClick = { id -> viewModel.onDepartmentClicked(id) }
                )
            }


            // Hobby
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp) // optional padding
            ) {
                SectionTitle(
                    title = "Hobby",
                    onClickSeeAll = { seeAllHobby = true }
                )
                Spacer(modifier = Modifier.height(8.dp)) // optional spacing between items
                FilterChips(
                    filterItems = hobbies.map { it.toFilterItem() },
                    onItemClick = { id -> viewModel.onHobbyClicked(id) }
                )
            }

            PeopleGrid(persons, onPersonCardClick)
        }

        if (seeAllDepartment) {
            Dialog(onDismissRequest = { seeAllDepartment = false }) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White, RoundedCornerShape(12.dp))
                        .padding(16.dp)
                ) {
                    AllFilterDisplay(
                        filterItems = departments.map { it.toFilterItem() },
                        onItemClick = { id -> viewModel.onDepartmentClicked(id) }
                    )
                }
            }
        }

        if (seeAllHobby) {
            Dialog(onDismissRequest = { seeAllHobby = false }) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White, RoundedCornerShape(12.dp))
                        .padding(16.dp)
                ) {
                    AllFilterDisplay(
                        filterItems = hobbies.map { it.toFilterItem() },
                        onItemClick = { id -> viewModel.onHobbyClicked(id) }
                    )
                }
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    MainScreen(LocalContext.current)
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AllFilterDisplay(
    filterItems: List<FilterItemUi>,
    onItemClick: (Int) -> Unit
) {
    FlowRow(
        modifier = Modifier.fillMaxWidth()
    ) {
        filterItems.forEach { item ->
            Box(
                modifier = Modifier
                    .padding(4.dp)
                    .then(
                        if (item.isSelected) {
                            Modifier.background(
                                Color.Black,
                                RoundedCornerShape(8.dp)
                            )
                        } else {
                            Modifier.border(
                                width = 1.dp,
                                color = Color.Black,
                                shape = RoundedCornerShape(8.dp)
                            )
                        }
                    )
                    .clickable { onItemClick(item.id) }
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = item.label,
                    color = if (item.isSelected) Color.White else Color.Black
                )
            }
        }
    }
}

@Composable
fun SearchBar(
    searchText: String,
    onSearch: (String) -> Unit
) {
    TextField(
        value = searchText,
        onValueChange = { newValue ->
            onSearch(newValue)
        },
        placeholder = { Text("Search") },
        shape = RoundedCornerShape(16.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.LightGray,
            unfocusedContainerColor = Color.LightGray,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun SectionTitle(
    title: String,
    modifier: Modifier = Modifier,
    onClickSeeAll: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title)
        TextButton(onClick = onClickSeeAll) {
            Text(text = "See all")
        }
    }
}

@Composable
fun FilterChips(
    filterItems: List<FilterItemUi>,
    onItemClick: (Int) -> Unit
) {
    LazyRow {
        items(filterItems) { item ->
            Box(
                modifier = Modifier
                    .padding(4.dp)
                    .then(
                        if (item.isSelected) {
                            Modifier.background(
                                Color.Black,
                                RoundedCornerShape(8.dp)
                            )
                        } else {
                            Modifier.border(
                                width = 1.dp,
                                color = Color.Black,
                                shape = RoundedCornerShape(8.dp)
                            )
                        }
                    )
                    .clickable { onItemClick(item.id) }
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = item.label,
                    color = if (item.isSelected) Color.White else Color.Black
                )
            }
        }
    }
}

@Composable
fun EmptyState() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 40.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.SearchOff, // or Error / Info
            contentDescription = "No Data",
            modifier = Modifier.size(80.dp),
            tint = Color.Gray
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "No data found",
            color = Color.Gray
        )
    }
}

@Composable
fun PeopleGrid(
    persons: List<PersonUi>,
    onPersonCardClick: (Int) -> Unit
) {
    if (persons.isEmpty()) {
        EmptyState()
    } else {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(0.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(top = 20.dp)
        ) {
            items(persons.size) { index ->
                PersonCard(
                    person = persons[index],
                    onClick = { onPersonCardClick(persons[index].id) }
                )
            }
        }
    }
}

@Composable
fun PersonCard(
    person: PersonUi,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(200.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() }
    ) {
        // Background Image
        AsyncImage(
            model = person.profileUrl,
            contentDescription = "${person.name} profile",
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
        )

        // Gradient overlay at bottom for text visibility
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .align(Alignment.BottomStart)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color(0xAA000000))
                    )
                )
        )

        // Texts
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(12.dp)
        ) {
            Text(
                text = person.name,
                color = Color.White,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = person.department,
                color = Color.White,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
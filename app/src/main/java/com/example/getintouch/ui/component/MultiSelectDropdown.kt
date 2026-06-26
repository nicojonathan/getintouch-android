package com.example.getintouch.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuDefaults
//import androidx.compose.material3.ExposedDropdownMenu
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.getintouch.ui.model.HobbyUi

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MultiSelectDropdown(
    itemTextColor: Color = Color.White,
    selectedTextColor: Color = Color.Black,
    borderColor: Color = Color.Black,
    labelColor: Color = Color.Black,
    hobbies: List<HobbyUi>,
    selectedHobbies: List<HobbyUi>,
    onSelectionChange: (List<HobbyUi>) -> Unit,
    modifier: Modifier
) {

    var expanded by remember {
        mutableStateOf(false)
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        },
        modifier = modifier
    ) {

        OutlinedTextField(
            value = selectedHobbies.joinToString {
                it.name
            },
            onValueChange = {},
            readOnly = true,
            label = {
                Text("Hobbies")
            },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )
            },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = selectedTextColor,
                unfocusedTextColor = selectedTextColor,

                focusedBorderColor = borderColor,
                unfocusedBorderColor = borderColor,

                focusedLabelColor = labelColor,
                unfocusedLabelColor = labelColor,
            ),
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            },
            modifier = Modifier
                .background(Color.Black)
        ) {

            hobbies.forEach { hobby ->

                val isSelected = hobby in selectedHobbies

                DropdownMenuItem(
                    text = {
                        Text(hobby.name)
                    },
                    colors = MenuDefaults.itemColors(
                        textColor = itemTextColor
                    ),
                    leadingIcon = {
                        Checkbox(
                            checked = isSelected,
                            onCheckedChange = null
                        )
                    },
                    onClick = {

                        val updatedSelection =
                            if (isSelected) {
                                selectedHobbies - hobby
                            } else {
                                selectedHobbies + hobby
                            }

                        onSelectionChange(updatedSelection)
                    }
                )
            }
        }
    }
}
package com.example.getintouch.ui.mapper

import com.example.getintouch.ui.model.DepartmentUi
import com.example.getintouch.ui.model.FilterItemUi
import com.example.getintouch.ui.model.HobbyUi

fun HobbyUi.toFilterItem() = FilterItemUi(
    id = id,
    label = name,
    isSelected = isSelected
)

fun DepartmentUi.toFilterItem() = FilterItemUi(
    id = id,
    label = name,
    isSelected = isSelected
)
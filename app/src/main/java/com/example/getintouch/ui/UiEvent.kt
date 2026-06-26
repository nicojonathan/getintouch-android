package com.example.getintouch.ui

sealed interface UiEvent {
    data class ShowToast(val message: String) : UiEvent
}
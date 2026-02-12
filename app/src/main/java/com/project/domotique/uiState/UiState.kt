package com.project.domotique.uiState


data class  UiState<T> (
    val loading: Boolean = false,
    val data: T? = null,
    val success: Boolean = false,
    val errors: String? = null
)



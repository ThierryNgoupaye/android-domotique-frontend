package com.project.domotique.features.auth.data.models

data class AuthRequest (
    val login: String,
    private val password: String
)
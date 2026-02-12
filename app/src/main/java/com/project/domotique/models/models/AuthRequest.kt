package com.project.domotique.models.models

data class AuthRequest (
    val login: String,
    private val password: String
)
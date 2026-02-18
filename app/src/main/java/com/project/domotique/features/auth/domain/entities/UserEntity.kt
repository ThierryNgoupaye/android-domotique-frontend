package com.project.domotique.features.auth.domain.entities

data class UserEntity(
    val userLogin: String,
    val owner: Int
)
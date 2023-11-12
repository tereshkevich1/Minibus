package com.example.minibus.models

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val phoneNumber : String,
    val role: Boolean
)
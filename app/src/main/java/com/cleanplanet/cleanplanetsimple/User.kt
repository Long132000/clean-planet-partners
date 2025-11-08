package com.cleanplanet.simple

import java.io.Serializable

data class User(
    val id: Int = 0,
    val login: String,
    val password: String,
    val role: String, // "admin", "manager", "partner"
    val fullName: String,
    val email: String,
    val phone: String,
    val partnerId: Int = 0, // для роли partner
    val isActive: Boolean = true
) : Serializable

enum class UserRole {
    ADMIN, MANAGER, PARTNER
}
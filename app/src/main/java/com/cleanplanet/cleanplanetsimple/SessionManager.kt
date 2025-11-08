package com.cleanplanet.simple

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("UserSession", Context.MODE_PRIVATE)

    fun saveUser(user: User) {
        with(prefs.edit()) {
            putInt("user_id", user.id)
            putString("user_login", user.login)
            putString("user_role", user.role)
            putString("user_name", user.fullName)
            putString("user_email", user.email)
            putString("user_phone", user.phone)
            putInt("partner_id", user.partnerId)
            apply()
        }
    }

    fun getCurrentUser(): User? {
        val id = prefs.getInt("user_id", -1)
        if (id == -1) return null

        return User(
            id = id,
            login = prefs.getString("user_login", "") ?: "",
            password = "", // не храним пароль
            role = prefs.getString("user_role", "") ?: "",
            fullName = prefs.getString("user_name", "") ?: "",
            email = prefs.getString("user_email", "") ?: "",
            phone = prefs.getString("user_phone", "") ?: "",
            partnerId = prefs.getInt("partner_id", 0)
        )
    }

    fun logout() {
        prefs.edit().clear().apply()
    }

    fun isLoggedIn(): Boolean = getCurrentUser() != null
    fun getUserRole(): String = getCurrentUser()?.role ?: ""
    fun getUserId(): Int = getCurrentUser()?.id ?: -1
}
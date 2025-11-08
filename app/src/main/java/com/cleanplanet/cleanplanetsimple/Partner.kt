package com.cleanplanet.simple

import java.io.Serializable

data class Partner(
    val id: Int = 0,
    val name: String,
    val type: String,
    val director: String,
    val phone: String,
    val email: String,
    val rating: Int,
    val address: String = "",
    val inn: String = ""
) : Serializable
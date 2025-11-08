package com.cleanplanet.simple

import java.io.Serializable

data class Order(
    val id: Int = 0,
    val partnerId: Int,
    val serviceId: Int,
    val quantity: Int,
    val status: String, // "pending", "confirmed", "completed", "cancelled"
    val orderDate: String,
    val completionDate: String? = null,
    val totalPrice: Double,
    val notes: String = ""
) : Serializable

data class SupplyOrder(
    val id: Int = 0,
    val supplierId: Int,
    val materialId: Int,
    val quantity: Int,
    val unitPrice: Double,
    val totalCost: Double,
    val orderDate: String,
    val expectedDate: String,
    val status: String, // "ordered", "delivered", "cancelled"
    val orderedBy: Int // userId менеджера
) : Serializable
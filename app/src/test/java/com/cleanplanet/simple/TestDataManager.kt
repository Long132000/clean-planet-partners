package com.cleanplanet.simple

// Упрощенная версия DataManager для тестов
class TestDataManager {

    fun isValidEmail(email: String): Boolean {
        return email.contains("@") && email.contains(".")
    }

    fun isValidPhone(phone: String): Boolean {
        return phone.startsWith("+7") && phone.length == 12
    }

    fun isValidRating(rating: Int): Boolean {
        return rating in 0..10
    }

    fun getPartners(): List<Partner> {
        return listOf(
            Partner(1, "Тестовый партнер 1", "Розничный пункт", "Директор 1", "+79991112233", "test1@test.com", 8),
            Partner(2, "Тестовый партнер 2", "Корпоративный клиент", "Директор 2", "+79992223344", "test2@test.com", 9)
        )
    }

    fun getServiceHistory(): List<ServiceRecord> {
        return listOf(
            ServiceRecord("2024-01-15", "Тестовый партнер", "Стирка", 5, 2500.0)
        )
    }

    fun searchPartners(query: String): List<Partner> {
        return if (query.isNotEmpty()) {
            getPartners().filter { it.name.contains(query, ignoreCase = true) }
        } else {
            getPartners()
        }
    }

    fun authenticate(login: String, password: String, role: String): User? {
        return if (login == "admin" && password == "admin" && role == "admin") {
            User(1, "admin", "admin", "admin", "Администратор", "admin@test.com", "+79990000000")
        } else {
            null
        }
    }

    fun addPartner(partner: Partner): Boolean {
        return partner.name.isNotEmpty() && partner.phone.isNotEmpty() && partner.email.isNotEmpty()
    }
}
package com.cleanplanet.simple

import org.junit.Test
import org.junit.Assert.*

class DataManagerTest {

    private val dataManager = TestDataManager()

    @Test
    fun isValidEmail_WithValidEmail_ReturnsTrue() {
        // Act & Assert
        assertTrue("✅ Валидация email выполнена успешно",
            dataManager.isValidEmail("test@test.com"))
    }

    @Test
    fun isValidPhone_WithValidPhone_ReturnsTrue() {
        // Act & Assert
        assertTrue("✅ Валидация телефона выполнена успешно",
            dataManager.isValidPhone("+79991234567"))
    }

    @Test
    fun isValidRating_WithValidRating_ReturnsTrue() {
        // Act & Assert
        assertTrue("✅ Валидация рейтинга выполнена успешно",
            dataManager.isValidRating(5))
    }

    @Test
    fun getPartners_ReturnsNonEmptyList() {
        // Act
        val partners = dataManager.getPartners()

        // Assert
        assertTrue("✅ Получение списка партнеров выполнено успешно",
            partners.isNotEmpty())
    }

    @Test
    fun getServiceHistory_ReturnsNonEmptyList() {
        // Act
        val history = dataManager.getServiceHistory()

        // Assert
        assertTrue("✅ Получение истории услуг выполнено успешно",
            history.isNotEmpty())
    }

    @Test
    fun searchPartners_WithExistingQuery_ReturnsResults() {
        // Act
        val partners = dataManager.searchPartners("Тестовый")

        // Assert
        assertTrue("✅ Поиск партнеров выполнен успешно",
            partners.isNotEmpty())
    }

    @Test
    fun authenticate_WithCorrectCredentials_ReturnsUser() {
        // Act
        val user = dataManager.authenticate("admin", "admin", "admin")

        // Assert
        assertNotNull("✅ Аутентификация выполнена успешно", user)
    }

    @Test
    fun addPartner_WithValidData_ReturnsTrue() {
        // Arrange
        val partner = Partner(
            name = "Тестовый партнер",
            type = "Розничный пункт",
            director = "Тестовый директор",
            phone = "+79991234567",
            email = "test@test.com",
            rating = 8
        )

        // Act
        val result = dataManager.addPartner(partner)

        // Assert
        assertTrue("✅ Добавление партнера выполнено успешно", result)
    }

    @Test
    fun isValidEmail_WithInvalidEmail_ReturnsFalse() {
        // Act & Assert
        assertFalse("✅ Валидация невалидного email выполнена успешно",
            dataManager.isValidEmail("invalid-email"))
    }

    @Test
    fun isValidPhone_WithInvalidPhone_ReturnsFalse() {
        // Act & Assert
        assertFalse("✅ Валидация невалидного телефона выполнена успешно",
            dataManager.isValidPhone("123"))
    }

    @Test
    fun searchPartners_WithNonExistingQuery_ReturnsEmptyList() {
        // Act
        val partners = dataManager.searchPartners("НесуществующийПартнер")

        // Assert
        assertTrue("✅ Поиск несуществующего партнера выполнен успешно",
            partners.isEmpty())
    }
}
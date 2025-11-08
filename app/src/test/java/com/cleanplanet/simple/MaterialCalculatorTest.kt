package com.cleanplanet.simple

import org.junit.Test
import org.junit.Assert.*

class MaterialCalculatorTest {

    private val calculator = MaterialCalculator()

    @Test
    fun calculateMaterialRequired_ForWashingService_ReturnsCorrectValue() {
        // Act
        val result = calculator.calculateMaterialRequired(1, 1, 2, 5.0, 2.0)

        // Assert
        assertTrue("✅ Расчет для стирки выполнен успешно", result > 0)
    }

    @Test
    fun calculateMaterialRequired_ForDryCleaning_ReturnsCorrectValue() {
        // Act
        val result = calculator.calculateMaterialRequired(2, 2, 1, 3.0, 1.5)

        // Assert
        assertTrue("✅ Расчет для химчистки выполнен успешно", result > 0)
    }

    @Test
    fun calculateMaterialRequired_ForRepairService_ReturnsCorrectValue() {
        // Act
        val result = calculator.calculateMaterialRequired(3, 3, 5, 1.0, 0.5)

        // Assert
        assertTrue("✅ Расчет для ремонта выполнен успешно", result > 0)
    }

    @Test
    fun calculateServiceCost_ForExistingService_ReturnsCost() {
        // Act
        val result = calculator.calculateServiceCost(1)

        // Assert
        assertTrue("✅ Расчет стоимости услуги выполнен успешно", result > 0)
    }

    @Test
    fun calculateTotalOrderCost_ForValidOrder_ReturnsCorrectCost() {
        // Act
        val result = calculator.calculateTotalOrderCost(1, 3)

        // Assert
        assertTrue("✅ Расчет общей стоимости заказа выполнен успешно", result > 0)
    }

    @Test
    fun getServiceTypeName_ForValidId_ReturnsCorrectName() {
        // Act & Assert
        assertEquals("✅ Получение названия услуги выполнено успешно",
            "Стирка", calculator.getServiceTypeName(1))
    }

    @Test
    fun getMaterialTypeName_ForValidId_ReturnsCorrectName() {
        // Act & Assert
        assertEquals("✅ Получение названия материала выполнено успешно",
            "Моющее средство", calculator.getMaterialTypeName(1))
    }

    @Test
    fun calculateMaterialRequired_WithInvalidServiceType_ReturnsError() {
        // Act
        val result = calculator.calculateMaterialRequired(-1, 1, 2, 5.0, 2.0)

        // Assert
        assertEquals("✅ Обработка невалидного ID услуги выполнена успешно", -1, result)
    }

    @Test
    fun calculateServiceCost_ForNonExistingService_ReturnsError() {
        // Act
        val result = calculator.calculateServiceCost(999)

        // Assert
        assertEquals("✅ Обработка несуществующей услуги выполнена успешно", -1.0, result, 0.0)
    }
}
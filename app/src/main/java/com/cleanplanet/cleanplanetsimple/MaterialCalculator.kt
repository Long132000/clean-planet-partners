package com.cleanplanet.simple

import kotlin.math.ceil

/**
 * Класс для расчета материалов - МОДУЛЬ 4
 * Отвечает за расчет необходимого количества материалов для услуг
 */
class MaterialCalculator {

    /**
     * Расчет необходимого количества материалов
     * @param serviceTypeId ID типа услуги (1-Стирка, 2-Химчистка, 3-Ремонт)
     * @param materialTypeId ID типа материала (1-Моющее средство, 2-Растворитель, 3-Упаковка, 4-Отбеливатель)
     * @param serviceCount количество услуг
     * @param weight вес изделия (кг)
     * @param area площадь загрязнения (м²)
     * @return необходимое количество материалов или -1 при ошибке
     */
    fun calculateMaterialRequired(
        serviceTypeId: Int,
        materialTypeId: Int,
        serviceCount: Int,
        weight: Double,
        area: Double
    ): Int {
        // Валидация входных параметров
        if (serviceTypeId <= 0 || materialTypeId <= 0 ||
            serviceCount <= 0 || weight <= 0.0 || area <= 0.0) {
            return -1
        }

        // Коэффициенты сложности услуги
        val serviceCoefficients = mapOf(
            1 to 1.2, // Стирка - коэффициент 1.2
            2 to 1.5, // Химчистка - коэффициент 1.5
            3 to 1.1  // Ремонт - коэффициент 1.1
        )

        // Проценты перерасхода материалов
        val materialWastePercent = mapOf(
            1 to 0.10, // Моющие средства - 10% перерасход
            2 to 0.15, // Растворители - 15% перерасход
            3 to 0.05, // Упаковка - 5% перерасход
            4 to 0.08  // Отбеливатели - 8% перерасход
        )

        // Получение коэффициентов
        val serviceCoefficient = serviceCoefficients[serviceTypeId] ?: return -1
        val wastePercent = materialWastePercent[materialTypeId] ?: return -1

        return try {
            // Расчет базового количества на одну услугу
            val baseMaterialPerService = weight * area * serviceCoefficient

            // Расчет общего количества без перерасхода
            val totalMaterialWithoutWaste = baseMaterialPerService * serviceCount

            // Учет перерасхода материалов
            val wasteMultiplier = 1 + wastePercent
            val totalMaterialWithWaste = totalMaterialWithoutWaste * wasteMultiplier

            // Округление до целого в большую сторону
            ceil(totalMaterialWithWaste).toInt()

        } catch (e: Exception) {
            -1
        }
    }

    /**
     * Расчет себестоимости услуги по идентификатору
     * @param serviceId ID услуги (1-Стирка, 2-Химчистка, 3-Ремонт)
     * @return себестоимость услуги или -1 при ошибке
     */
    fun calculateServiceCost(serviceId: Int): Double {
        val serviceCosts = mapOf(
            1 to 350.0, // Стирка: материалы + трудозатраты
            2 to 850.0, // Химчистка: дорогие растворители
            3 to 150.0  // Ремонт: минимальные затраты
        )

        return serviceCosts[serviceId] ?: -1.0
    }

    /**
     * Расчет полной стоимости заказа
     */
    fun calculateTotalOrderCost(serviceId: Int, quantity: Int): Double {
        val cost = calculateServiceCost(serviceId)
        return if (cost != -1.0) {
            cost * quantity
        } else {
            -1.0
        }
    }

    /**
     * Получение названия типа услуги по ID
     */
    fun getServiceTypeName(serviceTypeId: Int): String {
        return when (serviceTypeId) {
            1 -> "Стирка"
            2 -> "Химчистка"
            3 -> "Ремонт"
            else -> "Неизвестно"
        }
    }

    /**
     * Получение названия типа материала по ID
     */
    fun getMaterialTypeName(materialTypeId: Int): String {
        return when (materialTypeId) {
            1 -> "Моющее средство"
            2 -> "Растворитель"
            3 -> "Упаковка"
            4 -> "Отбеливатель"
            else -> "Неизвестно"
        }
    }
}
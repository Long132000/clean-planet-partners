package com.cleanplanet.simple

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class CalculatorActivity : AppCompatActivity() {

    private lateinit var spinnerServiceType: Spinner
    private lateinit var spinnerMaterialType: Spinner
    private lateinit var editServiceCount: EditText
    private lateinit var editWeight: EditText
    private lateinit var editArea: EditText
    private lateinit var textResult: TextView
    private lateinit var buttonCalculate: Button

    private val calculator = MaterialCalculator()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculator)

        initViews()
        setupSpinners()
        setupCalculateButton()
    }

    private fun initViews() {
        spinnerServiceType = findViewById(R.id.spinnerServiceType)
        spinnerMaterialType = findViewById(R.id.spinnerMaterialType)
        editServiceCount = findViewById(R.id.editServiceCount)
        editWeight = findViewById(R.id.editWeight)
        editArea = findViewById(R.id.editArea)
        textResult = findViewById(R.id.textResult)
        buttonCalculate = findViewById(R.id.buttonCalculate)

        // Кнопка назад
        findViewById<ImageButton>(R.id.buttonBack).setOnClickListener {
            finish()
        }
    }

    private fun setupSpinners() {
        // Типы услуг
        val serviceTypes = arrayOf("Стирка", "Химчистка", "Ремонт")
        val serviceAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, serviceTypes)
        serviceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerServiceType.adapter = serviceAdapter

        // Типы материалов
        val materialTypes = arrayOf("Моющее средство", "Растворитель", "Упаковка", "Отбеливатель")
        val materialAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, materialTypes)
        materialAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerMaterialType.adapter = materialAdapter
    }

    private fun setupCalculateButton() {
        buttonCalculate.setOnClickListener {
            calculateMaterials()
        }
    }

    private fun calculateMaterials() {
        try {
            val serviceTypeId = spinnerServiceType.selectedItemPosition + 1
            val materialTypeId = spinnerMaterialType.selectedItemPosition + 1
            val serviceCount = editServiceCount.text.toString().toIntOrNull() ?: 1
            val weight = editWeight.text.toString().toDoubleOrNull() ?: 0.0
            val area = editArea.text.toString().toDoubleOrNull() ?: 0.0

            if (serviceCount <= 0 || weight <= 0.0 || area <= 0.0) {
                showError("Заполните все поля корректными значениями")
                return
            }

            val result = calculator.calculateMaterialRequired(
                serviceTypeId = serviceTypeId,
                materialTypeId = materialTypeId,
                serviceCount = serviceCount,
                weight = weight,
                area = area
            )

            if (result != -1) {
                val serviceName = calculator.getServiceTypeName(serviceTypeId)
                val materialName = calculator.getMaterialTypeName(materialTypeId)

                val resultText = """
                    📊 Результаты расчета:
                    
                    Услуга: $serviceName
                    Материал: $materialName
                    Количество услуг: $serviceCount
                    Вес: $weight кг
                    Площадь: $area м²
                    
                    🎯 Необходимо материалов: $result единиц
                    
                    Коэффициенты:
                    • Сложность услуги: ${getServiceCoefficient(serviceTypeId)}
                    • Перерасход материала: ${getWastePercent(materialTypeId)}%
                """.trimIndent()

                textResult.text = resultText
                textResult.setTextColor(getColor(android.R.color.holo_green_dark))
            } else {
                showError("Ошибка в расчетах. Проверьте введенные данные.")
            }

        } catch (e: Exception) {
            showError("Ошибка: ${e.message}")
        }
    }

    private fun getServiceCoefficient(serviceTypeId: Int): String {
        return when (serviceTypeId) {
            1 -> "1.2 (Стирка)"
            2 -> "1.5 (Химчистка)"
            3 -> "1.1 (Ремонт)"
            else -> "1.0"
        }
    }

    private fun getWastePercent(materialTypeId: Int): String {
        return when (materialTypeId) {
            1 -> "10"
            2 -> "15"
            3 -> "5"
            4 -> "8"
            else -> "0"
        }
    }

    private fun showError(message: String) {
        textResult.text = "❌ $message"
        textResult.setTextColor(getColor(android.R.color.holo_red_dark))
    }
}
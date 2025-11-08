package com.cleanplanet.simple

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class ExportActivity : AppCompatActivity() {

    private lateinit var spinnerExportFormat: Spinner
    private lateinit var spinnerDataSelection: Spinner
    private lateinit var textExportStatus: TextView
    private lateinit var buttonExport: Button
    private lateinit var dataManager: DataManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_export)

        dataManager = DataManager(this)

        initViews()
        setupSpinners()
        setupExportButton()
    }

    private fun initViews() {
        spinnerExportFormat = findViewById(R.id.spinnerExportFormat)
        spinnerDataSelection = findViewById(R.id.spinnerDataSelection)
        textExportStatus = findViewById(R.id.textExportStatus)
        buttonExport = findViewById(R.id.buttonExport)

        // Кнопка назад
        findViewById<ImageButton>(R.id.buttonBack).setOnClickListener {
            finish()
        }
    }

    private fun setupSpinners() {
        // Форматы экспорта
        val formats = arrayOf("CSV", "JSON", "XML")
        val formatAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, formats)
        formatAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerExportFormat.adapter = formatAdapter

        // Типы данных для экспорта
        val dataTypes = arrayOf("Партнеры", "История услуг", "Все данные")
        val dataAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, dataTypes)
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerDataSelection.adapter = dataAdapter
    }

    private fun setupExportButton() {
        buttonExport.setOnClickListener {
            exportData()
        }
    }

    private fun exportData() {
        val format = spinnerExportFormat.selectedItem.toString()
        val dataType = spinnerDataSelection.selectedItem.toString()

        textExportStatus.text = "⏳ Экспорт данных...\nФормат: $format\nТип: $dataType"

        // Имитация экспорта (в реальном приложении здесь была бы работа с файлами)
        Thread {
            Thread.sleep(2000) // Имитация долгой операции

            runOnUiThread {
                val success = when (dataType) {
                    "Партнеры" -> dataManager.exportPartnersToCsv()
                    else -> true // Для демонстрации
                }

                if (success) {
                    textExportStatus.text = """
                        ✅ Экспорт завершен успешно!
                        
                        Данные: $dataType
                        Формат: $format
                        Файл сохранен в папке приложения
                        
                        📊 Статистика:
                        • Партнеров: ${dataManager.getPartners().size}
                        • Записей истории: ${dataManager.getServiceHistory().size}
                    """.trimIndent()
                } else {
                    textExportStatus.text = "❌ Ошибка экспорта данных"
                }
            }
        }.start()
    }
}
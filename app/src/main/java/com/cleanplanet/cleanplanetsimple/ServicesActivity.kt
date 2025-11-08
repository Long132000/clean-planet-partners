package com.cleanplanet.simple

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ServicesActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var dataManager: DataManager
    private lateinit var servicesAdapter: ServicesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_services)

        dataManager = DataManager(this)

        initViews()
        setupRecyclerView()
        loadServices()
    }

    private fun initViews() {
        recyclerView = findViewById(R.id.recyclerViewServices)

        // Кнопка назад
        findViewById<ImageButton>(R.id.buttonBack).setOnClickListener {
            finish()
        }

        // Для партнеров показываем кнопку создания заказа
        val currentUser = dataManager.sessionManager.getCurrentUser()
        if (currentUser?.role == "partner") {
            findViewById<Button>(R.id.buttonCreateOrder).setOnClickListener {
                createNewOrder()
            }
        } else {
            findViewById<Button>(R.id.buttonCreateOrder).visibility = Button.GONE
        }
    }

    private fun setupRecyclerView() {
        servicesAdapter = ServicesAdapter { service ->
            // Обработка клика на услугу
            showServiceDetails(service)
        }

        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@ServicesActivity)
            adapter = servicesAdapter
        }
    }

    private fun loadServices() {
        val services = dataManager.getServices()
        servicesAdapter.submitList(services)

        if (services.isEmpty()) {
            Toast.makeText(this, "Список услуг пуст", Toast.LENGTH_LONG).show()
        }
    }

    private fun showServiceDetails(service: Service) {
        val message = """
            🛠️ Услуга: ${service.name}
            
            Код: ${service.code}
            Тип: ${service.type}
            Описание: ${service.description}
            Цена: ${service.price} руб.
            Норма времени: ${service.timeNorm} ч.
            Цех: ${service.workshop}
            Персонал: ${service.staffCount} чел.
        """.trimIndent()

        android.app.AlertDialog.Builder(this)
            .setTitle("Детали услуги")
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show()
    }

    private fun createNewOrder() {
        // Реализация создания заказа
        val intent = Intent(this, CreateOrderActivity::class.java)
        startActivity(intent)
    }
}
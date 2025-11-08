package com.cleanplanet.simple

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var dataManager: DataManager
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dataManager = DataManager(this)
        sessionManager = SessionManager(this)

        // Проверка авторизации
        if (!sessionManager.isLoggedIn()) {
            navigateToLogin()
            return
        }

        setupUI()
    }

    private fun setupUI() {
        val currentUser = sessionManager.getCurrentUser() ?: return

        // Установка приветствия
        val textGreeting = findViewById<TextView>(R.id.textGreeting)
        textGreeting.text = "Добро пожаловать, ${currentUser.fullName}!"

        // Обновление информации о роли
        val textCurrentRole = findViewById<TextView>(R.id.textCurrentRole)
        textCurrentRole.text = "✅ Текущая роль: ${getRoleName(currentUser.role)}"

        // Находим все кнопки
        val buttonPartners = findViewById<Button>(R.id.buttonPartners)
        val buttonAddPartner = findViewById<Button>(R.id.buttonAddPartner)
        val buttonSuppliers = findViewById<Button>(R.id.buttonSuppliers)
        val buttonCalculator = findViewById<Button>(R.id.buttonCalculator)
        val buttonHistory = findViewById<Button>(R.id.buttonHistory)
        val buttonExport = findViewById<Button>(R.id.buttonExport)
        val buttonServices = findViewById<Button>(R.id.buttonServices)
        val buttonOrders = findViewById<Button>(R.id.buttonOrders)
        val buttonSupplyOrders = findViewById<Button>(R.id.buttonSupplyOrders)
        val buttonUsers = findViewById<Button>(R.id.buttonUsers)
        val buttonLogout = findViewById<Button>(R.id.buttonLogout)

        // Показываем/скрываем кнопки в зависимости от роли
        when (currentUser.role) {
            "admin" -> {
                // Админ видит всё
                buttonPartners.visibility = Button.VISIBLE
                buttonAddPartner.visibility = Button.VISIBLE
                buttonSuppliers.visibility = Button.VISIBLE
                buttonCalculator.visibility = Button.VISIBLE
                buttonHistory.visibility = Button.VISIBLE
                buttonExport.visibility = Button.VISIBLE
                buttonServices.visibility = Button.VISIBLE
                buttonOrders.visibility = Button.VISIBLE
                buttonSupplyOrders.visibility = Button.VISIBLE
                buttonUsers.visibility = Button.VISIBLE
            }
            "manager" -> {
                // Менеджер видит управление партнерами и поставками
                buttonPartners.visibility = Button.VISIBLE
                buttonAddPartner.visibility = Button.VISIBLE
                buttonSuppliers.visibility = Button.VISIBLE
                buttonCalculator.visibility = Button.VISIBLE
                buttonHistory.visibility = Button.VISIBLE
                buttonExport.visibility = Button.VISIBLE
                buttonServices.visibility = Button.VISIBLE
                buttonOrders.visibility = Button.VISIBLE
                buttonSupplyOrders.visibility = Button.VISIBLE
                buttonUsers.visibility = Button.GONE
            }
            "partner" -> {
                // Партнер видит только услуги и свои заказы
                buttonPartners.visibility = Button.GONE
                buttonAddPartner.visibility = Button.GONE
                buttonSuppliers.visibility = Button.GONE
                buttonCalculator.visibility = Button.GONE
                buttonHistory.visibility = Button.VISIBLE
                buttonExport.visibility = Button.GONE
                buttonServices.visibility = Button.VISIBLE
                buttonOrders.visibility = Button.VISIBLE
                buttonSupplyOrders.visibility = Button.GONE
                buttonUsers.visibility = Button.GONE
            }
        }

        // Обработчики навигации
        buttonPartners.setOnClickListener {
            val intent = Intent(this, PartnersListActivity::class.java)
            startActivity(intent)
        }

        buttonAddPartner.setOnClickListener {
            val intent = Intent(this, PartnerEditActivity::class.java)
            startActivity(intent)
        }

        buttonSuppliers.setOnClickListener {
            val intent = Intent(this, SuppliersListActivity::class.java)
            startActivity(intent)
        }

        buttonCalculator.setOnClickListener {
            val intent = Intent(this, CalculatorActivity::class.java)
            startActivity(intent)
        }

        buttonHistory.setOnClickListener {
            val intent = Intent(this, ServiceHistoryActivity::class.java)
            startActivity(intent)
        }

        buttonExport.setOnClickListener {
            val intent = Intent(this, ExportActivity::class.java)
            startActivity(intent)
        }

        buttonServices.setOnClickListener {
            val intent = Intent(this, ServicesActivity::class.java)
            startActivity(intent)
        }

        buttonOrders.setOnClickListener {
            when (currentUser.role) {
                "partner" -> {
                    val intent = Intent(this, PartnerOrdersActivity::class.java)
                    startActivity(intent)
                }
                else -> {
                    val intent = Intent(this, OrdersManagementActivity::class.java)
                    startActivity(intent)
                }
            }
        }

        buttonSupplyOrders.setOnClickListener {
            val intent = Intent(this, SupplyOrdersActivity::class.java)
            startActivity(intent)
        }

        buttonUsers.setOnClickListener {
            val intent = Intent(this, UsersManagementActivity::class.java)
            startActivity(intent)
        }

        // Обработчик кнопки выхода
        buttonLogout.setOnClickListener {
            logout()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_logout -> {
                logout()
                true
            }
            R.id.menu_profile -> {
                showProfile()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun logout() {
        sessionManager.logout()
        navigateToLogin()
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showProfile() {
        val currentUser = sessionManager.getCurrentUser()
        val message = """
            👤 Профиль:
            
            Имя: ${currentUser?.fullName}
            Логин: ${currentUser?.login}
            Роль: ${getRoleName(currentUser?.role ?: "")}
            Email: ${currentUser?.email}
            Телефон: ${currentUser?.phone}
        """.trimIndent()

        android.app.AlertDialog.Builder(this)
            .setTitle("Профиль пользователя")
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show()
    }

    private fun getRoleName(role: String): String {
        return when (role) {
            "admin" -> "Администратор"
            "manager" -> "Менеджер"
            "partner" -> "Партнер"
            else -> "Неизвестно"
        }
    }
}
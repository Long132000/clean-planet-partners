package com.cleanplanet.simple

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var editLogin: EditText
    private lateinit var editPassword: EditText
    private lateinit var buttonLogin: Button
    private lateinit var spinnerRole: Spinner
    private lateinit var dataManager: DataManager
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        dataManager = DataManager(this)
        sessionManager = SessionManager(this)

        // Если уже авторизован - переходим сразу
        if (sessionManager.isLoggedIn()) {
            navigateToMain()
            return
        }

        initViews()
        setupLoginButton()
    }

    private fun initViews() {
        editLogin = findViewById(R.id.editLogin)
        editPassword = findViewById(R.id.editPassword)
        buttonLogin = findViewById(R.id.buttonLogin)
        spinnerRole = findViewById(R.id.spinnerRole)

        // Настройка спиннера ролей
        val roles = arrayOf("Администратор", "Менеджер", "Партнер")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, roles)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerRole.adapter = adapter
    }

    private fun setupLoginButton() {
        buttonLogin.setOnClickListener {
            val login = editLogin.text.toString().trim()
            val password = editPassword.text.toString().trim()
            val role = when (spinnerRole.selectedItemPosition) {
                0 -> "admin"
                1 -> "manager"
                2 -> "partner"
                else -> "partner"
            }

            if (login.isEmpty() || password.isEmpty()) {
                showError("Заполните все поля")
                return@setOnClickListener
            }

            val user = dataManager.authenticate(login, password, role)
            if (user != null) {
                sessionManager.saveUser(user)
                navigateToMain()
                Toast.makeText(this, "Добро пожаловать, ${user.fullName}!", Toast.LENGTH_SHORT).show()
            } else {
                showError("Неверные учетные данные")
            }
        }
    }

    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showError(message: String) {
        Toast.makeText(this, "❌ $message", Toast.LENGTH_LONG).show()
    }
}
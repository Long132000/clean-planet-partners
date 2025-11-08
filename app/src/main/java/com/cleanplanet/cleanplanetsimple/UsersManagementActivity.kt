package com.cleanplanet.simple

import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class UsersManagementActivity : AppCompatActivity() {

    private lateinit var dataManager: DataManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users_management)

        dataManager = DataManager(this)

        // Кнопка назад
        findViewById<ImageButton>(R.id.buttonBack).setOnClickListener {
            finish()
        }

        // Заголовок
        findViewById<TextView>(R.id.textTitle).text = "👥 Управление пользователями"

        Toast.makeText(this, "Экран управления пользователями", Toast.LENGTH_SHORT).show()
    }
}
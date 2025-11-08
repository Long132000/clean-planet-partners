package com.cleanplanet.simple

import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class OrdersManagementActivity : AppCompatActivity() {

    private lateinit var dataManager: DataManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orders_management)

        dataManager = DataManager(this)

        // Кнопка назад
        findViewById<ImageButton>(R.id.buttonBack).setOnClickListener {
            finish()
        }

        // Заголовок
        findViewById<TextView>(R.id.textTitle).text = "📦 Управление заказами"

        Toast.makeText(this, "Экран управления заказами", Toast.LENGTH_SHORT).show()
    }
}
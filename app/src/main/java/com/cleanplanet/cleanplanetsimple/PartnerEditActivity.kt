package com.cleanplanet.simple

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class PartnerEditActivity : AppCompatActivity() {

    private lateinit var dataManager: DataManager
    private var editingPartner: Partner? = null

    // UI элементы
    private lateinit var editName: EditText
    private lateinit var spinnerType: Spinner
    private lateinit var editDirector: EditText
    private lateinit var editPhone: EditText
    private lateinit var editEmail: EditText
    private lateinit var ratingBar: RatingBar
    private lateinit var editAddress: EditText
    private lateinit var editInn: EditText
    private lateinit var textRatingValue: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_partner_edit)

        dataManager = DataManager(this)

        initViews()
        loadPartnerData()
        setupSaveButton()
    }

    private fun initViews() {
        // Находим все View элементы
        editName = findViewById(R.id.editPartnerName)
        spinnerType = findViewById(R.id.spinnerPartnerType)
        editDirector = findViewById(R.id.editPartnerDirector)
        editPhone = findViewById(R.id.editPartnerPhone)
        editEmail = findViewById(R.id.editPartnerEmail)
        ratingBar = findViewById(R.id.ratingBar)
        editAddress = findViewById(R.id.editPartnerAddress)
        editInn = findViewById(R.id.editPartnerInn)
        textRatingValue = findViewById(R.id.textRatingValue)

        // Настройка спиннера типов
        val types = arrayOf("Розничный пункт", "Корпоративный клиент", "Интернет-агрегатор")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, types)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerType.adapter = adapter

        // Настройка рейтинга
        ratingBar.setOnRatingBarChangeListener { _, rating, _ ->
            textRatingValue.text = "Рейтинг: ${rating.toInt()}/10"
        }

        // Кнопка назад
        findViewById<ImageButton>(R.id.buttonBack).setOnClickListener {
            finish()
        }
    }

    private fun loadPartnerData() {
        val partnerId = intent.getIntExtra("partner_id", -1)
        if (partnerId != -1) {
            editingPartner = dataManager.getPartnerById(partnerId)
            editingPartner?.let { partner ->
                fillFormWithPartnerData(partner)
                // Меняем заголовок на "Редактирование"
                findViewById<TextView>(R.id.textTitle).text = "Редактирование партнера"
            }
        }
    }

    private fun fillFormWithPartnerData(partner: Partner) {
        editName.setText(partner.name)

        // Устанавливаем тип в спиннере
        val typePosition = when (partner.type) {
            "Розничный пункт" -> 0
            "Корпоративный клиент" -> 1
            "Интернет-агрегатор" -> 2
            else -> 0
        }
        spinnerType.setSelection(typePosition)

        editDirector.setText(partner.director)
        editPhone.setText(partner.phone)
        editEmail.setText(partner.email)
        ratingBar.rating = partner.rating.toFloat()
        editAddress.setText(partner.address)
        editInn.setText(partner.inn)
    }

    private fun setupSaveButton() {
        findViewById<Button>(R.id.buttonSave).setOnClickListener {
            if (validateForm()) {
                savePartner()
            }
        }
    }

    private fun validateForm(): Boolean {
        val name = editName.text.toString().trim()
        val phone = editPhone.text.toString().trim()
        val email = editEmail.text.toString().trim()
        val rating = ratingBar.rating.toInt()

        if (name.isEmpty()) {
            showError("Введите наименование партнера")
            return false
        }

        if (!dataManager.isValidPhone(phone)) {
            showError("Неверный формат телефона. Должен быть: +7XXXXXXXXXX")
            return false
        }

        if (!dataManager.isValidEmail(email)) {
            showError("Неверный формат email")
            return false
        }

        if (!dataManager.isValidRating(rating)) {
            showError("Рейтинг должен быть от 0 до 10")
            return false
        }

        return true
    }

    private fun savePartner() {
        val partner = Partner(
            id = editingPartner?.id ?: 0,
            name = editName.text.toString().trim(),
            type = spinnerType.selectedItem.toString(),
            director = editDirector.text.toString().trim(),
            phone = editPhone.text.toString().trim(),
            email = editEmail.text.toString().trim(),
            rating = ratingBar.rating.toInt(),
            address = editAddress.text.toString().trim(),
            inn = editInn.text.toString().trim()
        )

        val success = if (editingPartner != null) {
            dataManager.updatePartner(partner.id, partner)
        } else {
            dataManager.addPartner(partner)
        }

        if (success) {
            val message = if (editingPartner != null) "Партнер обновлен" else "Партнер добавлен"
            Toast.makeText(this, "✅ $message", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, "❌ Ошибка сохранения", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showError(message: String) {
        Toast.makeText(this, "❌ $message", Toast.LENGTH_LONG).show()
    }
}
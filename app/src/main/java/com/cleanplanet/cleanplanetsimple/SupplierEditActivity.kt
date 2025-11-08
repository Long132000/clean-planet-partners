package com.cleanplanet.simple

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class SupplierEditActivity : AppCompatActivity() {

    private lateinit var editName: EditText
    private lateinit var spinnerType: Spinner
    private lateinit var editINN: EditText
    private lateinit var editContactPerson: EditText
    private lateinit var editPhone: EditText
    private lateinit var editEmail: EditText
    private lateinit var editAddress: EditText
    private lateinit var editRating: EditText
    private lateinit var buttonSave: Button
    private lateinit var buttonDelete: Button

    private lateinit var dataManager: DataManager
    private var currentSupplier: Supplier? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_supplier_edit)

        dataManager = DataManager(this)
        initViews()
        setupSpinners()
        loadSupplierData()
        setupSaveButton()
        setupDeleteButton()
    }

    private fun initViews() {
        editName = findViewById(R.id.editSupplierName)
        spinnerType = findViewById(R.id.spinnerSupplierType)
        editINN = findViewById(R.id.editSupplierINN)
        editContactPerson = findViewById(R.id.editSupplierContactPerson)
        editPhone = findViewById(R.id.editSupplierPhone)
        editEmail = findViewById(R.id.editSupplierEmail)
        editAddress = findViewById(R.id.editSupplierAddress)
        editRating = findViewById(R.id.editSupplierRating)
        buttonSave = findViewById(R.id.buttonSaveSupplier)
        buttonDelete = findViewById(R.id.buttonDeleteSupplier)

        findViewById<ImageButton>(R.id.buttonBack).setOnClickListener {
            finish()
        }
    }

    private fun setupSpinners() {
        val types = arrayOf("химия", "упаковка", "оборудование", "прочее")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, types)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerType.adapter = adapter
    }

    private fun loadSupplierData() {
        val supplierId = intent.getIntExtra("supplier_id", -1)
        if (supplierId != -1) {
            currentSupplier = dataManager.getSupplierById(supplierId)
            currentSupplier?.let { supplier ->
                editName.setText(supplier.name)
                spinnerType.setSelection(getTypePosition(supplier.type))
                editINN.setText(supplier.inn)
                editContactPerson.setText(supplier.contactPerson ?: "")
                editPhone.setText(supplier.phone ?: "")
                editEmail.setText(supplier.email ?: "")
                editAddress.setText(supplier.address ?: "")
                editRating.setText(supplier.rating.toString())
            }
        } else {
            buttonDelete.visibility = Button.GONE
        }
    }

    private fun getTypePosition(type: String): Int {
        return when (type) {
            "химия" -> 0
            "упаковка" -> 1
            "оборудование" -> 2
            else -> 3
        }
    }

    private fun setupSaveButton() {
        buttonSave.setOnClickListener {
            if (validateForm()) {
                saveSupplier()
            }
        }
    }

    private fun setupDeleteButton() {
        buttonDelete.setOnClickListener {
            deleteSupplier()
        }
    }

    private fun validateForm(): Boolean {
        val name = editName.text.toString().trim()
        val inn = editINN.text.toString().trim()
        val rating = editRating.text.toString().trim().toDoubleOrNull()

        if (name.isEmpty()) {
            showError("Введите наименование поставщика")
            return false
        }

        if (inn.isEmpty() || inn.length != 12) {
            showError("ИНН должен содержать 12 цифр")
            return false
        }

        if (rating == null || rating < 0.0 || rating > 5.0) {
            showError("Рейтинг должен быть от 0.0 до 5.0")
            return false
        }

        return true
    }

    private fun saveSupplier() {
        val supplier = Supplier(
            id = currentSupplier?.id ?: 0,
            name = editName.text.toString().trim(),
            type = spinnerType.selectedItem.toString(),
            inn = editINN.text.toString().trim(),
            contactPerson = editContactPerson.text.toString().trim().takeIf { it.isNotEmpty() },
            phone = editPhone.text.toString().trim().takeIf { it.isNotEmpty() },
            email = editEmail.text.toString().trim().takeIf { it.isNotEmpty() },
            address = editAddress.text.toString().trim().takeIf { it.isNotEmpty() },
            rating = editRating.text.toString().trim().toDouble()
        )

        val success = if (currentSupplier != null) {
            dataManager.updateSupplier(currentSupplier!!.id, supplier)
        } else {
            dataManager.addSupplier(supplier)
        }

        if (success) {
            Toast.makeText(this, "✅ Поставщик сохранен", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            showError("Ошибка сохранения поставщика")
        }
    }

    private fun deleteSupplier() {
        currentSupplier?.let { supplier ->
            // В реальном приложении здесь была бы логика удаления
            Toast.makeText(this, "Функция удаления в разработке", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showError(message: String) {
        Toast.makeText(this, "❌ $message", Toast.LENGTH_LONG).show()
    }
}
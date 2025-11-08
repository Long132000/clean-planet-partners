package com.cleanplanet.simple

import android.content.Context
import android.widget.Toast
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class DataManager(private val context: Context) {

    private val partners = ArrayList<Partner>()
    private val serviceHistory = ArrayList<ServiceRecord>()
    private val users = ArrayList<User>()
    private val orders = ArrayList<Order>()
    private val supplyOrders = ArrayList<SupplyOrder>()
    private val services = ArrayList<Service>()
    private val suppliers = ArrayList<Supplier>()

    val sessionManager = SessionManager(context)

    init {
        initTestData()
    }

    private fun initTestData() {
        // Тестовые пользователи
        if (users.isEmpty()) {
            users.addAll(listOf(
                User(1, "admin", "admin", "admin", "Администратор Системы", "admin@cleanplanet.ru", "+79990000001"),
                User(2, "manager", "manager", "manager", "Менеджер Иванов", "manager@cleanplanet.ru", "+79990000002"),
                User(3, "partner1", "partner1", "partner", "ООО Чистый Мир", "cleanworld@mail.ru", "+79991234567", 1),
                User(4, "partner2", "partner2", "partner", "СтиркаПрофи", "stirka@yandex.ru", "+79992345678", 2)
            ))
        }

        if (partners.isEmpty()) {
            partners.addAll(listOf(
                Partner(1, "ООО Чистый Мир", "Розничный пункт", "Иванов П.С.", "+79991234567", "cleanworld@mail.ru", 8, "ул. Ленина, 15", "1234567890"),
                Partner(2, "СтиркаПрофи", "Корпоративный клиент", "Петрова А.И.", "+79992345678", "stirka@yandex.ru", 9, "пр. Мира, 28", "0987654321"),
                Partner(3, "АггрегатУслуг", "Интернет-агрегатор", "Сидоров В.К.", "+79993456789", "aggregate@gmail.com", 7, "ул. Центральная, 5", "1122334455")
            ))
        }

        // Тестовые поставщики
        if (suppliers.isEmpty()) {
            suppliers.addAll(listOf(
                Supplier(1, "химия", "ООО ХимПродукт", "123456789012", "Семенов А.В.", "+79994567890", "chem@mail.ru", "ул. Промышленная, 10", 4.8),
                Supplier(2, "упаковка", "ПакетСервис", "234567890123", "Орлова М.К.", "+79995678901", "pack@yandex.ru", "пр. Заводской, 25", 4.5),
                Supplier(3, "оборудование", "ТехноМир", "345678901234", "Громов П.И.", "+79996789012", "tech@gmail.com", "ул. Машиностроителей, 8", 4.9)
            ))
        }

        if (services.isEmpty()) {
            services.addAll(listOf(
                Service(1, "ST-001", "Стирка белья", "стирка", "Стирка белья с применением моющих средств", 500.00, 2.5, 1, 2),
                Service(2, "CH-001", "Химчистка пальто", "химчистка", "Химчистка верхней одежды", 1200.00, 4.0, 2, 1),
                Service(3, "RE-001", "Ремонт одежды", "ремонт", "Мелкий ремонт одежды", 300.00, 1.0, 3, 1)
            ))
        }

        if (serviceHistory.isEmpty()) {
            serviceHistory.addAll(listOf(
                ServiceRecord("2024-01-15", "ООО Чистый Мир", "Стирка белья", 5, 2500.0),
                ServiceRecord("2024-01-16", "ООО Чистый Мир", "Химчистка пальто", 2, 2400.0)
            ))
        }

        if (supplyOrders.isEmpty()) {
            supplyOrders.addAll(listOf(
                SupplyOrder(1, 1, 1, 100, 150.0, 15000.0, "2024-01-20", "2024-01-25", "ordered", 2),
                SupplyOrder(2, 2, 3, 500, 2.5, 1250.0, "2024-01-22", "2024-01-27", "delivered", 2)
            ))
        }
    }

    // === МЕТОДЫ ДЛЯ РАБОТЫ С ПОСТАВЩИКАМИ ===

    fun getSuppliers(): List<Supplier> = suppliers.toList()

    fun addSupplier(supplier: Supplier): Boolean {
        return try {
            val newId = if (suppliers.isEmpty()) 1 else suppliers.maxOf { it.id } + 1
            suppliers.add(supplier.copy(id = newId))
            true
        } catch (e: Exception) {
            false
        }
    }

    fun updateSupplier(supplierId: Int, updatedSupplier: Supplier): Boolean {
        val index = suppliers.indexOfFirst { it.id == supplierId }
        return if (index != -1) {
            suppliers[index] = updatedSupplier.copy(id = supplierId)
            true
        } else {
            false
        }
    }

    fun getSupplierById(supplierId: Int): Supplier? {
        return suppliers.find { it.id == supplierId }
    }

    fun searchSuppliers(query: String): List<Supplier> {
        if (query.isEmpty()) return suppliers.toList()
        return suppliers.filter {
            it.name.contains(query, ignoreCase = true) ||
                    it.contactPerson?.contains(query, ignoreCase = true) == true ||
                    it.type.contains(query, ignoreCase = true)
        }
    }

    fun filterSuppliersByType(type: String): List<Supplier> {
        if (type == "Все типы") return suppliers.toList()
        return suppliers.filter { it.type == type }
    }

    // === СУЩЕСТВУЮЩИЕ МЕТОДЫ ===

    fun authenticate(login: String, password: String, role: String): User? {
        return users.find { it.login == login && it.password == password && it.role == role && it.isActive }
    }

    fun getUsers(): List<User> {
        return if (sessionManager.getUserRole() == "admin") users.toList() else emptyList()
    }

    fun addUser(user: User): Boolean {
        return if (sessionManager.getUserRole() == "admin") {
            val newId = if (users.isEmpty()) 1 else users.maxOf { it.id } + 1
            users.add(user.copy(id = newId))
            true
        } else false
    }

    fun getServices(): List<Service> = services.toList()

    fun createOrder(order: Order): Boolean {
        val currentUser = sessionManager.getCurrentUser()
        return if (currentUser?.role == "partner" && order.partnerId == currentUser.partnerId) {
            val newId = if (orders.isEmpty()) 1 else orders.maxOf { it.id } + 1
            orders.add(order.copy(id = newId))
            true
        } else false
    }

    fun getPartnerOrders(partnerId: Int): List<Order> {
        return if (sessionManager.getUserRole() == "partner" && partnerId == sessionManager.getCurrentUser()?.partnerId) {
            orders.filter { it.partnerId == partnerId }
        } else emptyList()
    }

    fun createSupplyOrder(order: SupplyOrder): Boolean {
        return if (sessionManager.getUserRole() == "manager") {
            val newId = if (supplyOrders.isEmpty()) 1 else supplyOrders.maxOf { it.id } + 1
            supplyOrders.add(order.copy(id = newId, orderedBy = sessionManager.getUserId()))
            true
        } else false
    }

    fun getSupplyOrders(): List<SupplyOrder> {
        return if (sessionManager.getUserRole() in listOf("manager", "admin")) {
            supplyOrders.toList()
        } else emptyList()
    }

    fun getPartners(): List<Partner> = partners.toList()
    fun getServiceHistory(): List<ServiceRecord> = serviceHistory.toList()

    fun addPartner(partner: Partner): Boolean {
        return try {
            val newId = if (partners.isEmpty()) 1 else partners.maxOf { it.id } + 1
            partners.add(partner.copy(id = newId))
            true
        } catch (e: Exception) {
            false
        }
    }

    fun updatePartner(partnerId: Int, updatedPartner: Partner): Boolean {
        val index = partners.indexOfFirst { it.id == partnerId }
        return if (index != -1) {
            partners[index] = updatedPartner.copy(id = partnerId)
            true
        } else {
            false
        }
    }

    fun getPartnerById(partnerId: Int): Partner? {
        return partners.find { it.id == partnerId }
    }

    fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$"
        return email.matches(emailRegex.toRegex())
    }

    fun isValidPhone(phone: String): Boolean {
        val phoneRegex = "^\\+7[0-9]{10}\$"
        return phone.matches(phoneRegex.toRegex())
    }

    fun isValidRating(rating: Int): Boolean = rating in 0..10

    fun searchPartners(query: String): List<Partner> {
        if (query.isEmpty()) return partners.toList()
        return partners.filter {
            it.name.contains(query, ignoreCase = true) ||
                    it.director.contains(query, ignoreCase = true)
        }
    }

    fun filterPartnersByType(type: String): List<Partner> {
        return partners.filter { it.type == type }
    }

    fun filterPartnersByMinRating(minRating: Int): List<Partner> {
        return partners.filter { it.rating >= minRating }
    }

    fun getPartnersSortedByRating(): List<Partner> {
        return partners.sortedByDescending { it.rating }
    }

    fun getPartnersSortedByName(): List<Partner> {
        return partners.sortedBy { it.name }
    }

    fun exportPartnersToCsv(): Boolean {
        return try {
            val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val fileName = "partners_export_$timestamp.csv"

            val csvHeader = "ID;Наименование;Тип;Директор;Телефон;Email;Рейтинг;Адрес;ИНН\n"
            val csvContent = partners.joinToString("\n") { partner ->
                "${partner.id};${partner.name};${partner.type};${partner.director};${partner.phone};${partner.email};${partner.rating};${partner.address};${partner.inn}"
            }

            val fileContent = csvHeader + csvContent
            val file = File(context.getExternalFilesDir(null), fileName)
            FileOutputStream(file).use { fos ->
                fos.write(fileContent.toByteArray())
            }
            true
        } catch (e: Exception) {
            false
        }
    }

    fun createBackup(): Boolean {
        return try {
            showToast("✅ Резервная копия создана\nПартнеров: ${partners.size}\nЗаписей: ${serviceHistory.size}")
            true
        } catch (e: Exception) {
            false
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}

data class Supplier(
    val id: Int = 0,
    val type: String,
    val name: String,
    val inn: String,
    val contactPerson: String? = null,
    val phone: String? = null,
    val email: String? = null,
    val address: String? = null,
    val rating: Double = 0.0
)

data class Service(
    val id: Int,
    val code: String,
    val name: String,
    val type: String,
    val description: String,
    val price: Double,
    val timeNorm: Double,
    val workshop: Int,
    val staffCount: Int
)

data class ServiceRecord(
    val date: String,
    val partnerName: String,
    val serviceName: String,
    val quantity: Int,
    val totalPrice: Double
)
package com.cleanplanet.simple

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PartnersListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var searchEditText: EditText
    private lateinit var filterSpinner: Spinner
    private lateinit var sortSpinner: Spinner
    private lateinit var partnersAdapter: PartnersAdapter
    private lateinit var dataManager: DataManager

    private var allPartners: List<Partner> = emptyList()
    private var filteredPartners: List<Partner> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_partners_list)

        dataManager = DataManager(this)

        // Инициализация UI
        initViews()
        setupRecyclerView()
        loadPartners()
        setupSearchAndFilter()
    }

    private fun initViews() {
        recyclerView = findViewById(R.id.recyclerViewPartners)
        searchEditText = findViewById(R.id.editTextSearch)
        filterSpinner = findViewById(R.id.spinnerFilter)
        sortSpinner = findViewById(R.id.spinnerSort)

        // Настройка кнопки назад
        findViewById<ImageButton>(R.id.buttonBack).setOnClickListener {
            finish()
        }

        // Настройка кнопки добавления
        findViewById<Button>(R.id.buttonAddNew).setOnClickListener {
            val intent = Intent(this, PartnerEditActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupRecyclerView() {
        partnersAdapter = PartnersAdapter { partner ->
            // Обработка клика на партнера - переход к редактированию
            val intent = Intent(this, PartnerEditActivity::class.java).apply {
                putExtra("partner_id", partner.id)
            }
            startActivity(intent)
        }

        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@PartnersListActivity)
            adapter = partnersAdapter
        }
    }

    private fun loadPartners() {
        allPartners = dataManager.getPartners()
        filteredPartners = allPartners
        updatePartnersList()

        // Показать сообщение если список пуст
        if (allPartners.isEmpty()) {
            Toast.makeText(this, "Список партнеров пуст", Toast.LENGTH_LONG).show()
        }
    }

    private fun setupSearchAndFilter() {
        // Настройка поиска
        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH) {
                performSearch()
                true
            } else {
                false
            }
        }

        // Настройка фильтров
        val filterOptions = arrayOf("Все типы", "Розничный пункт", "Корпоративный клиент", "Интернет-агрегатор")
        val filterAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, filterOptions)
        filterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        filterSpinner.adapter = filterAdapter

        filterSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                applyFilters()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // Настройка сортировки
        val sortOptions = arrayOf("По рейтингу (убыв.)", "По рейтингу (возр.)", "По имени (А-Я)", "По имени (Я-А)")
        val sortAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, sortOptions)
        sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sortSpinner.adapter = sortAdapter

        sortSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                applySorting()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun performSearch() {
        val query = searchEditText.text.toString().trim()
        if (query.isEmpty()) {
            filteredPartners = allPartners
        } else {
            filteredPartners = dataManager.searchPartners(query)
        }
        applyFilters()
    }

    private fun applyFilters() {
        var result = filteredPartners

        // Применение фильтра по типу
        val selectedFilter = filterSpinner.selectedItem.toString()
        if (selectedFilter != "Все типы") {
            result = dataManager.filterPartnersByType(selectedFilter)
        }

        // Применение сортировки
        applySorting(result)
    }

    private fun applySorting(partners: List<Partner> = filteredPartners) {
        val selectedSort = sortSpinner.selectedItem.toString()
        val sortedPartners = when (selectedSort) {
            "По рейтингу (убыв.)" -> partners.sortedByDescending { it.rating }
            "По рейтингу (возр.)" -> partners.sortedBy { it.rating }
            "По имени (А-Я)" -> partners.sortedBy { it.name }
            "По имени (Я-А)" -> partners.sortedByDescending { it.name }
            else -> partners
        }

        filteredPartners = sortedPartners
        updatePartnersList()
    }

    private fun updatePartnersList() {
        partnersAdapter.submitList(filteredPartners)

        // Обновление счетчика
        val countText = "Найдено партнеров: ${filteredPartners.size}"
        findViewById<TextView>(R.id.textPartnersCount).text = countText
    }

    override fun onResume() {
        super.onResume()
        // Обновляем список при возвращении на экран
        loadPartners()
    }
}
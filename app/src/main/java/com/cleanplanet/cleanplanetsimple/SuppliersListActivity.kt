package com.cleanplanet.simple

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SuppliersListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var searchEditText: EditText
    private lateinit var filterSpinner: Spinner
    private lateinit var sortSpinner: Spinner
    private lateinit var suppliersAdapter: SuppliersAdapter
    private lateinit var dataManager: DataManager

    private var allSuppliers: List<Supplier> = emptyList()
    private var filteredSuppliers: List<Supplier> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_suppliers_list)

        dataManager = DataManager(this)

        initViews()
        setupRecyclerView()
        loadSuppliers()
        setupSearchAndFilter()
    }

    private fun initViews() {
        recyclerView = findViewById(R.id.recyclerViewSuppliers)
        searchEditText = findViewById(R.id.editTextSearch)
        filterSpinner = findViewById(R.id.spinnerFilter)
        sortSpinner = findViewById(R.id.spinnerSort)

        findViewById<ImageButton>(R.id.buttonBack).setOnClickListener {
            finish()
        }

        findViewById<Button>(R.id.buttonAddNew).setOnClickListener {
            val intent = Intent(this, SupplierEditActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupRecyclerView() {
        suppliersAdapter = SuppliersAdapter { supplier ->
            val intent = Intent(this, SupplierEditActivity::class.java).apply {
                putExtra("supplier_id", supplier.id)
            }
            startActivity(intent)
        }

        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@SuppliersListActivity)
            adapter = suppliersAdapter
        }
    }

    private fun loadSuppliers() {
        allSuppliers = dataManager.getSuppliers()
        filteredSuppliers = allSuppliers
        updateSuppliersList()

        if (allSuppliers.isEmpty()) {
            Toast.makeText(this, "Список поставщиков пуст", Toast.LENGTH_LONG).show()
        }
    }

    private fun setupSearchAndFilter() {
        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH) {
                performSearch()
                true
            } else {
                false
            }
        }

        val filterOptions = arrayOf("Все типы", "химия", "упаковка", "оборудование", "прочее")
        val filterAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, filterOptions)
        filterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        filterSpinner.adapter = filterAdapter

        filterSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                applyFilters()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

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
            filteredSuppliers = allSuppliers
        } else {
            filteredSuppliers = dataManager.searchSuppliers(query)
        }
        applyFilters()
    }

    private fun applyFilters() {
        var result = filteredSuppliers

        val selectedFilter = filterSpinner.selectedItem.toString()
        if (selectedFilter != "Все типы") {
            result = dataManager.filterSuppliersByType(selectedFilter)
        }

        applySorting(result)
    }

    private fun applySorting(suppliers: List<Supplier> = filteredSuppliers) {
        val selectedSort = sortSpinner.selectedItem.toString()
        val sortedSuppliers = when (selectedSort) {
            "По рейтингу (убыв.)" -> suppliers.sortedByDescending { it.rating }
            "По рейтингу (возр.)" -> suppliers.sortedBy { it.rating }
            "По имени (А-Я)" -> suppliers.sortedBy { it.name }
            "По имени (Я-А)" -> suppliers.sortedByDescending { it.name }
            else -> suppliers
        }

        filteredSuppliers = sortedSuppliers
        updateSuppliersList()
    }

    private fun updateSuppliersList() {
        suppliersAdapter.submitList(filteredSuppliers)
        findViewById<TextView>(R.id.textSuppliersCount).text = "Найдено поставщиков: ${filteredSuppliers.size}"
    }

    override fun onResume() {
        super.onResume()
        loadSuppliers()
    }
}
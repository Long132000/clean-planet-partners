package com.cleanplanet.simple

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ServiceHistoryActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var spinnerPartnerFilter: Spinner
    private lateinit var dataManager: DataManager
    private lateinit var historyAdapter: ServiceHistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service_history)

        dataManager = DataManager(this)

        initViews()
        setupRecyclerView()
        loadServiceHistory()
        setupPartnerFilter()
    }

    private fun initViews() {
        recyclerView = findViewById(R.id.recyclerViewHistory)
        spinnerPartnerFilter = findViewById(R.id.spinnerPartnerFilter)

        // Кнопка назад
        findViewById<ImageButton>(R.id.buttonBack).setOnClickListener {
            finish()
        }
    }

    private fun setupRecyclerView() {
        historyAdapter = ServiceHistoryAdapter()
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@ServiceHistoryActivity)
            adapter = historyAdapter
        }
    }

    private fun loadServiceHistory() {
        val history = dataManager.getServiceHistory()
        historyAdapter.submitList(history)

        if (history.isEmpty()) {
            Toast.makeText(this, "История услуг пуста", Toast.LENGTH_LONG).show()
        }
    }

    private fun setupPartnerFilter() {
        val partners = dataManager.getPartners()
        val partnerNames = mutableListOf("Все партнеры")
        partnerNames.addAll(partners.map { it.name })

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, partnerNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerPartnerFilter.adapter = adapter

        spinnerPartnerFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                filterHistoryByPartner(position)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun filterHistoryByPartner(selectedPosition: Int) {
        val history = dataManager.getServiceHistory()
        val filteredHistory = if (selectedPosition == 0) {
            history // Все партнеры
        } else {
            val selectedPartnerName = spinnerPartnerFilter.selectedItem.toString()
            history.filter { it.partnerName == selectedPartnerName }
        }

        historyAdapter.submitList(filteredHistory)

        // Обновление счетчика
        val countText = "Найдено записей: ${filteredHistory.size}"
        findViewById<TextView>(R.id.textHistoryCount).text = countText
    }
}